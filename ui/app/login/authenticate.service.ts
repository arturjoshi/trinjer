import {Injectable} from "@angular/core";
import {TokenService} from "../services/token.service";
import {Http, Response} from "@angular/http";
import {HttpUtils} from "../services/http-utils.service";
import {LoginUser} from "../models/login-user.model";
import "rxjs/Rx";
import {Observable} from "rxjs";
import {Router} from "@angular/router";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class AuthenticateService{
    constructor(
        private tokenService: TokenService,
        private router: Router,
        private httpUtils: HttpUtils
    ){}

    public authenticate(user: LoginUser): Observable<any>{
        let body = {
            username: user.username,
            password: user.password,
            email: ""
        };

        let request = this.httpUtils.makePostWithoutToken("authenticate", body)
            .map(AuthenticateService.extractData)
            .catch(AuthenticateService.handleError);

        //TODO: Go to success auth handler
        request.subscribe((token) => {
            this.tokenService.saveToken(token);
            this.router.navigate(['Dashboard'])
        });

        return request;
    }

    private static extractData(res: Response){
        return res;
    }

    private static handleError(error: any){
        console.log(error);
        return error;
    }
}