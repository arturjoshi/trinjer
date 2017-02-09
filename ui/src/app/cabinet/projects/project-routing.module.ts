import { AuthGuard } from './../../services/auth-guard.service';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { ProjectsListComponent } from './projects-list/projects-list.component';
import { Routes } from '@angular/router';

const routes: Routes = [
    { path: 'projects', component: ProjectsListComponent, canActivate: [AuthGuard]}
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [RouterModule]
})
export class ProjectRoutingModule{

}