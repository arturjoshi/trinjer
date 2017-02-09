import { TestRoutingComponent } from './test-routing.component';
import { CabinetComponent } from './cabinet.component';
import { Routes } from '@angular/router';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

const routes: Routes = [
    {path: 'cabinet', component: CabinetComponent, children: [
        {path: 'test', component: TestRoutingComponent}
    ]}
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]

})
export class CabinetRoutingModule{}