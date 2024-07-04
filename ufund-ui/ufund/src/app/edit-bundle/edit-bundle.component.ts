import { Component, OnInit , OnDestroy} from '@angular/core';
import { NeedService } from '../need.service';
import { Need } from '../Interfaces/Need';
import { ActivatedRoute, RouterLink , Router} from '@angular/router';
import { UserService } from '../user.service';
import { BundleService } from '../bundle.service';
import { Bundle } from '../Interfaces/Bundle';

@Component({
  selector: 'app-edit-bundle',
  templateUrl: './edit-bundle.component.html',
  styleUrl: './edit-bundle.component.css'
})
export class EditBundleComponent {

  private id: number = -1;
  private sub: any;
  public debug: string = "Wazzup";
  
  bundle: Bundle = Object(); 
  needs: Need[] = [];
  checked: Map<Need, boolean> = new Map<Need, boolean>();
  bundles: Bundle[] = [];

  newName: string | undefined;
  newList: Need[] = [];

  constructor(private needService: NeedService, 
    private route: ActivatedRoute, private router: Router, private bundleservice: BundleService) { }

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

      this.bundleservice.getBundle(this.id).subscribe(
        value => this.bundle = value);
      this.newName = this.bundle.name;
      this.newList = this.bundle.list;
      this.debug = "got bundle and its name, needlist"

      this.needService.getNeeds().subscribe(needs => this.needs = needs);
      this.debug = 'got all the needs' // get all the needs
      this.needs.forEach(need => 
        {
          if(this.bundle.list.includes(need)) // included needs should be pre-checked
          {
            this.checked.set(need,true);
          }
          else
          this.checked.set(need,false); // other needs are included, unchecked
          this.debug = 'set up the checked map'
        });

        this.bundleservice.getBundles().subscribe(bundles => this.bundles = bundles);
        this.debug = 'subscribe to all bundles';
    }
  
    ngOnDestroy() {
      this.sub.unsubscribe();
    }

    checkDataCorrect(bundle: Bundle): boolean{
      let result:  boolean = true;
      let errorMessage: string = "Errors:";
      this.debug ='reached error checking'

      
      this.bundles.forEach(tempBundle => {
        if (tempBundle.name.toLowerCase().trim() == bundle.name.toLowerCase().trim()
            && tempBundle.id != bundle.id) {
          errorMessage += "<br />* Name already exists";
          this.debug = 'reached illegal name failure'
          result = false;
        }

        if(this.newList===undefined||this.newList.length<=0)
        {
          this.debug ='reached illegal list failure'
          errorMessage += "<br />* Bundle must contain at least one need";
          result = false;
        }
      });

      this.debug = 'No illegal args detected, return value of true'
      result = true;
      return result;
    }

    save(){
      this.debug ='save called';
      if (this.newName !== undefined){this.bundle.name = this.newName; this.debug = 'name change detected';}
      this.bundle.list = this.newList;

      
      // double check data types
      if (!this.checkDataCorrect(this.bundle)){
        return;
      };

      this.bundleservice.updateBundle(this.bundle).subscribe();
      this.router.navigate(['/cupboard'])
    }


    checkAddNeeds(need: Need): void{
      this.debug = 'checked called'
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
