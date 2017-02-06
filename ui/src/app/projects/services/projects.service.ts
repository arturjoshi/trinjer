import {Injectable} from "@angular/core";
import {ProjectDTO} from "../models/project.interface";
import {BehaviorSubject, Observable, Observer} from "rxjs/Rx";
import {AccountService} from "../../services/account.service";
import {IAccount} from "../../models/account.interface";
import {HttpUtils} from "../../services/http-utils.service";
import {Response} from "@angular/http";
import {Project} from "../models/project.model";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

@Injectable()
export class ProjectsService{
    private projectsArray: ProjectDTO[];
    private projectsBehavior: BehaviorSubject<ProjectDTO[]>;
    private account: IAccount;

    constructor(private accountService: AccountService, private httpUtils: HttpUtils){
        this.projectsArray = [];
        this.projectsBehavior = new BehaviorSubject<ProjectDTO[]>(this.projectsArray);

        this.accountService.account.subscribe((account: IAccount) => {
            this.account = account;
            this.getProjectsFromBackend().subscribe();
        });
    }

    private getProjectsFromBackend(): Observable<ProjectDTO[]>{
        let url = "accounts/" + this.account.id + "/projects";

        return Observable.create((observer: Observer<ProjectDTO[]>) => {
            this.httpUtils.makeGet(url)
                .map((res: Response) => ProjectsService.extractData(res))
                .subscribe((projects: ProjectDTO[]) => {
                    this.projectsArray = projects;

                    observer.next(projects);
                    observer.complete();
                });
        });
    }

    private static extractData(response: Response): Project[]{
        let json = response.json();

        let projects: Project[] = [];

        for(let item of json._embedded.projects){
            item = JSON.parse(item);
            let project = new Project(item['name'], item['isVisible']);
            projects.push(project);
        }

        return projects;
    }

    getNewProjects(): Observable<ProjectDTO[]>{
        this.getProjectsFromBackend().subscribe(() => {
            this.projectsBehavior.next(this.projectsArray);
        });
        return this.projectsBehavior.asObservable();
    }

    addProject(project: ProjectDTO): Observable<ProjectDTO>{
        return Observable.create((observer: Observer<ProjectDTO>) => {
            let urlPrefix = this.account.id + "/createProject/"; 
            this.httpUtils.makePost(urlPrefix, project.serialize()).subscribe(() => {
                this.projectsArray.push(project);
                this.sendNotification();
                
                observer.next(project);
                observer.complete();
            })
        })
    }

    private sendNotification(): void{
        this.projectsBehavior.next(this.projectsArray);
    }

    get projects(): Observable<ProjectDTO[]>{
        this.sendNotification();
        return this.projectsBehavior.asObservable();
    }
}