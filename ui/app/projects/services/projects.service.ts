import {Injectable} from "@angular/core";
import {ProjectDTO} from "../models/project.interface";
import {BehaviorSubject, Observable} from "rxjs/Rx";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

@Injectable()
export class ProjectsService{
    private projectsArray: ProjectDTO[];
    private projectsBehavior: BehaviorSubject<ProjectDTO[]>;

    constructor(){
        this.projectsArray = [];
        this.projectsBehavior = new BehaviorSubject<ProjectDTO[]>(this.projectsArray);
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