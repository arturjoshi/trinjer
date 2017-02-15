import { ProjectDTO } from './../../models/project.interface';
import { Component, OnInit, Input } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'project-list-item',
    templateUrl: 'project-list-item.template.html',
    styleUrls: ['project-list-item.css']
})
export class ProjectListItemComponent {
    @Input() project: ProjectDTO;

    openProjectSettings(project: ProjectDTO){
        console.log("Open settings to project: " + project.serialize());
    }

    openInviteProjectDialog(project: ProjectDTO){
        console.log("Open invitions to project: " + project.serialize())
    }
}