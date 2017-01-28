/**
 * Created by xoll on 06.01.2017.
 */
import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app.component";
import {TrinjerRoutingModule} from "./trinjer-routing.module";
import {LoginDialog} from "./login/login.dialog";
import {RegistrationComponent} from "./registration/registration.component";
import {DashboarComponent} from "./dashboard/dashboard.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms"
import {AuthGuard} from "./services/auth-guard.service";
import {EqualValidatorDirective} from "./registration/equal-validator.directive";
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
        TrinjerRoutingModule
    ],
    declarations: [
        AppComponent,
        LoginDialog,
        RegistrationComponent,
        DashboarComponent,
        EqualValidatorDirective
    ],
    providers: [
        AuthGuard,
        TokenService,
        AccountService,
        HttpUtils],
    entryComponents: [
        LoginDialog
    ],
    bootstrap: [AppComponent]
})
export class TrinjerModule{

}