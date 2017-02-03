import { Router } from '@angular/router';
/**
 * Created by xoll on 07.01.2017.
 */
import {Component} from "@angular/core";
import {AuthGuard} from "./services/auth-guard.service";
import {MdDialog, MdDialogRef} from "@angular/material";
import {LoginDialog} from "./login/login.dialog";
import {RegistrationDialog} from "./registration/registration.component";

@Component({
    selector: 'app',
    templateUrl: 'app/app.template.html',
    styleUrls: ['app/app.css'],
    providers: [AuthGuard]
})
export class AppComponent{
    //noinspection JSUnusedGlobalSymbols
    title = "Trinjer";
    private loginDialog: MdDialogRef<LoginDialog>;
    private registrationDialog: MdDialogRef<RegistrationDialog>;

    constructor(private authGuard: AuthGuard, 
            private dialog: MdDialog, 
            private router: Router){}

    isAuth(){
        return this.authGuard.isAuthenticated();
    }

    logout(){
        this.authGuard.logout();
        this.router.navigate(['/']);
    }

    openLoginDialog(){
        this.loginDialog = this.dialog.open(LoginDialog);
        this.loginDialog.afterClosed().subscribe(() => {this.loginDialog = null;});
    }

    openRegistrationDialog(){
        this.registrationDialog = this.dialog.open(RegistrationDialog);
        this.registrationDialog.afterClosed().subscribe(() => {
            this.registrationDialog = null;
        });
    }
}