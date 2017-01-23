import {TestBed, inject} from "@angular/core/testing";
import {ProjectsService} from "../../app/projects/services/projects.service";
import {Project} from "../../app/projects/models/project.model";
import {ProjectDTO} from "../../app/projects/models/project.interface";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

describe("Projects service", ()=>{
    let projectsService: ProjectsService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                ProjectsService
            ]
        });
    });

    beforeEach(inject([ProjectsService], (service: ProjectsService) => {
        projectsService = service;
    }));

    it("Subscribe and add project", () => {
        let project = new Project("Test project");

        let isFirst: boolean = true;

        projectsService.projects.subscribe((projects: ProjectDTO[]) => {
            if(isFirst){
                expect(projects.length).toEqual(0);
                isFirst = false;
            }else{
                expect(projects.length).toEqual(1);
            }
        });

        projectsService.addProject(project);
        expect(isFirst).toBeFalsy();
    })
});