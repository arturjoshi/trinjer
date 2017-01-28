import {Component} from "@angular/core";
import {AuthenticateService} from "./authenticate.service";
import {LoginUser} from "../models/login-user.model";
import {AccountService} from "../services/account.service";
import {MdDialogRef} from "@angular/material";
/**
 * Created by xoll on 08.01.2017.
 */

@Component({
    selector: 'login',
    templateUrl: 'app/login/login.template.html',
    providers: [
        AuthenticateService,
        AccountService
    ]
})
export class LoginDialog{
    user: LoginUser = LoginUser.getNewLoginUser();

    constructor(
        private dialogRef: MdDialogRef<LoginDialog>,
        private authenticateService: AuthenticateService
    ){}

    login(){
        this.authenticateService.authenticate(this.user)
            .subscribe(
                () => {},
                (error: any): void => {
                    console.log("Authenticate error!");
                });
    }

    close(){
        this.dialogRef.close('Yes');
    }
}