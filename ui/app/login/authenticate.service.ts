import {Injectable} from "@angular/core";
import {TokenService} from "../services/token.service";
import {HttpUtils} from "../services/http-utils.service";
import "rxjs/Rx";
import {UserDTO} from "../models/user.dto.interface";
import {Observable} from "rxjs/Rx";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class AuthenticateService{
    private baseUrl: string = 'authenticate/';

    constructor(
        private tokenService: TokenService,
        private httpUtils: HttpUtils
    ){}

    public authenticate(user: UserDTO): Observable<any>{
        return Observable.create((observer: any) => {
            this.httpUtils.makePostWithoutToken(this.baseUrl, user)
                .catch(AuthenticateService.handleError)
                .subscribe((token: string) => {
                    this.tokenService.saveToken(token);
                    observer.next(user);
                    observer.complete();
                });
        });
    }

    private static handleError(error: any): any{
        console.error(error);
        return error;
    }
}