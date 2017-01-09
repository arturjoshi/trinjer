import {Injectable, OnInit} from "@angular/core";
import {Http, RequestOptions, Headers} from "@angular/http";
import {TokenService} from "./token.service";
import "rxjs/Rx";
import {Observable} from "rxjs/Rx";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class HttpUtils implements OnInit{
    private baseUrl: string = "http://localhost:8080/api/";
    private optionsWithoutToken: RequestOptions;
    private options: RequestOptions;

    makePostWithoutToken(prefix: string, body: string, params: Object = {}): Observable<any>{
        return this.http.post(this.baseUrl + prefix, body)
            .map(response => {
                console.log(response);
                return response;
            })
            .catch(error => {
                console.log(error);
                return error;
            });
    }

    ngOnInit(): void {
        let defaultHeaders: Headers = new Headers({'Content-Type': ' x-www-url-encoded'});
        this.optionsWithoutToken = new RequestOptions({headers: defaultHeaders});

        if(this.tokenService.isTokenPresent()){
            this.initializeTokenOptions();
        }else{
            //TODO: Subscribe on token
        }
    }

    private initializeTokenOptions(){
        let tokenHeaders: Headers = new Headers({
            'Content-Type': 'application/json',
            'X-Auth-Token': this.tokenService.getToken()
        });
        this.options = new RequestOptions({
            headers: tokenHeaders
        });
    }

    constructor(
        private http: Http,
        private tokenService: TokenService
    ){}
}