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
import {AuthGuard} from "./services/auth-guard.service";

@NgModule({
    imports: [
        BrowserModule,
        TrinjerRoutingModule
    ],
    declarations: [
        AppComponent,
        LoginComponent,
        RegistrationComponent,
        DashboarComponent
    ],
    providers: [ AuthGuard],
    bootstrap: [AppComponent]
})
export class TrinjerModule{

}