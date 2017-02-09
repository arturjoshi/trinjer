import { CabinetRoutingModule } from './cabinet.routing.module';
import { ProjectModule } from './projects/projects.module';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '@angular/material';
import { MainComponent } from './main.component';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

@NgModule({
    imports: [
        RouterModule,
        MaterialModule,
        CommonModule,

        CabinetRoutingModule,

        ProjectModule
    ],
    declarations: [
        MainComponent
    ]
})
export class CabinetModule{}