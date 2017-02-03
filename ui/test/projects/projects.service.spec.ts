import {TestBed, inject} from "@angular/core/testing";
import {ProjectsService} from "../../src/app/projects/services/projects.service";
import {AccountService} from "../../src/app/services/account.service";
import {IAccount} from "../../src/app/models/account.interface";
import {AccountDTO} from "../../src/app/models/account";
import {ProjectDTO} from "../../src/app/projects/models/project.interface";
import {Project} from "../../src/app/projects/models/project.model";
import {BaseRequestOptions, Http, RequestMethod, Response, ResponseOptions} from "@angular/http";
import {MockBackend, MockConnection} from "@angular/http/testing";
import {HttpUtils} from "../../src/app/services/http-utils.service";
import {TokenService} from "../../src/app/services/token.service";
import {Serializable} from "../../src/app/projects/models/serialization.interface";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

describe("Project service", () => {
    let projectsService: ProjectsService;
    let account: IAccount = getAccount();
    let token = "testoken";
    let mockBackend: MockBackend;

    beforeEach(configureModule);

    beforeEach(() => {
        spyOnLocalStorage();
        localStorage.setItem('account', JSON.stringify(account));
        localStorage.setItem('token', token);
    });

    beforeEach(inject([ProjectsService, MockBackend], (ps: ProjectsService, mb: MockBackend) => {
        projectsService = ps;
        mockBackend = mb;
    }));

    it('Add projects', () => {
        let projects: ProjectDTO[] = [];

        mockBackend.connections.subscribe((connection: MockConnection) => {
            let expectedUrl = "http://localhost:8080/api/accounts/" + account.id + "/projects";

            expect(connection.request.method).toEqual(RequestMethod.Get);
            expect(connection.request.headers.get('x-auth-token')).toEqual(token);
            expect(connection.request.url).toEqual(expectedUrl);

            connection.mockRespond(new Response(new ResponseOptions({
                body: JSON.stringify(projects)
            })));
        });

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

    it('Get list of projects', () => {
        let projects: Project[] = [
            new Project("First project"),
            new Project("Second project"),
            new Project("Third project"),
            new Project("Fours project")
        ];

        mockBackend.connections.subscribe((connection: MockConnection) => {
            let expectedUrl = "http://localhost:8080/api/accounts/" + account.id + "/projects";

            expect(connection.request.method).toEqual(RequestMethod.Get);
            expect(connection.request.headers.get("x-auth-token")).toEqual(token);
            expect(connection.request.url).toEqual(expectedUrl);

            let serializableProjects = serializeList(projects);

            connection.mockRespond(new Response(new ResponseOptions({
                body: {
                    _embedded:{
                        projects: serializableProjects
                    } 
                }
            })));
        });

        projectsService.getNewProjects().subscribe((p: Project[]) => {
            expect(serializeList(p)).toEqual(serializeList(projects))
        });
    })
});

function serializeList(list: Serializable[]){
    let result: string[] = [];
    for(let item of list){
        result.push(item.serialize());
    }
    return result;
}

function configureModule(){
    let httpProvider = {
        provide: Http,
        useFactory: (mockBackend: MockBackend, baseRequestOptions: BaseRequestOptions) => {
            return new Http(mockBackend, baseRequestOptions);
        },
        deps: [
            MockBackend,
            BaseRequestOptions
        ]
    };

    TestBed.configureTestingModule({
        providers: [
            ProjectsService,
            AccountService,
            HttpUtils,
            TokenService,
            httpProvider,
            MockBackend,
            BaseRequestOptions
        ]
    });
}

function getAccount(){
    return AccountDTO.getFromJson({
        id: 12,
        username: "testuser",
        email: "test@email.com",
        createdTime: null,
        isConfirmed: false,
        isTemp: false
    });
}

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