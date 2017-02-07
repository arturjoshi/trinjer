import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

const routes: Routes = [
    {path: "projects/", loadChildren: 'projects/projects.module#ProjectModule'}
]

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    
})
export class CoreModuleRouting{}