import { AuthGuard } from './../services/auth-guard.service';
import { RegistrationDialog } from './../registration/registration.component';
import { MdDialog } from '@angular/material';
import { MdDialogRef } from '@angular/material';
import { LoginDialog } from './../login/login.dialog';
import { Component } from '@angular/core';

@Component({
    selector: 'landing',
    moduleId: module.id,
    templateUrl: 'landing.template.html',
    styleUrls: ['landing.css']
})
export class LandingComponent{
    private loginDialog: MdDialogRef<LoginDialog>;
    private registrationDialog: MdDialogRef<RegistrationDialog>;

    constructor(private dialog: MdDialog, private authGuard: AuthGuard){}

    isAuthentificate(){
        return this.authGuard.isAuthenticated();
    }

    logout(){
        this.authGuard.logout();
    }

    openLoginDialog(){
        this.loginDialog = this.dialog.open(LoginDialog);
        this.loginDialog.afterClosed().subscribe((result) => {
            this.loginDialog = null;
            console.log(result);
        });
    }

    openRegistrationDialog(){
        this.registrationDialog = this.dialog.open(RegistrationDialog);
        this.registrationDialog.afterClosed().subscribe((result) => {
            this.registrationDialog = null;
            console.log(result);
        });
    }
}