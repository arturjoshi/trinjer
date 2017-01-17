/**
 * Created by xoll on 06.01.2017.
 */
import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app.component";
import {TrinjerRoutingModule} from "./trinjer-routing.module";
import {LoginComponent} from "./login/login.component";
import {RegistrationComponent} from "./registration/registration.component";
import {DashboarComponent} from "./dashboard/dashboard.component";
import {FormsModule} from "@angular/forms"
import {AuthGuard} from "./services/auth-guard.service";
import {EqualValidatorDirective} from "./registration/equal-validator.directive";
import {TokenService} from "./services/token.service";
import {HttpUtils} from "./services/http-utils.service";
import {HttpModule} from "@angular/http";
import {AccountService} from "./services/account.service";
import {ProjectsComponent} from "./project/projects.component";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        TrinjerRoutingModule
    ],
    declarations: [
        AppComponent,
        LoginComponent,
        RegistrationComponent,
        DashboarComponent,
        ProjectsComponent,
        EqualValidatorDirective
    ],
    providers: [
        AuthGuard,
        TokenService,
        AccountService,
        HttpUtils],
    bootstrap: [AppComponent]
})
export class TrinjerModule{

}