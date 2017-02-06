import { Validators } from '@angular/forms';
import { Project } from './../models/project.model';
import { FormGroup } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { MdDialogRef } from '@angular/material';
import { Component } from '@angular/core';

@Component({
    selector: 'create-project',
    templateUrl: 'app/projects/create-project/create-project.template.html',
    styleUrls: ['app/projects/create-project/create-project.css']
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
            this.isCreateInProcess = true;
            setTimeout(() => {this.isCreateInProcess = false;}, 5000);
            console.log(this.project.serialize());
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