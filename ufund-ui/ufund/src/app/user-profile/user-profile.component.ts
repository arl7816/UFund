import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import { NeedService } from '../need.service';
import { Router } from '@angular/router';
import { User } from '../Interfaces/User';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit{
  password: string = "";
  username: string = UserService.fetchUser();
  profile: User = new User();
  user: User = new User();

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {
    console.log("hi");

    if (UserService.fetchUser() == ""){
      this.router.navigate(['/login']);
    }

    this.userService.getUser(this.username).subscribe(user => {
      this.password = user.password;
      this.username = user.username; // Assuming the user object has a password field.
    });
    console.log("password:" + this.password);

    this.userService.getUser(this.username).subscribe({
      next: (user: User) => {
        this.profile = user;
        // handle the next event
      },
      error: (error: any) => {
        // handle the error
        console.error('Error fetching user:', error);
      },
      complete: () => {
        // handle the completion of the Observable
        console.log('User fetch completed');
      }
    });
    this.user.username = ""
    this.user.password = "";
  }
  public userProfileForm: FormGroup = new FormGroup({
    username: new FormControl(''), 
    password: new FormControl('') 
  });

  deleteUser(): void{
    this.userService.deleteUser(this.profile.id).subscribe({
      next: (user: User) => {
        console.log('User deleted successfully', user);
        this.userService.removeUser();
        // Handle the successful deletion
        this.router.navigateByUrl("/login");
      },
      error: (error: any) => {
        console.error('Error deleting user:', error);
        // Handle the error response
      },
      complete: () => {
        // This is optional and can be used if there's logic that should run after completion regardless of the result
        console.log('Deletion request complete');
        // Any cleanup logic or additional navigation can go here
      }
    });
  }

  async checkValid(newUserName: string, newPassword: string): Promise<boolean> {
    
    let user = await this.userService.getUser(newUserName).toPromise();
    if(!user===false)
    {
      alert("User Already Exists");
    }
    return !user; // If user exists, return false; otherwise, return true
    
  }

  async onUpdateUser(){
    let x: boolean = true;
    this.user.username = this.user.username.trim();
    this.user.password = this.user.password.trim();
    
    let result = await this.checkValid(this.user.username, this.user.password);
    if (!result){
      alert("Update Unsuccessful");
      return;
    }

    let oldName = this.username;

    if (this.user.username != ""){
      this.username = this.user.username;
    }
    
    if (this.user.password != ""){
      this.password = this.user.password;
    }
    

    this.profile.username = this.username;
    this.profile.password = this.password;

    this.userService.getBasket(oldName).subscribe(needs => {
      this.userService.updateUser(this.profile).subscribe(value => {
        needs.forEach(element => {
          this.userService.addToBasket(this.profile.username, element).subscribe();
        });
      });

      
      
    })
    /*this.userService.updateUser(this.profile).subscribe({
      next: (res) => {
        console.log(this.profile.username +" "+ this.profile.password);
        alert("Profile has been updated!");
        // Additional actions after successful update
      },
      error: (error) => {
        console.error("Error updating profile:", error);
        // Handle errors, possibly show user feedback
      }
    });*/
    if(x)
    {
      alert("Update Successful!");
      localStorage.setItem("username",this.username);
    }
    else
    {
      alert("Update Unsuccessful");
    }

  }

}
function next(value: User): void {
  throw new Error('Function not implemented.');
}