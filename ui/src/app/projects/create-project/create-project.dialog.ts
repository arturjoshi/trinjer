import { Validators } from '@angular/forms';
import { Project } from './../models/project.model';
import { FormGroup } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { MdDialogRef } from '@angular/material';
import { Component } from '@angular/core';

@Component({
    selector: 'create-project',
    templateUrl: 'app/projects/create-project/create-project.template.html'
})
export class CreateProjectDialog{
    projectForm: FormGroup;
    project: Project;
    isCreateInProcess: boolean = false;

    constructor(
        private dialogRef: MdDialogRef<CreateProjectDialog>,
        private formBuilder: FormBuilder
    ){
        this.project = new Project('', true);

        this.projectForm = this.formBuilder.group({
            'name': [this.project.name, [Validators.required]],
            'isVisible': [Validators.required]
        });
    }

    create(){
        this.isCreateInProcess = true;
        setTimeout(() => {this.isCreateInProcess = false;}, 5000);
        console.log(this.project.serialize());
    }

    close(){
        this.dialogRef.close('Cancel');
    }
}