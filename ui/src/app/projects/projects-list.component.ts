import { ProjectDTO } from './models/project.interface';
import { ProjectsService } from './services/projects.service';
import { Component } from '@angular/core';

@Component({
    selector: 'projects-list',
    template: `<h1>Projects list </h1>`,
    providers: [ProjectsService]
})
export class ProjectsListComponent{
    private projectsArray: ProjectDTO[];
    
    constructor(private projectsService: ProjectsService){
        this.projectsService.projects.subscribe((projects: ProjectDTO[]) => {
            this.projectsArray = projects;
        })
    }
}