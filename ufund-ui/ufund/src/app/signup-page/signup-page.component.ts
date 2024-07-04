import { Component } from '@angular/core';
import { User } from '../Interfaces/User';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, of, throwError } from 'rxjs';
import { LocalStorageService } from '../local-storage.service';
import { UserService } from '../user.service';


@Component({
  selector: 'app-signup-page',
  standalone: false,
  templateUrl: './signup-page.component.html',
  styleUrl: './signup-page.component.css'
})
export class SignupPageComponent {
  signup: User;
  constructor(private userService: UserService, private router: Router) {
    this.signup = new User();
  }

  onSignUpPress() {
    debugger;
    if (this.signup.username.trim() == ""){
      debugger;
      alert("Please enter a Username");
    } else if (this.signup.password.trim() == ""){
      debugger;
      alert("Please enter a password");
    } else {
      this.userService.createUser(this.signup.username, this.signup.password).subscribe((res)=>{
        console.log(res);
        alert("created user " + this.signup.username + " successfully, you can now login as " + this.signup.username);
        this.router.navigateByUrl("/login");
      });
    }
  }
}
