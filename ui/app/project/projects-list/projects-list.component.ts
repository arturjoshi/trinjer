import {Component, OnInit} from "@angular/core";
import {ProjectService} from "../project.service";
import {IProject} from "../project.interface";
/**
 * Created by Andrew Zelenskiy on 17.01.2017.
 */

@Component({
    selector: 'projects-list',
    templateUrl: 'app/project/projects-list/projects-list.template.html',
    providers: [ProjectService]
})
export class ProjectsListComponent implements OnInit{
    projects: IProject[];

    constructor(private projectService: ProjectService){}


    //noinspection JSUnusedGlobalSymbols
    ngOnInit(){

        this.projectService.getProjects().subscribe((projects) => {
            console.log("Initialize projects: " + projects);
            this.projects = projects;
        });
    }
}