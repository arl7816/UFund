import { Component } from '@angular/core';
import { UserService } from '../user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HOMEComponent {

  constructor(private userservice: UserService) {}


  LoggedIn(): boolean{
    return UserService.fetchUser() == "";
  }

}
