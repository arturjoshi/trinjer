import { ProjectDTO } from './../models/project.interface';
import { ProjectsService } from './../services/projects.service';
import { Component } from '@angular/core';

@Component({
    selector: 'projects-list',
    templateUrl: 'app/projects/projects-list/projects-list.template.html',
    providers: [ProjectsService]
})
export class ProjectsListComponent {
    projectsArray: ProjectDTO[];

    constructor(private projectsService: ProjectsService) {
        this.projectsService.projects.subscribe((projects: ProjectDTO[]) => {
            this.projectsArray = projects;
        })
    }

    isProjectsPresent(): boolean {
        return this.projectsArray.length != 0;
    }
}