import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from './local-storage.service';
import { Notification } from './Interfaces/Notification';
import { Observable, Subject, catchError, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationsSubject: Subject<Notification[]> = new Subject<Notification[]>();
  public notifications$: Observable<Notification[]> = this.notificationsSubject.asObservable();

  private userUrl = 'http://localhost:8080/notify/';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient, private localStorage: LocalStorageService)
    { }


  createEmitNotification(title: string, content: string): void{
    this.createNotifcation(title, content).subscribe(() => {
        this.getNotifications().subscribe(notifications => {
          this.emitNotifications(notifications);
        });
      });
    }
  
  /** GET notifications from the server */
  getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.userUrl)
      .pipe(
        //tap(_ => this.log('fetched heroes')),
        catchError(this.handleError<Notification[]>('getNotifications', []))
      );
  }

  createNotifcation(title: string, content: string): Observable<Notification>{
    return this.http.post<Notification>(this.userUrl, {
      "title": title,
      "content": content
    }, this.httpOptions).pipe(
      catchError(this.handleError<Notification>("createNotification"))
    )
  }

    // Method to emit notifications whenever there's a change
    emitNotifications(notifications: Notification[]): void {
      this.notificationsSubject.next(notifications);
    }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      
      //this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
