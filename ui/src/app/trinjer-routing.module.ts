import { LandingComponent } from './landing/landing.component';
/**
 * Created by xoll on 07.01.2017.
 */
import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {RegistrationDialog} from "./registration/registration.component";
import {AuthGuard} from "./services/auth-guard.service";

const appRoutes: Routes = [
    { path: 'projects/', loadChildren: './app/core/projects/projects.module#ProjectModule'},

    { path: '', component: LandingComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule]
})
export class TrinjerRoutingModule{}