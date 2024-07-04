import { Component, OnInit, } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NeedService } from '../need.service';
import { Need } from '../Interfaces/Need';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { User } from '../Interfaces/User';
import { Bundle } from '../Interfaces/Bundle';
import { BundleService } from '../bundle.service';


@Component({
  selector: 'app-helper-cupboard',
  templateUrl: './helper-cupboard.component.html',
  styleUrl: './helper-cupboard.component.css'
})
export class HelperCupboardComponent implements OnInit{

  needs: Need[] = [];
  bundles: Bundle[] = [];
  test: string ="";
  username: string = UserService.fetchUser();
  search: string | undefined;
  increaseAmount: number | undefined;
  user: User = Object()

  constructor(private needService: NeedService, private userservice: UserService,
    private bundleservice: BundleService,
    private router: Router) { }

  ngOnInit(): void {
    this.getNeeds();
    this.getBundles();
    console.log(this.username)
    this.userservice.getUser(this.username)
      .subscribe(user => this.user = user);
    if(this.username == '')
      {
        this.router.navigate(['/login'])
      }
      if (this.username == 'admin')
      {
        this.router.navigate(['/cupboard'])
      }
  }

  getNeeds(): void {
    if (this.search == undefined){
      this.needService.getNeeds()
      .subscribe(needs => this.needs = needs);
    }else{
      this.needService.searchNeeds(this.search)
      .subscribe(needs => this.needs = needs);
    }
  }

  getBundles(): void {
    if (this.search ==undefined)
    {
      this.bundleservice.getBundles().subscribe(bundles => this.bundles = bundles);
    }
    else{
      this.bundleservice.searchBundles(this.search).subscribe(bundles => this.bundles = bundles);
    }
  }

  addSelect(need: Need): void{
    this.userservice.addToBasket(UserService.fetchUser(), need).subscribe({next: value => this.getNeeds()});
    this.test = "you have added "+need.name+" to your funding basket!";
  }

  //TODO
  addBundle(bundle: Bundle): void{
    bundle.list.forEach(need => this.addSelect(need) );
    this.test = "Needs from "+bundle.name+" have been successfully added to your basket."

  }

  toggleWalletPopup(): void {
    const walletPopup = document.getElementById('walletPopup');
    if (walletPopup) {
        walletPopup.style.display = (walletPopup.style.display === 'block') ? 'none' : 'block';
    }
  }

  closeWalletPopup(): void {
    const walletPopup = document.getElementById('walletPopup');
    if (walletPopup) {
        walletPopup.style.display = 'none';
    }
  }

  increaseWallet(): void {
    // Implement logic to increase the wallet balance
    if (this.increaseAmount === undefined){
      return;
    }

  if (this.increaseAmount > 0 && Number.isInteger(this.increaseAmount)){
      // Increase wallet balance
      this.user.wallet += this.increaseAmount;

      // Close the pop-up
      this.closeWalletPopup();
      // Reset the input field
      this.increaseAmount = 0;

      this.userservice.getBasket(this.user.username).subscribe(needs => {
        this.userservice.updateUser(this.user).subscribe(value => {
          needs.forEach(element => {
            this.userservice.addToBasket(this.user.username, element).subscribe();
          });
        });
        
      })
      //this.userservice.updateUser(this.user).subscribe();
    }
  }


  logOut() {
    this.userservice.removeUser();
    this.router.navigateByUrl("/login");
  }
}

