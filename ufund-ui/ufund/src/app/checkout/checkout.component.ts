import { Component, OnInit } from '@angular/core';
import { Need } from '../Interfaces/Need'; 
import { NeedService } from '../need.service';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { Observable, catchError, forkJoin, map, of, switchMap } from 'rxjs';
import { User } from '../Interfaces/User';
import { NotificationService } from '../notification-service.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {
  basketItems: Need[] = [];
  totalAmount: number = 0;
  checkoutSuccess: boolean = false; // Flag to indicate checkout success
  successMessage: string = ''; // Message to display upon successful checkout
  wallet: number = 0;
  user: User = Object()
  
  constructor(
    private basketService: NeedService,
    private userservice: UserService,
    private router: Router,
    private needService: NeedService,
    private notifyService: NotificationService) { }

  ngOnInit(): void {
    
    this.getBasketItems();
    this.calculateTotal();
    this.userservice.getUser(UserService.fetchUser()).subscribe(
      user => {
        //this.wallet = user.wallet
        this.user = user; 
      }

    )
    if (UserService.fetchUser() == ''||UserService.fetchUser()=='admin'){
      this.router.navigate(['/login']);
    }

  }

  getBasketItems(): void {
    this.userservice.getBasket(UserService.fetchUser()).pipe(
      switchMap(needs => {
        // Check if the 'needs' array is empty
        if (needs.length === 0) {

          
    
          // Return an observable emitting an empty array
          return of([]);
        } else {
          // Map each need to an observable that fetches the corresponding need object
          const requests = needs.map(need => this.needService.getNeed(need.id));
      
          // Use forkJoin to wait for all requests to complete
          return forkJoin(requests).pipe(
            map(results => {
              // Filter out any null or undefined values (deleted needs)
              return results.filter(need => need !== null && need !== undefined);
            }),
            catchError(error => {
              console.error("Error fetching needs:", error);
              // Handle the error here, if needed
              return [];
            })
          );
        }
      })
    ).subscribe({
      next: filteredNeeds => {
        // Now you have the filtered array of needs without the deleted ones
        this.basketItems = filteredNeeds;
        this.calculateTotal();
      },
      error: err => {
        console.error("Error fetching basket items:", err);
        // Handle the error here, if needed
      }
    });
  }
  
  

  calculateTotal(): void {
    let total = 0; // Initialize total amount
    for (let item of this.basketItems) {
      total += item.cost; // Add the cost of each item multiplied by its quantity
    }
    this.totalAmount = total;
  }

  removeItem(needToRemove: Need) {
    if (confirm('Are you sure you want to remove this item?')) {
      this.userservice.removeFromBasket(UserService.fetchUser(),needToRemove).subscribe({next: value => this.getBasketItems()});
  }
}


checkout(): void {
  let pairs: Map<number, number> = new Map<number, number>();
  let basketMap: Map<number, Need> = new Map<number, Need>();

  if (this.user.wallet < this.totalAmount){
    alert("You do not have enough funds for this checkout, please remove some items " +
    "or increase your wallet");
    return;
  }

  // otherwise the user is totally able to do it
  this.user.wallet -= this.totalAmount;
  this.userservice.updateUser(this.user).subscribe();

  this.basketItems.forEach((element, index) => {
    let num = pairs.get(element.id);
    if (num === undefined){
      pairs.set(element.id, 1);
      basketMap.set(element.id, element);
    }else{
      pairs.set(element.id, num + 1);
    }
  });

  // check if there is any surplus
  // if there is a surplus ask the user if they want to continue or not
  let surplus: string = "";
  basketMap.forEach(
    need => {
      let buying: number | undefined = pairs.get(need.id);
      if (buying){
        if (buying > need.quantity){
          surplus += "An extra " + (buying - need.quantity) + " will be bought for " + need.name + "\n";
          need.surplus += (buying - need.quantity);
          this.needService.updateNeed(need).subscribe();
        }
      }
    }
  )

  // check for the surplus and confirm that the user wants to carry on
  if (surplus !== ""){
    if (!confirm("Selling the following items will result in a surplus, would you like to continue?\n" 
    + surplus)){
      return;
    }
  }

  // Array to store update requests for each need
  const updateRequests: Observable<any>[] = [];

  // Iterate over the map entries to update the quantities of each need
  pairs.forEach((quantity, needId) => {
    // Fetch the need from the backend
    debugger;
    const updateRequest = this.needService.getNeed(needId).pipe(
      switchMap(need => {
        need.surplus = 0;
        // Calculate the new quantity by subtracting the quantity in the basket
        const newQuantity = need.quantity - quantity;

        if (newQuantity <= 0 && need.quantity != 0){
          // make the nofitcation that further more will be surplus
          this.notifyService.createEmitNotification("Surplus found", need.name + " has met its goal, will now be surplus");
        }

        // Update the need with the new quantity
        need.quantity = newQuantity >= 0 ? newQuantity : 0;
        return this.needService.updateNeed(need);
      })
    );
    debugger;
    // Add the update request to the array
    updateRequests.push(updateRequest);
  });

  // Use forkJoin to wait for all update requests to complete
  forkJoin(updateRequests).subscribe({
    next: () => {
      // All needs have been updated, now remove them from the basket
      this.basketItems.forEach(basketNeed => {
        this.userservice.removeFromBasket(UserService.fetchUser(), basketNeed).subscribe(
          {next: value => this.getBasketItems()}
        );
      //return this.getBasketItems();
      });  
    },
    error: err => {
      console.error('Error processing checkout:', err);
    }
  });
}

leave():void
{
  if(this.checkoutSuccess)
  {
    this.router.navigate(['/basket']);
}
}

}   