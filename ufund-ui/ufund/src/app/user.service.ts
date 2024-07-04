import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';

import { Observable, Subject, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Need } from './Interfaces/Need';
import { User } from './Interfaces/User';
import { FundingBasket } from './Interfaces/FundingBasket';
import { LocalStorageService } from './local-storage.service';


@Injectable({ providedIn: 'root' })
export class UserService {

  private static storedUser: string ='';
  private userUrl = 'http://localhost:8080/users';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient, private localStorage: LocalStorageService)
    { }


  /** GET heroes from the server */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.userUrl)
      .pipe(
        //tap(_ => this.log('fetched heroes')),
        catchError(this.handleError<User[]>('getUsers', []))
      );
  }

  createUser(username: string, password:string): Observable<User>
  {
    return this.http.post<User>(this.userUrl,
      {
        "username": username,
        "password": password
      },this.httpOptions).pipe(catchError(this.handleUserError));
  }

  getUser(username: String): Observable<User>{
    return this.http.get<User>(this.userUrl + "/user/" + username)
      .pipe(
        catchError(this.handleError<User>('getUser', undefined))
      );
  }

  updateUser(user: User): Observable<any>{
    console.log("I def rec " + user.username);
    return this.http.put<any>(this.userUrl, {
      "id": user.id,
      "password": user.password, 
      "username": user.username,
      "FundingBasket": user.basket,
      "isAdmin": user.isAdmin,
      "wallet": user.wallet
    }, this.httpOptions)
    .pipe(
      catchError(this.handleError('updateUser'))
    );
  }

  /**
   * Get the funding basket of needs
   * @param user 
   * @returns list of needs
   */
  getBasket(username: string): Observable<Need[]>{
    return this.http.get<Need[]>(this.userUrl+"/getBasket/"+username
    ,this.httpOptions)
    .pipe(catchError(this.handleError<Need[]>('getBasket')));
  }

  removeFromBasket(username: string, need: Need ): Observable<boolean>{
    return this.http.put<boolean>(this.userUrl+"/removeFromBasket/"+username,
    {
      "id": need.id, 
      "name": need.name,
      "cost": + need.cost,
      "type": need.type,
      "quantity": +need.quantity
    },this.httpOptions).pipe(
      catchError(this.handleError<boolean>('removeFromBasket')));


  }

  /**
   * add a need to a funding basket
   * 

   * @param user 
   * @param need 
   * @returns 
   */
  addToBasket(username: string, need: Need): Observable<boolean>
  {
    return this.http.post<boolean>(this.userUrl+"/addToBasket/"+username,
    {
      "id": need.id, 
      "name": need.name,
      "cost": + need.cost,
      "type": need.type,
      "quantity": +need.quantity
    },this.httpOptions).pipe(catchError(this.handleError<boolean>('addToBasket')));

  }

  /**
  * saves username to local storage
  * 
  * @param username 
  */
  storeUser(username: string): void{
    this.localStorage.setItem("username", username);
  }

  /**
  * reads username from local storage
  * 
  * @returns returns the username from local storage
  */
  static fetchUser(): string{
    if (localStorage.getItem("username")==null){
      return '';
    } else {
    return String(localStorage.getItem("username"));
  }
  }

  /**
  * removes username from local storage
  * 
  */
  removeUser(): void {
    this.localStorage.removeItem("username");
  }

  deleteUser(userId: number): Observable<User>{
    return this.http.delete<User>(this.userUrl + "/" + userId)
    .pipe(
      catchError(this.handleError<User>('deleteUser', undefined))
    );
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

  private handleUserError(error:HttpErrorResponse){
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else if (error.status == 409){
      alert("A user with the same name already exists, please pick another username.");
    } else if (error.status == 500){
      alert("An error occured, please try again later.");
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