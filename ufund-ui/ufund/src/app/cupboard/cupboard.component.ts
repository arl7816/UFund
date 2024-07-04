import { Component, OnInit } from '@angular/core';

import { NeedService } from '../need.service';
import { Need } from '../Interfaces/Need';
import { UserService } from '../user.service';
import { User } from '../Interfaces/User';
import { Router } from '@angular/router';
import { Renderer2 } from '@angular/core';
import { BundleService } from '../bundle.service';
import { Bundle } from '../Interfaces/Bundle';
import { NotificationService } from '../notification-service.service';


@Component({
  selector: 'app-cupboard',
  templateUrl: './cupboard.component.html',
  styleUrl: './cupboard.component.css'
})
export class CupboardComponent implements OnInit{
  needs: Need[] = [];
  bundles: Bundle[] = [];
  selected: number = -1; // -1 implies nothing is selected
  selectedName: string = "None";
  search: string | undefined;
  bundleSelected: number = -1;
  bundleName: string = "None";


  constructor(private needService: NeedService, private bundleservice: BundleService, private router: Router, 
    private renderer: Renderer2, private notifyService: NotificationService, private userservice: UserService) { }


  ngOnInit(): void {
    if (!(UserService.fetchUser() === 'admin')){
      this.router.navigate(['/login']);
    }

    this.getNeeds();
    this.getBundles();
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

  updateSelect(id: number, name: string): void{
    if (this.selected !== -1){
      const prevButton = document.getElementById("button_" + this.selected);
      prevButton!.classList.remove('selected'); // Remove 'selected' class
    }
    
    const currentButton = document.getElementById("button_" + id);
    currentButton!.classList.add('selected'); // Add 'selected' class
    this.selected = id;
    this.selectedName = name;
  }

  updateBundleSelect(id: number, name: string): void{
    if (this.selected !== -1){
      const prevButton = document.getElementById("button_" + this.selected);
      prevButton!.classList.remove('selected'); // Remove 'selected' class
    }
    
    const currentButton = document.getElementById("button_" + id);
    currentButton!.classList.add('selected'); // Add 'selected' class
    this.bundleSelected = id;
    this.bundleName = name;
  }


  removeNeed(): void{
    this.needService.deleteNeed(this.selected).subscribe(
      {next: value => this.getNeeds()}
    );
    if (this.selectedName != "None"){
      this.notifyService.createEmitNotification("Need Deleted", this.selectedName + " was deleted");
    }
    this.selected = -1;
    this.selectedName = "None";
  }

  removeBundle(): void{
    this.bundleservice.deleteBundle(this.bundleSelected).subscribe(
      {next: value => this.getBundles()}
    );
    this.bundleSelected = -1;
    this.bundleName = "None";
  }

  logout(): void{
    this.userservice.removeUser();
    this.router.navigateByUrl("/login");
  }
}
