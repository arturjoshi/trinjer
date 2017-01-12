import {Injectable} from "@angular/core";
import {TokenService} from "../services/token.service";
import {HttpUtils} from "../services/http-utils.service";
import "rxjs/Rx";
import {Observable} from "rxjs";
import {UserDTO} from "../models/user.interface";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class AuthenticateService{
    constructor(
        private tokenService: TokenService,
        private httpUtils: HttpUtils
    ){}

    public authenticate(user: UserDTO): Observable<any>{
        let request = this.httpUtils.makePostWithoutToken("authenticate", user)
            .catch(AuthenticateService.handleError);

        request.subscribe((token) => {this.tokenService.saveToken(token)});

        return request;
    }

    private static handleError(error: any){
        console.error(error);
    }
}