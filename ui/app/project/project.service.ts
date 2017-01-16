import {Injectable, OnInit} from "@angular/core";
import {HttpUtils} from "../services/http-utils.service";
import {TokenService} from "../services/token.service";
import {ProjectDTO} from "./project-dto.interface";
import {IAccount} from "../models/account.interface";
import {Observable, Observer} from "rxjs";
import {AccountService} from "../services/account.service";
import {IProject} from "./project.interface";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

@Injectable()
export class ProjectService implements OnInit{
    private projects: Observable<IProject[]>;
    private account: IAccount;

    createProject(project: ProjectDTO): Observable<any>{
        let prefix = this.account.id.toString() + "/createProject";

        return Observable.create((observer) => {
            this.httpUtils.makePost(prefix, project)
                .subscribe((response) => {
                    observer.next(response);
                    observer.complete();
            });
        });
    }

    getProjects(): Observable<IProject[]>{
        return this.projects;
    }


    constructor(
        private httpUtils: HttpUtils,
        private tokenService: TokenService,
        private accountService: AccountService){}


    ngOnInit(){
        this.account = this.accountService.getAccount();

        let prefix = "accounts/" + this.account.id.toString() + "/projects";

        this.projects = Observable.create((observer: Observer) => {
            this.httpUtils.makeGet(prefix)
                .catch((error: Error): Error => {observer.error(error); return error;})
                .subscribe((projects: IProject[]) => {
                    observer.next(projects);
                    observer.complete();
            });
        });
    }
}