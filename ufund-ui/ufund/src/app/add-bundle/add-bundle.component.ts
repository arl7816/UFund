import { Component } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';
import { NeedService } from '../need.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Need } from '../Interfaces/Need';
import { UserService } from '../user.service';
import { Bundle } from '../Interfaces/Bundle';
import { BundleService } from '../bundle.service';

@Component({
  selector: 'app-add-bundle',
  templateUrl: './add-bundle.component.html',
  styleUrl: './add-bundle.component.css'
})
export class AddBundleComponent implements OnInit, OnDestroy {
  
  private bundles: Bundle[] = [];
  public needs: Need[] = [];
  private checked: Map<Need,boolean> = new Map<Need,boolean>(); 

  newName: string | undefined;
  newList: Need[] | undefined;
  
  constructor(private needService: NeedService, 
    private route: ActivatedRoute, private router: Router, private bundleservice: BundleService) { }

    ngOnInit() {
      if (UserService.fetchUser() != "admin"){
        this.router.navigate(['/login']);
      }
      this.getNeeds();

      document.getElementById("errorBox")!.style.visibility = "hidden";

    }

    getNeeds(): void{
      this.needService.getNeeds()
      .subscribe(needs => this.needs = needs);

      // put needs in map to keep track of checked
      this.needs.forEach(need =>
        this.checked.set(need,false));
    }
  
    ngOnDestroy() {
      //this.sub.unsubscribe();
    }

    checkDataCorrect(name?: string, list?: Need[]): boolean{
      let result:  boolean = true;
      let errorMessage: string = "Errors:";

      if (name === undefined){
        errorMessage += "<br />* Name is not defined"
        result = false;
      }else{
        this.bundleservice.getBundles()
        .subscribe(bundles => this.bundles = bundles);

        this.bundles.forEach(tempBundle => {
          if (tempBundle.name.toLowerCase().trim() == name.toLowerCase().trim()){
            errorMessage += "<br />* Name already exists";
            result = false;
            return;
          }
        });
      }

      if (list === undefined){
        errorMessage += "<br />* Bundle Must Contain Needs"
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
      if (!this.checkDataCorrect(this.newName, this.newList)){
        return;
      };
      
      // this line is just so the compiler will stop yelling, the checkDataCorrect 
      // method already confirms that these are defined
      if (this.newName === undefined || this.newList === undefined){ return ;}

      this.bundleservice.createBundle(this.newName, this.newList)
        .subscribe({next: value => {
            // have to put this line for it to compile
            if (this.newList=== undefined || this.newName === undefined){return;}
            value.list = this.newList;
            value.name = this.newName;
            this.bundleservice.updateBundle(value).subscribe();
            this.router.navigate(['/cupboard']);
          }
        }
        );
    }


    updateChecked(need: Need): void
    {
      this.checked.set(need,!this.checked.get(need));
      let temp: Array<Need> = new Array<Need>;
      for (let key of this.checked.keys())
      {
        if (this.checked.get(key))
        {
          temp.push(key);
        }
      }
      this.newList = temp;
      
    }

}
