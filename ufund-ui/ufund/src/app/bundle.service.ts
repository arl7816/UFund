import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { Bundle } from './Interfaces/Bundle';
import { Need } from './Interfaces/Need';

@Injectable({
  providedIn: 'root'
})
export class BundleService {
  private selectedNeed: number = -1; //-1 implies nothing is selected
  private bundleUrl = 'http://localhost:8080/bundles';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient)
    { }

  /** GET heroes from the server */
  getBundles(): Observable<Bundle[]> {
    return this.http.get<Bundle[]>(this.bundleUrl)
      .pipe(
        //tap(_ => this.log('fetched heroes')),
        catchError(this.handleError<Bundle[]>('getBundles', []))
      );
  }

  getBundle(id: number): Observable<Bundle>{
    return this.http.get<Bundle>(this.bundleUrl + "/" + id)
      .pipe(
        catchError(this.handleError<Bundle>('getBundle', undefined))
      );
  }

  updateBundle(bundle: Bundle): Observable<any>{
    return this.http.put(this.bundleUrl, {
      "id": bundle.id, 
      "name": bundle.name,
      "needs": bundle.list,
    }, this.httpOptions)
    .pipe(catchError(this.handleError('updateBundle')));
  }

  
  createBundle(name: string, list: Array<Need>): Observable<Bundle>{
    return this.http.post<Bundle>(this.bundleUrl, {
      "name": name,
      "needs": list, 
    }, this.httpOptions).pipe(
      catchError(this.handleError<Bundle>('createBundle', undefined))
    );;
  }

  deleteBundle(id: number): Observable<Bundle>{
    return this.http.delete<Bundle>(this.bundleUrl + "/" + id)
      .pipe(
        catchError(this.handleError<Bundle>('deleteBundle', undefined))
      );
  }

  searchBundles(search: string): Observable<Bundle[]>{
    return this.http.get<Bundle[]>(this.bundleUrl + "/?name=" + search)
    .pipe(
      catchError(this.handleError<Bundle[]>('deleteBundle', undefined))
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
}
