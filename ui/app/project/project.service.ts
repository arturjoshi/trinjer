import {Injectable, OnInit} from "@angular/core";
import {HttpUtils} from "../services/http-utils.service";
import {ProjectDTO} from "./project-dto.interface";
import {IAccount} from "../models/account.interface";
import {Observable, BehaviorSubject} from "rxjs/Rx";
import {AccountService} from "../services/account.service";
import {IProject} from "./project.interface";
import {Response} from "@angular/http";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

@Injectable()
export class ProjectService{
    private _projectsList: IProject[];

    //Observable items
    private _projects: BehaviorSubject<IProject[]>;
    projects: Observable<IProject[]>;

    private account: IAccount;


    createProject(project: ProjectDTO): Observable<IProject[]>{
        let postfix = this.account.id.toString() + "/createProject/";

        this.httpUtils.makePost(postfix, project)
            //map
            .subscribe((project: IProject) => {
                this._projectsList.push(project);
                this._projects.next(Object.assign({}, this._projectsList));
            });

        return this.projects;
    }


    refreshAllProjects(){
        this.getProjectsByClientFromBackend().subscribe(() => {
            this._projects.next(Object.assign({}, this._projectsList));
        });
    }


    getProjects(): Observable<IProject[]>{
        return this.getProjectsByClientFromBackend();
    }


    constructor(private httpUtils: HttpUtils, private accountService: AccountService){
        this._projects = new BehaviorSubject([]);
        this.projects = this._projects.asObservable();

        this.account = this.accountService.getAccount();
        console.log(this.account.id);
        if(this.account === null)
            throw new Error("Account does not exist!");

        this.getProjectsByClientFromBackend().subscribe((projects: IProject[]) => {
            this._projectsList = projects;
        });

    }


    private getProjectsByClientFromBackend(): any{
        let prefix = "/accounts/" + this.account.id.toString() + "/projects/";
        return this.httpUtils.makeGet(prefix).map((response: Response) => {
            return response;
        });
    }
}