import {TestBed, inject} from "@angular/core/testing";
import {ProjectsService} from "../../app/projects/services/projects.service";
import {AccountService} from "../../app/services/account.service";
import {IAccount} from "../../app/models/account.interface";
import {AccountDTO} from "../../app/models/account";
import {ProjectDTO} from "../../app/projects/models/project.interface";
import {Project} from "../../app/projects/models/project.model";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

describe("Project service", () => {
    let projectsService: ProjectsService;
    let account: IAccount = AccountDTO.getFromJson({
        id: 12,
        username: "testuser",
        email: "test@email.com",
        createdTime: null,
        isConfirmed: false,
        isTemp: false
    });

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                ProjectsService,
                AccountService
            ]
        });
    });

    beforeEach(() => {
        spyOnLocalStorage();
        localStorage.setItem('account', JSON.stringify(account));
    });

    beforeEach(inject([ProjectsService], (ps: ProjectsService) => {
        projectsService = ps;
    }));

    it('Add projects', () => {
        let isFirst = false;
        let length = 0;

        projectsService.projects.subscribe((projects: ProjectDTO[]) => {
            if(isFirst)
                isFirst = false;
            expect(projects.length).toEqual(length);
        });

        length = 1;
        projectsService.addProject(new Project("test name"));
        expect(isFirst).toBeFalsy();
    });

});

function spyOnLocalStorage(){
    let storage = {};
    spyOn(localStorage, 'getItem').and.callFake((key: string) =>{
        return storage[key];
    });
    spyOn(localStorage, 'setItem').and.callFake((key:string, value: string) => {
        storage[key] = value;
    });
    spyOn(localStorage, 'removeItem').and.callFake((key:string) => {
        delete storage[key];
    });
}