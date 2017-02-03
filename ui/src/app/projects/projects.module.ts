import { CreateProjectComponent } from './create-project/create-project.component';
import { ProjectRoutingModule } from './project-routing.module';
import { MaterialModule } from '@angular/material';
import { ProjectsListComponent } from './projects-list/projects-list.component';
import { ProjectsService } from './services/projects.service';
import { TokenService } from './../services/token.service';
import { AccountService } from './../services/account.service';
import { HttpUtils } from './../services/http-utils.service';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
    imports: [
        CommonModule,
        MaterialModule.forRoot(),
        HttpModule,
        ProjectRoutingModule
    ],
    providers: [
        HttpUtils,
        AccountService,
        TokenService,
        ProjectsService
    ],
    declarations: [
        ProjectsListComponent,
        CreateProjectComponent
    ]
})
export class ProjectModule{}