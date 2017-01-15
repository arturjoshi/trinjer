/**
 * Created by xoll on 07.01.2017.
 */
import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./login/login.component";
import {RegistrationComponent} from "./registration/registration.component";
import {DashboarComponent} from "./dashboard/dashboard.component";
import {AuthGuard} from "./services/auth-guard.service";
import {AppComponent} from "./app.component";

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent},
    { path: 'registration', component: RegistrationComponent},
    //TODO: Move routing component to another component
    // { path: '', component: AppComponent, name: 'Home'},

    { path: 'dashboard', component: DashboarComponent, canActivate: [AuthGuard]}
];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule]
})
export class TrinjerRoutingModule{}