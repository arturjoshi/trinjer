import { CabinetComponent } from './cabinet.component';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

@NgModule({
    imports: [
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