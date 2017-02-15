import { CreateProjectDialog } from './../create-project/create-project.dialog';
import { MdDialogRef } from '@angular/material';
import { MdDialog } from '@angular/material';
import { ProjectDTO } from './../models/project.interface';
import { ProjectsService } from './../services/projects.service';
import { Component } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'projects-list',
    templateUrl: 'projects-list.template.html',
    styleUrls: ['projects-list.css']
})
export class ProjectsListComponent {
    projectsArray: ProjectDTO[];
    private createProjectDialog: MdDialogRef<CreateProjectDialog>;

    constructor(private projectsService: ProjectsService, private dialog: MdDialog) {
        this.projectsService.projects.subscribe((projects: ProjectDTO[]) => {
            this.projectsArray = projects;
        })
    }

    isProjectsPresent(): boolean {
        return this.projectsArray.length != 0;
    }

    openCreateProjectDialog(){
        this.createProjectDialog = this.dialog.open(CreateProjectDialog);
        this.createProjectDialog.afterClosed().subscribe(() => {
            this.createProjectDialog = null;
        });
    }
}