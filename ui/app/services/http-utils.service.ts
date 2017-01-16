import {Injectable, OnInit} from "@angular/core";
import {Http, RequestOptions, Headers, Response} from "@angular/http";
import {TokenService} from "./token.service";
import "rxjs/Rx";
import {Observable} from "rxjs/Rx";
import {UserDTO} from "../models/user.dto.interface";
import {IProject} from "../project/project.interface";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class HttpUtils implements OnInit{
    private baseUrl: string = "http://localhost:8080/api/";
    private optionsWithoutToken: RequestOptions;
    private options: RequestOptions;


    makePost(prefix: string, body: Object, params: Object = {}): Observable<any> {
        if(this.options === null)throw new Error("Options are not exist!");

        return this.http.post(this.baseUrl + prefix, body, this.getOptions(params));
    }


    makeGet(prefix: string, options: Object = {}):Observable<Response> {
        return this.http.get(this.baseUrl + prefix, this.getOptions(options));
    }


    makePostWithoutToken(prefix: string, body: Object, params: Object = {}): Observable<any>{
        return this.http.post(this.baseUrl + prefix, body, this.getOptions(params));
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


    private getOptions(options: Object): RequestOptions{
        let result = new RequestOptions();
        Object.assign(result, this.options, options);

        return result;
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