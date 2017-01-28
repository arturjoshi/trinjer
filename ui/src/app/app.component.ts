/**
 * Created by xoll on 07.01.2017.
 */
import {Component} from "@angular/core";
import {AuthGuard} from "./services/auth-guard.service";
import {MdDialog, MdDialogRef} from "@angular/material";
import {LoginDialog} from "./login/login.dialog";

//TODO: Connect material or bootstrap to project
@Component({
    selector: 'app',
    templateUrl: 'app/app.template.html',
    styleUrls: ['app/app.css'],
    providers: [AuthGuard]
})
export class AppComponent{
    //noinspection JSUnusedGlobalSymbols
    title = "Trinjer";
    private dialogRef: MdDialogRef<LoginDialog>;

    constructor(private authGuard: AuthGuard, private dialog: MdDialog){}

    isAuth(){
        return this.authGuard.isAuthenticated();
    }

    logout(){
        this.authGuard.logout();
    }

    openLoginDialog(){
        this.dialogRef = this.dialog.open(LoginDialog);

        this.dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.dialogRef = null;
        })
    }
}