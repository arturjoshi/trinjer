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
import {UserService} from "./services/user.service";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        TrinjerRoutingModule
    ],
    declarations: [
        AppComponent,
        LoginComponent,
        RegistrationComponent,
        DashboarComponent,
        EqualValidatorDirective
    ],
    providers: [ AuthGuard, UserService],
    bootstrap: [AppComponent]
})
export class TrinjerModule{

}