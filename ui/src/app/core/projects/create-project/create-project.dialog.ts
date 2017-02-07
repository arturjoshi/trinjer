import { ProjectDTO } from './../models/project.interface';
import { ProjectsService } from './../services/projects.service';
import { Validators } from '@angular/forms';
import { Project } from './../models/project.model';
import { FormGroup } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { MdDialogRef } from '@angular/material';
import { Component } from '@angular/core';

@Component({
    selector: 'create-project',
    templateUrl: 'app/core/projects/create-project/create-project.template.html',
    styleUrls: ['app/core/projects/create-project/create-project.css']
})
export class CreateProjectDialog{
    projectForm: FormGroup;
    project: Project;
    isCreateInProcess: boolean = false;
    formErrors = {
        'projectName': '',
    }
    validationMessages = {
        'projectName': {
            'required': 'Name is require'
        }
    }; 

    constructor(
        private dialogRef: MdDialogRef<CreateProjectDialog>,
        private projectsService: ProjectsService,
        private formBuilder: FormBuilder
    ){
        this.project = new Project('', true);

        this.projectForm = this.formBuilder.group({
            'projectName': [this.project.name, [Validators.required]],
            'isVisible': [this.project.isVisible, []]
        });
        this.projectForm.valueChanges.subscribe(() => {
            this.onFormChange();
        })
    }

    create(){
        if(this.projectForm.invalid){
            this.formErrors.projectName = this.validationMessages.projectName.required;
        }else{
            this.projectsService.addProject(this.project).subscribe((project: ProjectDTO) => {
                this.dialogRef.close("Create!");
                console.log(project);
            });
        }
    }

    close(){
        this.dialogRef.close('Cancel');
    }

    private onFormChange(){
        if(!this.projectForm) return ;
        const form = this.projectForm;

        for(const field in this.formErrors){
            this.formErrors[field] = '';
            const control = form.get(field);

            if(control && control.dirty && !control.valid){
                const messages = this.validationMessages[field];
                for(const key in control.errors){
                    this.formErrors[field] += messages[key] + " ";
                }
            }
        }
    }
}