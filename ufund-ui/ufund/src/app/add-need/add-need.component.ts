import { Component } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';
import { NeedService } from '../need.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Need } from '../Interfaces/Need';
import { UserService } from '../user.service';
import { NotificationService } from '../notification-service.service';

@Component({
  selector: 'app-add-need',
  templateUrl: './add-need.component.html',
  styleUrl: './add-need.component.css'
})
export class AddNeedComponent implements OnInit, OnDestroy{
  private id: number = -1;
  private sub: any;
  private newNeed: Need = Object();
  private needs: Need[] = [];

  newName: string | undefined;
  newCost: number | undefined;
  newQuantity: number | undefined;
  newType: string | undefined;
  
  constructor(private needService: NeedService, 
    private route: ActivatedRoute, private router: Router,
    private notifyService: NotificationService) { }

    ngOnInit() {
      if (UserService.fetchUser() != "admin"){
        this.router.navigate(['/login']);
      }

      document.getElementById("errorBox")!.style.visibility = "hidden";

      this.newCost = 1;
    }
  
    ngOnDestroy() {
      //this.sub.unsubscribe();
    }

    checkDataCorrect(name?: string, cost?: number, quantity?: number, type?: string): boolean{
      let result:  boolean = true;
      let errorMessage: string = "Errors:";

      if (quantity === undefined){
        errorMessage += "<br />* Quantity is not defined"
        result = false;
      }else{
        if (!Number.isInteger(quantity)){
          errorMessage += "<br />* Quantity can only be a whole number";
          result = false;
        }

        if (quantity <= 0){
          errorMessage += "<br />* Quantity must be greater than to 0";
          result = false;
        }
      }

      if (name === undefined){
        errorMessage += "<br />* Name is not defined"
        result = false;
      }else{
        name = name.trim();
        if (name == ""){
          errorMessage += "<br />* Name can't be blank"
          result = false;
        }
      

        this.needService.getNeeds()
        .subscribe(needs => this.needs = needs);

        this.needs.forEach(tempNeed => {
          if (tempNeed.name.toLowerCase().trim() == name!.toLowerCase().trim()){
            errorMessage += "<br />* Name already exist";
            result = false;
            return;
          }
        });
      }

      if (cost === undefined){
        errorMessage += "<br />* Cost is not defined"
        result = false;
      }else{
        if (!Number.isInteger(cost)){
          errorMessage += "<br />* Cost can only be a whole number for now";
          result = false;
        }

        if (cost <= 0){
          errorMessage += "<br />* Cost must be greater than 0"
          result = false;
        }
      }

      if (type === undefined){
        errorMessage += "<br />* Type is not defined"
        result = false;
      }

      if (!result){
        document.getElementById("errorBox")!.style.setProperty("visibility", "visible");
        document.getElementById("errors")!.innerHTML= errorMessage;
      }
      return result;
    }

    add(): void{
      // check to make sure the data types are correct later
      if (!this.checkDataCorrect(this.newName, this.newCost, this.newQuantity, this.newType)){
        return;
      };
      
      // this line is just so the compiler will stop yelling, the checkDataCorrect 
      // method already confirms that these are defined
      if (this.newName === undefined || this.newQuantity === undefined){ return ;}

      this.needService.createNeed(this.newName.trim(), this.newQuantity)
        .subscribe({next: value => {
            // have to put this line for it to compile
            if (this.newType === undefined || this.newCost === undefined){return;}
            value.type = this.newType;
            value.cost = this.newCost;
            this.needService.updateNeed(value).subscribe();
            this.notifyService.createEmitNotification("NEED ADDED", "Need " + this.newName + " was added to the cupboard" +
            " at a price of " + this.newCost + " and a quantity of " + this.newQuantity);
            this.router.navigate(['/cupboard']);
          }
        }
        );
    }
}
