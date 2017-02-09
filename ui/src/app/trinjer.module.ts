import { CabinetModule } from './cabinet/cabinet.module';
import { LandingComponent } from './landing/landing.component';
/**
 * Created by xoll on 06.01.2017.
 */
import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app.component";
import {TrinjerRoutingModule} from "./trinjer-routing.module";
import {LoginDialog} from "./login/login.dialog";
import {RegistrationDialog} from "./registration/registration.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms"
import {AuthGuard} from "./services/auth-guard.service";
import {TokenService} from "./services/token.service";
import {HttpUtils} from "./services/http-utils.service";
import {HttpModule} from "@angular/http";
import {AccountService} from "./services/account.service";
import {MaterialModule} from "@angular/material";
import 'node_modules/hammerjs/hammer.min.js';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        MaterialModule.forRoot(),
        HttpModule,
        CabinetModule,
        TrinjerRoutingModule
    ],
    declarations: [
        AppComponent,
        LoginDialog,
        LandingComponent,
        RegistrationDialog
    ],
    providers: [
        AuthGuard,
        TokenService,
        AccountService,
        HttpUtils],
    entryComponents: [
        LoginDialog,
        RegistrationDialog
    ],
    bootstrap: [AppComponent]
})
export class TrinjerModule{}