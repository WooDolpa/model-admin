import {Inject, Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import { API_URL_TOKEN } from 'app/app.token';
import {catchError} from "rxjs/operators";
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import {CookieService} from "ngx-cookie-service";

@Injectable()
export class ApiGatewayService {

    constructor(private _http : HttpClient,
                private cookieService : CookieService,
                @Inject(API_URL_TOKEN) private apiUrl : string) { }


    get(url:string){

        let httpHeaders = new HttpHeaders().set('Authorization', this.getTokenKey());

        if(url !== ''){
            this.setCookies();
        }

        return this._http.get<Response>(`${this.apiUrl}/${url}`, {headers: httpHeaders})
            .delay(100)
            .pipe(
                catchError(this.handleError())
            )
    }

    post(url: string, data: any){

        let httpHeaders = new HttpHeaders({
            'Authorization' : this.getTokenKey(),
            'Content-type' : 'application/json'
        });

        if(url !== 'auth/login'){
            this.setCookies();
        }
        // console.log(`url : ${this.apiUrl}/${url} // data : ${data} // httpHeaders : ${httpHeaders}`);

        return this._http.post(`${this.apiUrl}/${url}`,data,{headers : httpHeaders})
            .delay(100)
            .pipe(
                catchError(this.handleError())
            )
    }

    imgPost(url : string, data : any){

        let httpHeaders = new HttpHeaders({
            'Authorization' : this.getTokenKey(),
            'enctype' : 'multipart/form-data'
        });

        if(url !== 'auth/login'){
            this.setCookies();
        }

        return this._http.post(`${this.apiUrl}/${url}`,data,{headers : httpHeaders})
            .delay(100)
            .pipe(
                catchError(this.handleError())
            )

    }

    /** 로그아웃 처리 */
    delete(url: string){

        let httpHeaders = new HttpHeaders().set('Authorization', this.getTokenKey());

        return this._http.delete(`${this.apiUrl}/${url}`, {headers: httpHeaders})
            .delay(100)
            .pipe(
                catchError(this.handleError())
            )
    }

    /** 쿠키값 시간 초기화*/
    setCookies(){
        let expireTime = 60 / (24*60);
        this.cookieService.set('token',this.getTokenKey(),expireTime);
    }

    getTokenKey() {
        let token = this.cookieService.get("token");
        return token;
    }

    /** Handle Http operation that failed.
     * Let the app continue.
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     *  */
    private handleError<T> (operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            // send the error to remote logging infrastructure
            // console.error(error);

            if(error instanceof HttpErrorResponse) {
                // Server or connection error happened
                if(!navigator.onLine) {
                    alert('No Internet connection');
                    // No Internet connection
                } else {
                    // Handle Http Error (error.status === 403, 404...)
                }
            } else {
                // Handle Client Error (angular Error, ReferenceError...)
            }

            // better job of transforming error for user consumption
            // this.log(`${operation} failed: ${error.message}`);

            // Let the app keep running by returning an empty result.
            //console.log(error.error);
            return of(error.error as T);
        };
    }


}
