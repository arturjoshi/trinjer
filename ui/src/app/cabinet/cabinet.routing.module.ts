import { AuthGuard } from './../services/auth-guard.service';
import { MainComponent } from './main/main.component';
import { Routes } from '@angular/router';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

const routes: Routes = [
    {path: 'cabinet', canActivate: [AuthGuard], component: MainComponent, children: [
        {path: '', loadChildren: './app/cabinet/projects/projects.module#ProjectModule'}
    ]}
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]

})
export class CabinetRoutingModule{}