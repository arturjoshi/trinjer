import {Injectable} from "@angular/core";
import { Response} from "@angular/http";
import {UserDTO} from "../models/user.interface";
import {AuthenticateService} from "../login/authenticate.service";
import {HttpUtils} from "../services/http-utils.service";
import {Observable} from "rxjs/Rx";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class RegistrationService{
    private baseUrl: string = 'register';

    constructor(
        private authenticateService: AuthenticateService,
        private httpUtils: HttpUtils
    ){}

    registration(user: UserDTO): Observable<any>{
        return Observable.create(observer => {
            this.httpUtils.makePostWithoutToken(this.baseUrl, user)
                .map(RegistrationService.extractData)
                .catch(RegistrationService.handleError)
                .subscribe((response) => {
                    this.authenticateService.authenticate(user).subscribe((user: UserDTO) => {
                        observer.next(response);
                        observer.complete();
                    });
                });
        });
    }

    private static extractData(res: Response){
        return res.json();
    }

    private static handleError(error: any){
        console.error(error);
    }
}