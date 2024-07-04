import { Component, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NotificationService } from './notification-service.service';
import { Router } from '@angular/router';
import { UserService } from './user.service';
import { User } from './Interfaces/User';
import { Notification } from './Interfaces/Notification';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'Society for the Welfare of Endangered Nestlings';
  recentNotifcation: string = "";
  showPopup: boolean = false;
  notifications: Notification[] = [];
  newNotification: string | undefined;
  newTitle: string | undefined;

  notifs: boolean = false;

  constructor(private notifyService: NotificationService,
    private router: Router,
    private userService: UserService){}
  
    ngOnInit(): void {
      this.notifyService.getNotifications().subscribe(notifications => {
        this.notifications = notifications.reverse();
        this.recentNotifcation = this.notifications[0].content;
      });
  
      // Subscribe to notifications$ observable to get notified of changes
      this.notifyService.notifications$.subscribe(notifications => {
        this.notifications = notifications.reverse();
        this.recentNotifcation = this.notifications[0].content;
      });
    }

  redirectNotification(): void{
    if (UserService.fetchUser() == "admin"){
      alert("Admin clicked me");
    }
  }

  toggleNotificationPopup(): void {
    const popup = document.getElementById('notifyPop');
    if (popup) {
        popup.style.display = (popup.style.display === 'block') ? 'none' : 'block';
    }
  }

  closeNotificationPopup(): void {
    const popup = document.getElementById('notifyPop');
    if (popup) {
        popup.style.display = 'none';
    }
  }

  addNotification(): void {
    if (this.newNotification === undefined || this.newNotification!.trim() === ""){
      return;
    }

    if (this.newTitle === undefined || this.newTitle.trim() === ""){
      return;
    }

    this.notifyService.createEmitNotification(this.newTitle, this.newNotification);

    // this.notifyService.createNotifcation(this.newTitle, this.newNotification).subscribe(() => {
    //   // After successfully creating the notification, fetch the updated notifications
    //   this.notifyService.getNotifications().subscribe(notifications => {
    //     // Emit the updated notifications to notify subscribers
    //     this.notifyService.emitNotifications(notifications);
    //   });
      this.newNotification = "";
      this.newTitle = "";
    
  }

  isAdmin(): boolean{
    return UserService.fetchUser() == "admin";
  }

  loggedIn(): boolean{
    return UserService.fetchUser() == null;

  }

  toggleNotifs(): void {
    this.notifs = !this.notifs;
  }
}
