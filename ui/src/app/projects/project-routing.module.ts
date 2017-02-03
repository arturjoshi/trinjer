import { CreateProjectComponent } from './create-project/create-project.component';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { ProjectsListComponent } from './projects-list/projects-list.component';
import { Routes } from '@angular/router';

const routes: Routes = [
    { path: 'projects/list', component: ProjectsListComponent},
    { path: 'projects/create', component: CreateProjectComponent}
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [RouterModule]
})
export class ProjectRoutingModule{

}