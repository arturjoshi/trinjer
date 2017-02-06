import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { ProjectsListComponent } from './projects-list/projects-list.component';
import { Routes } from '@angular/router';

const routes: Routes = [
    { path: 'projects/list', component: ProjectsListComponent}
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [RouterModule]
})
export class ProjectRoutingModule{

}