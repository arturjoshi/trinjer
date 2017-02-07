import { LandingComponent } from './landing/landing.component';
/**
 * Created by xoll on 07.01.2017.
 */
import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AuthGuard} from "./services/auth-guard.service";

const appRoutes: Routes = [
    { path: 'core/', loadChildren: './app/core/core.module#CoreModule', canActivate: [AuthGuard]},

    { path: '', component: LandingComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule]
})
export class TrinjerRoutingModule{}