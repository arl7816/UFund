import { Component, OnInit , OnDestroy} from '@angular/core';
import { NeedService } from '../need.service';
import { Need } from '../Interfaces/Need';
import { ActivatedRoute, RouterLink , Router} from '@angular/router';
import { UserService } from '../user.service';
import { NotificationService } from '../notification-service.service';

@Component({
  selector: 'app-edit-need',
  templateUrl: './edit-need.component.html',
  styleUrl: './edit-need.component.css'
})
export class EditNeedComponent implements OnInit, OnDestroy{
  private id: number = -1;
  private sub: any;
  
  need: Need = Object(); 
  needs: Need[] = [];

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

      this.sub = this.route.params.subscribe(params => {
         this.id = +params['id']; // (+) converts string 'id' to a number
         if (this.id == -1){
          this.router.navigate(['/cupboard']);
         }
      });

      this.needService.getNeed(this.id).subscribe(
        value => this.need = value);
      this.newName = this.need.name;
      this.newType = this.need.type;

      this.needService.getNeeds().subscribe(needs => this.needs = needs);
    }
  
    ngOnDestroy() {
      this.sub.unsubscribe();
    }

    checkDataCorrect(need: Need): boolean{
      let result:  boolean = true;
      let errorMessage: string = "Errors:";

      if (!Number.isInteger(need.quantity)){
        errorMessage += "<br />* Quantity can only be a whole number";
        result = false;
      }

      if (need.quantity < 0){
        errorMessage += "<br />* Quantity must be greater than or equal to 0";
        result = false;
      }

      this.needs.forEach(tempNeed => {
        if (tempNeed.name.toLowerCase().trim() == need.name.toLowerCase().trim()
            && tempNeed.id != need.id) {
          errorMessage += "<br />* Name already exists";
          result = false;
        }
      });
      

      if (!Number.isInteger(need.cost)){
        errorMessage += "<br />* Cost can only be a whole number for now";
        result = false;
      }

      if (need.cost <= 0){
        errorMessage += "<br />* Cost must be greater than 0"
        result = false;
      }

      if (!result){
        document.getElementById("errorBox")!.style.setProperty("visibility", "visible");
        document.getElementById("errors")!.innerHTML= errorMessage;
      }
      return result;
    }

    save(){
      let prevName = this.need.name;
      if (this.newName != undefined && this.newName!.trim() != "") {this.need.name = this.newName}
      if (this.newCost != undefined){this.need.cost = this.newCost}
      if (this.newQuantity != undefined){this.need.quantity = this.newQuantity}
      if (this.newType != undefined){this.need.type = this.newType}

      // double check data types
      if (!this.checkDataCorrect(this.need)){
        return;
      };

      this.need.name = this.need.name.trim();
      this.needService.updateNeed(this.need).subscribe();
      this.notifyService.createEmitNotification("Need Edited", "The need " + prevName + " was edited, come check out the changes as " + this.need.name);
      this.router.navigate(['/cupboard'])
    }
}
