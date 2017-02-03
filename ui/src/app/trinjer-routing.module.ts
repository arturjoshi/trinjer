import { ProjectsListComponent } from './projects/projects-list/projects-list.component';
/**
 * Created by xoll on 07.01.2017.
 */
import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {RegistrationDialog} from "./registration/registration.component";
import {DashboarComponent} from "./dashboard/dashboard.component";
import {AuthGuard} from "./services/auth-guard.service";

//TODO: Move routing component to another component
const appRoutes: Routes = [
    { path: 'registration', component: RegistrationDialog},
    { path: 'projects', component: ProjectsListComponent},

    { path: 'dashboard', component: DashboarComponent, canActivate: [AuthGuard]},
];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule]
})
export class TrinjerRoutingModule{}