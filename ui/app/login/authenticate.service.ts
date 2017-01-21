import {Injectable} from "@angular/core";
import {TokenService} from "../services/token.service";
import {HttpUtils} from "../services/http-utils.service";
import "rxjs/Rx";
import {UserDTO} from "../models/user.dto.interface";
import {Observable, Observer} from "rxjs/Rx";
import {AccountService} from "../services/account.service";
import {IAccount} from "../models/account.interface";
import {Response} from "@angular/http";
import {AccountTokenDTO} from "../models/account-token.dto";
import {AccountDTO} from "../models/account";
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

    public authenticate(user: UserDTO): Observable<AccountDTO>{
        return Observable.create((observer: Observer<AccountDTO>) => {
            this.httpUtils.makePostWithoutToken(this.baseUrl, user)
                .subscribe((response: Response) => {
                    let json = response.json();

                    let account = AccountDTO.getFromJson(json['account']);
                    let token = json['token'];

                    console.log(account);

                    this.accountService.saveAccount(account);
                    this.tokenService.saveToken(token);

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