import {Injectable, OnInit} from "@angular/core";
import {HttpUtils} from "../services/http-utils.service";
import {ProjectDTO} from "./project-dto.interface";
import {IAccount} from "../models/account.interface";
import {Observable, Observer, BehaviorSubject} from "rxjs";
import {AccountService} from "../services/account.service";
import {IProject} from "./project.interface";
import {Response} from "@angular/http";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

@Injectable()
export class ProjectService implements OnInit{
    private projectObserver: BehaviorSubject<IProject[]>;
    private _projects: IProject[];
    private account: IAccount;

    createProject(project: ProjectDTO): Observable<IProject[]>{
        let prefix = this.account.id.toString() + "/createProject";

        this.httpUtils.makePost(prefix, project)
            .subscribe((fullProject: IProject) => {
                this._projects.push(fullProject);
                this.projectObserver.next(Object.assign({}, this._projects));
        });

        return this.projectObserver.asObservable();
    }

    getProjects(): Observable<IProject[]>{
        return this.projectObserver.asObservable();
    }


    constructor(
        private httpUtils: HttpUtils,
        private accountService: AccountService){}


    //noinspection JSUnusedGlobalSymbols
    ngOnInit(){
        this.account = this.accountService.getAccount();

        this.getProjectFromHttp()
            .subscribe((projects: IProject[]) => {
                this._projects = projects;
            });
    }

    private getProjectFromHttp(): Observable<IProject[]>{
        let prefix = "accounts/" + this.account.id.toString() + "/projects";
        return this.httpUtils.makeGet(prefix).map((response: Response): IProject[] => {
            return <IProject[]>response;
        })
    }
}