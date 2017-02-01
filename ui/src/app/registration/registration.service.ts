import {Injectable} from "@angular/core";
import {Response} from "@angular/http";
import {UserDTO} from "../models/user.dto.interface";
import {AuthenticateService} from "../login/authenticate.service";
import {HttpUtils} from "../services/http-utils.service";
import {Observable, Observer} from "rxjs/Rx";
import {AccountDTO} from "../models/account";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class RegistrationService{
    private baseUrl: string = 'register/';

    constructor(
        private authenticateService: AuthenticateService,
        private httpUtils: HttpUtils
    ){}

    registration(user: UserDTO): Observable<AccountDTO>{
        return Observable.create((observer: Observer<AccountDTO>) => {
            //noinspection TypeScriptUnresolvedFunction
            this.httpUtils.makePostWithoutToken(this.baseUrl, user)
                .map(RegistrationService.extractData)
                .subscribe((response: Response) => {
                    this.authenticateService.authenticate(user).subscribe((user: AccountDTO) => {
                        observer.next(user);
                        observer.complete();
                    });
                }, (error: any) => {
                    observer.error(error._body);
                    observer.complete();
                });
        });
    }

    private static extractData(res: Response){
        return res.json();
    }

    private static handleError(error: any): any{
        console.error(error);
        return error;
    }
}