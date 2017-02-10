import { Project } from './../../models/project.model';
import { Component, Input } from '@angular/core';


@Component({
    moduleId: module.id,
    selector: 'project-list-item',
    templateUrl: 'project-list-item.template.html'
})
export class ProjectListItemComponent{
    @Input() project:Project;
}