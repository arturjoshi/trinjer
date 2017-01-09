import {Injectable} from "@angular/core";
import {TokenService} from "../services/token.service";
import {Http, Response} from "@angular/http";
import {HttpUtils} from "../services/http-utils.service";
import {LoginUser} from "../models/login-user.model";
import "rxjs/Rx";
import {Observable} from "rxjs";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class AuthenticateService{
    constructor(
        private tokenService: TokenService,
        private httpUtils: HttpUtils
    ){}

    public authenticate(user: LoginUser): Observable<any>{
        let body = {
            username: user.username,
            password: user.password
        };

        return this.httpUtils.makePostWithoutToken("authenticate", JSON.stringify(body))
            .map(AuthenticateService.extractData)
            .catch(AuthenticateService.handleError);
    }

    private static extractData(res: Response){
        let body = res.json();
        console.log(body);
        return body;
    }

    private static handleError(error: any){
        console.log(error);
        return error;
    }
}