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
    styleUrls: ['app/login/login.css'],
    providers: [
        AuthenticateService,
        AccountService
    ]
})
export class LoginDialog{
    user: LoginUser = LoginUser.getNewLoginUser();
    isLoginProcessed: boolean = false;

    constructor(
        private dialogRef: MdDialogRef<LoginDialog>,
        private authenticateService: AuthenticateService
    ){}

    login(){
        this.isLoginProcessed = true;
        this.authenticateService.authenticate(this.user)
            .subscribe(
                () => {
                    this.isLoginProcessed = false;
                    this.close();
                },
                (error: any): void => {
                    this.isLoginProcessed = false;
                    console.log("Authenticate error!" + error);
                });
    }

    close(){
        this.dialogRef.close('Cancel');
    }
}