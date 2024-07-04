import { Component, OnInit } from '@angular/core';
import { User } from '../Interfaces/User';
import { Need } from '../Interfaces/Need';
import { FundingBasket } from '../Interfaces/FundingBasket';
import { ActivatedRoute, RouterLink , Router} from '@angular/router';
import { Location } from '@angular/common';
import { LocalStorageService } from '../local-storage.service';
import { UserService } from '../user.service';
import { CommonModule } from '@angular/common';
import { NeedService } from '../need.service';
import { catchError, first, forkJoin, map, of, switchMap } from 'rxjs';
@Component({
  selector: 'app-funding-basket',
  templateUrl: './funding-basket.component.html',
  styleUrl: './funding-basket.component.css'
})
export class FundingBasketComponent implements OnInit {


  needs: Need[] = [];
  username: string = UserService.fetchUser();

  constructor(
    private userservice: UserService,
    private needservice: NeedService,
    private router: Router

  ) {}

  ngOnInit(): void {
      if(this.username == '')
      {
        this.router.navigate(['/login'])
      }
      if (this.username == 'admin')
      {
        this.router.navigate(['/cupboard'])
      }
      this.getBasket();
  }

  getBasket(): void {
    this.userservice.getBasket(UserService.fetchUser()).pipe(
      switchMap(needs => {
        // Check if the 'needs' array is empty
        if (needs.length === 0) {
          // Return an observable emitting an empty array
          return of([]);
        } else {
          // Map each need to an observable that fetches the corresponding need object
          const requests = needs.map(need => this.needservice.getNeed(need.id));
      
          // Use forkJoin to wait for all requests to complete
          return forkJoin(requests).pipe(
            map(results => {
              // Filter out any null or undefined values (deleted needs)
              return results.filter(need => need !== null && need !== undefined);
            }),
            catchError(error => {
              // Handle the error here, if needed
              return [];
            })
          );
        }
      })
    ).subscribe({
      next: filteredNeeds => {
        // Now you have the filtered array of needs without the deleted ones
        this.needs = filteredNeeds;
      },
      error: err => {
        console.error("Error fetching basket items:", err);
        // Handle the error here, if needed
      }
    });
  }


    remove(need: Need): void
    {
      this.userservice.removeFromBasket(this.username,need).subscribe({next: value => this.getBasket()}); 
    }

    toCheckout() {
      this.userservice.getBasket(this.username).subscribe((basket)=>{
        if (basket.length == 0){
          alert("Your basket is empty");
        } else {
          this.router.navigateByUrl("/checkout");
        }
      });
    }
}

  



