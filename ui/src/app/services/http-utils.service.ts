import {Injectable, OnInit} from "@angular/core";
import {Http, RequestOptions, Headers, Response} from "@angular/http";
import {TokenService} from "./token.service";
import "rxjs/Rx";
import {Observable} from "rxjs/Rx";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class HttpUtils{
    private baseUrl: string = "http://localhost:8080/api/";
    private optionsWithoutToken: RequestOptions;
    private options: RequestOptions = null;


    makePost(prefix: string, body: Object, params: Object = {}): Observable<any> {
        if(this.options === null)throw new Error("Options are not exist!");

        return this.http.post(this.baseUrl + prefix, body, HttpUtils.appendOptions(params, this.options));
    }


    makeGet(prefix: string, options: Object = {}):Observable<Response> {
        return this.http.get(this.baseUrl + prefix, this.options);
    }


    makePostWithoutToken(prefix: string, body: Object, params: Object = {}): Observable<any>{
        return this.http.post(this.baseUrl + prefix, body, HttpUtils.appendOptions(params, this.optionsWithoutToken));
    }


    private static appendOptions(options: Object, defaultOptions: RequestOptions): RequestOptions{
        let result = new RequestOptions();
        Object.assign(result, defaultOptions, options);

        //noinspection TypeScriptValidateTypes
        return result;
    }


    constructor(
        private http: Http,
        private tokenService: TokenService
    ){
        let defaultHeaders: Headers = new Headers({
            'Content-Type': 'application/json'
        });
        this.optionsWithoutToken = new RequestOptions({headers: defaultHeaders});
        this.options = null;

        this.tokenService.token.subscribe((token: string) => {
            this.initializeTokenOptions(token);
        });
    }

    initializeTokenOptions(token: string){
        let headers = new Headers();

        headers.append('x-auth-token', token);

        this.options = new RequestOptions({
            headers: headers
        });
    }
}