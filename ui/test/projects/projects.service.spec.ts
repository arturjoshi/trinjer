import {TestBed, inject, async} from "@angular/core/testing";
import {ProjectsService} from "../../app/projects/services/projects.service";
import {Project} from "../../app/projects/models/project.model";
import {ProjectDTO} from "../../app/projects/models/project.interface";
import {AccountService} from "../../app/services/account.service";
import {IAccount} from "../../app/models/account.interface";
import {AccountDTO} from "../../app/models/account";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

describe("Projects service", ()=>{
    let projectsService: ProjectsService;
    let accountService: AccountService;

    let account: IAccount;

    beforeEach(() => {
        account = new AccountDTO();
        account.id = 12;
        account.username = "testusername";
        account.email = "test@email.com";
    });

    beforeEach(() => {
        let storage = {
            'account': account
        };
        spyOn(localStorage, 'getItem').and.callFake((key:string) => {
            return storage[key] || null;
        });
        spyOn(localStorage, 'setItem').and.callFake((key: string, value: string) => {
            storage[key] = value;
        });
    });

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                AccountService,
                ProjectsService
            ]
        });
    });

    beforeEach(inject([AccountService, ProjectsService], (as: AccountService, ps: ProjectsService) => {
        projectsService = ps;
        accountService = as;
    }));

    it("account get success", async(() => {
        expect(projectsService.account).toBeDefined();
    }));


    it("Subscribe and add project", async(() => {
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
    }))
});