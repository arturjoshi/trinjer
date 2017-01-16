import {Injectable, OnInit} from "@angular/core";
import {HttpUtils} from "../services/http-utils.service";
import {TokenService} from "../services/token.service";
import {ProjectDTO} from "./project-dto.interface";
import {UserDTO} from "../models/user.dto.interface";
import {IAccount} from "../models/account.interface";
import {Observable} from "rxjs";
import {AccountService} from "../services/account.service";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

@Injectable()
export class ProjectService implements OnInit{
    private projects: ProjectDTO[];
    private account: IAccount;

    createProject(project: ProjectDTO){
        let prefix = this.account.id.toString() + "/createProject";

        return Observable.create((observer) => {
            this.httpUtils.makePost(prefix, project)
                .subscribe((response) => {
                    observer.next(response);
                    observer.complete();
            });
        });
    }

    getProjectsByAccount(account: IAccount){}


    constructor(
        private httpUtils: HttpUtils,
        private tokenService: TokenService,
        private accountService: AccountService){}


    ngOnInit(){
        this.account = this.accountService.getAccount();

        let prefix = "accounts/" + this.account.id.toString() + "/projects";
        this.httpUtils.makeGet(prefix);
    }
}