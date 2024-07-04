import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs'; 
import { catchError, map, tap } from 'rxjs/operators';

import { Need } from './Interfaces/Need';


@Injectable({ providedIn: 'root' })
export class NeedService {

  private selectedNeed: number = -1; //-1 implies nothing is selected
  private needsUrl = 'http://localhost:8080/ufund';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient)
    { }

  /** GET heroes from the server */
  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl)
      .pipe(
        //tap(_ => this.log('fetched heroes')),
        catchError(this.handleError<Need[]>('getNeeds', []))
      );
  }

  getNeed(id: number): Observable<Need>{
    return this.http.get<Need>(this.needsUrl + "/" + id)
      .pipe(
        catchError(this.handleError<Need>('getNeed', undefined))
      );
  }

  updateNeed(need: Need): Observable<any>{
    return this.http.put(this.needsUrl, {
      "id": need.id, 
      "name": need.name,
      "cost": + need.cost,
      "type": need.type,
      "quantity": +need.quantity,
      "surplus": + need.surplus,
    }, this.httpOptions)
    .pipe(catchError(this.handleError('updateNeed')));
  }

  checkout(basketItems: Need[]): Observable<any> {
    return this.http.post<any>(`${this.needsUrl}/checkout`, basketItems, this.httpOptions)
      .pipe(
        catchError(this.handleError<any>('checkout', [])) 
      );
  }

  
  createNeed(name: string, quantity: number): Observable<Need>{
    return this.http.post<Need>(this.needsUrl, {
      "name": name,
      "quantity": quantity
    }, this.httpOptions).pipe(
      catchError(this.handleError<Need>('createNeed', undefined))
    );;
  }

  deleteNeed(id: number): Observable<Need>{ 
    return this.http.delete<Need>(this.needsUrl + "/" + id)
      .pipe(
        catchError(this.handleError<Need>('deleteNeed', undefined))
      );
  }

  searchNeeds(search: string): Observable<Need[]>{
    return this.http.get<Need[]>(this.needsUrl + "/?name=" + search)
    .pipe(
      catchError(this.handleError<Need[]>('deleteNeed', undefined))
    );
  }


  // for transferring between components
  rememberNeed(id: number): void{
    this.selectedNeed = id;
  }

  // completes transfer and resets selected
  // make sure recievers handle -1 as a special case
  transferNeed(): number{
    let id = this.selectedNeed;
    this.selectedNeed = -1;
    return id;
    
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