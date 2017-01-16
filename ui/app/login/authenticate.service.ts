import {Injectable} from "@angular/core";
import {TokenService} from "../services/token.service";
import {HttpUtils} from "../services/http-utils.service";
import "rxjs/Rx";
import {UserDTO} from "../models/user.interface";
import {Observable, Observer} from "rxjs/Rx";
import {AccountService} from "../services/account.service";
import {IAccount} from "../models/account.interface";
import {Response} from "@angular/http";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class AuthenticateService{
    private baseUrl: string = 'authenticate/';

    constructor(
        private tokenService: TokenService,
        private accountService: AccountService,
        private httpUtils: HttpUtils
    ){}

    public authenticate(user: UserDTO): Observable<IAccount>{
        return Observable.create((observer: Observer<IAccount>) => {
            this.httpUtils.makePostWithoutToken(this.baseUrl, user)
                .catch(AuthenticateService.handleError)
                .subscribe((response: Response) => {
                    let token = response['token'] || null;
                    let account = response['account'] || null;

                    this.tokenService.saveToken(token);
                    this.accountService.saveAccount(account);

                    observer.next(account);
                    observer.complete();
                });
        });
    }

    private static handleError(error: any): any{
        console.error(error);
        return error;
    }
}