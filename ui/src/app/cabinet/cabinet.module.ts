import { MaterialModule } from '@angular/material';
import { CabinetComponent } from './cabinet.component';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

@NgModule({
    imports: [
        MaterialModule,
        CommonModule
    ],
    declarations: [
        CabinetComponent
    ],
    exports: [
        CabinetComponent
    ]

})
export class CabinetModule{}