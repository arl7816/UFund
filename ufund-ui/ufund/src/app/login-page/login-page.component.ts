import { Component } from '@angular/core';
import { User } from '../Interfaces/User';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { catchError, of, throwError } from 'rxjs';
import { LocalStorageService } from '../local-storage.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [FormsModule,HttpClientModule,RouterModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  url = "http://localhost:8080/users/login"
  login: User;
  
  constructor(private http: HttpClient, private router: Router, 
    private storageService: LocalStorageService,
    private userService: UserService) {
    this.login = new User();
  }

  onLoginPress() {
    //debugger;
    this.http.post<HttpResponse<any>>(this.url, this.login).pipe(catchError(this.handleError)).subscribe((res)=>{
      console.log(res);
      if(res){
        this.userService.storeUser(this.login.username); 
        alert("login succesful!");
        console.log("login successful");
        if(this.login.username=='admin')
        {
          this.router.navigateByUrl("/cupboard");
        }
        else
        {
          this.router.navigateByUrl("/helper-cupboard");
        }
      }
    })
  }

  onPressSignup() {
    this.router.navigateByUrl("signup");
}


  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else if (error.status == 409){
      alert("wrong password!");
    } else if (error.status == 500){
      alert("user does not exist");
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }
}