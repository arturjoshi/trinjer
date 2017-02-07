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
    template: `<router-outlet></router-outlet>`
})
export class AppComponent{}