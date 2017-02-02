import {Injectable} from "@angular/core";
import {TokenService} from "../services/token.service";
import {HttpUtils} from "../services/http-utils.service";
import "rxjs/Rx";
import {UserDTO} from "../models/user.dto.interface";
import {Observable, Observer} from "rxjs/Rx";
import {AccountService} from "../services/account.service";
import {Response} from "@angular/http";
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

                    this.accountService.saveAccount(account);
                    this.tokenService.setToken(token);

                    observer.next(account);
                    observer.complete();
                }, (error: any) => {
                    observer.error(error._body);
                    observer.complete();
                });
        });
    }
}