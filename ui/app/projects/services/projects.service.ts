import {Injectable} from "@angular/core";
import {ProjectDTO} from "../models/project.interface";
import {BehaviorSubject, Observable, Observer} from "rxjs/Rx";
import {AccountService} from "../../services/account.service";
import {IAccount} from "../../models/account.interface";
import {HttpUtils} from "../../services/http-utils.service";
import {Response} from "@angular/http";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

@Injectable()
export class ProjectsService{
    private projectsArray: ProjectDTO[];
    private projectsBehavior: BehaviorSubject<ProjectDTO[]>;
    private account: IAccount;

    constructor(
        private accountService: AccountService,
        private httpUtils: HttpUtils
    ){
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
                .map((response: Response): ProjectDTO[] => <ProjectDTO[]>response.json())
                .subscribe((projects: ProjectDTO[]) => {
                    this.projectsArray = projects;

                    observer.next(projects);
                    observer.complete();
                });
        });
    }

    addProject(project: ProjectDTO): void{
        this.projectsArray.push(project);
        this.sendNotification();
        this.projectsBehavior.next(this.projectsArray);
    }

    private sendNotification(): void{
        this.projectsBehavior.next(this.projectsArray);
    }

    get projects(): Observable<ProjectDTO[]>{
        this.sendNotification();
        return this.projectsBehavior.asObservable();
    }
}