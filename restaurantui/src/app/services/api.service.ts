import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http'
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(public http: HttpClient) { }

  baseUrl = "http://localhost:8080/restaurant/"

  testMethod() {
    alert("working")
  }

  startSession(data: any) {
    // call start session api
    const params = new HttpParams().set('username', data);
    let startSessionUrl =  this.baseUrl + 'startSession'
    let response = this.http.post(startSessionUrl, {},{
      params: params,
      responseType: 'text'
    })
    return response
  }

  joinSession(data: any) { // model
    let joinSessionUrl = this.baseUrl + 'joinSession'
    const params = new HttpParams().set('sessionId', data);
    let response = this.http.post(joinSessionUrl, {}, {
      params : params,
      responseType: 'text'
    }).pipe(
      catchError(this.handleError)
    );
    return response
  }

  endSession(sessionId: string, hostname: string) {
    // call start session api
    let startSessionUrl =  this.baseUrl + 'endSession'
    const params = new HttpParams().set('sessionId', sessionId).set('username', hostname);
    let response = this.http.post(startSessionUrl, {},{
      params : params,
      responseType: 'text'
    }).pipe(
      catchError(this.handleError)
    );
    return response
  }

  submitRestaurant(sessionId: string, restaurantname: string) {
    const params = new HttpParams().set('sessionId', sessionId).set('restaurantName', restaurantname);
    let addRestaurantUrl = this.baseUrl + 'submitRestaurant'
    let response = this.http.post(addRestaurantUrl, {}, {
      params: params,
      responseType: 'text'
    })
    return response
  }

  getAllRestaurant(sessionId: string){
    const params = new HttpParams().set('sessionId', sessionId)
    let getRestaurantUrl = this.baseUrl + 'getRestaurants'
    let response = this.http.get(getRestaurantUrl, {
      params: params,
      responseType: 'text'
    })
    return response
  }

  getSelectedRestaurant(sessionId: string){
    const params = new HttpParams().set('sessionId', sessionId)
    let getRestaurantUrl = this.baseUrl + 'getSelectedRestaurant'
    let response = this.http.get(getRestaurantUrl, {
      params: params,
      responseType: 'text'
    })
    return response
  }

  private handleError(error: HttpErrorResponse) {
    // You can customize the error handling logic here
    console.error(error.message);
    return throwError(() => new Error(error.message)); // Rethrow the error or return a custom error message
  }

}
