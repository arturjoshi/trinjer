/**
 * Created by xoll on 08.01.2017.
 */
import {Component} from "@angular/core";
import {MdDialogRef, MdSnackBar} from "@angular/material";
import {RegistrationUserDTO} from "../models/registration-user.interface";
import {RegistrationUser} from "../models/registration-user.model";
import {FormBuilder, FormGroup, Validators, AbstractControl} from "@angular/forms";
import {RegistrationService} from "./registration.service";
import {AuthenticateService} from "../login/authenticate.service";
import {AccountService} from "../services/account.service";

@Component({
    moduleId: module.id,
    selector: 'registration',
    templateUrl: 'registration.template.html',
    styleUrls: ['registration.css'],
    providers: [
        RegistrationService,
        AuthenticateService
    ]
})
export class RegistrationDialog{
    user: RegistrationUserDTO;
    registrationForm: FormGroup;
    isRegistrationProcessed: boolean = false;
    formErrors = {
        'username': '',
        'email': '',
        'password': '',
        'passwordConfirm': ''
    };
    validationMessages = {
        'username': {
            'required': 'Username is require',
            'exist': "Account with such username is already exists"
        },
        'email': {
            'required': 'Email is require',
            'pattern': 'Incorrect email format',
            'exist': "Account with such email is already exists"
        },
        'password': {
            'required' : 'Password is require',
            'incorrect': 'Passwords are not equal'
        },
        'passwordConfirm': {
            'required': 'Re-enter password',
            'incorrect': 'Passwords are not equal'
        }
    };

    constructor(private dialogRef: MdDialogRef<RegistrationDialog>,
                private snackBar: MdSnackBar,
                private registrationService: RegistrationService,
                private formBuilder: FormBuilder){

        this.user = RegistrationUser.getNewRegistrationUser();
        this.registrationForm = this.formBuilder.group(this.getFormGroup());

        this.registrationForm.valueChanges.subscribe(() => {
            this.onFormChange();
        })
    }

    //noinspection JSUnusedGlobalSymbols
    registration(){
        if(!this.registrationForm.invalid){
            this.isRegistrationProcessed = true;
            this.registrationService.registration(this.user).subscribe(() => {
                this.isRegistrationProcessed = false;
                this.close("Registration");
            }, (error: any) => {
                this.isRegistrationProcessed = false;
                this.errorHandler(error);
            });
        }else{
            for(let prop in this.user){
                if(this.user[prop] == ''){
                    this.formErrors[prop] = this.validationMessages[prop].required;
                }
            }
        }
    }

    private errorHandler(error: any): void{
        const EMAIL_EXIST = "Account with such email is already exists";
        const USERNAME_EXIST = "Account with such username is already exists";

        if(error == USERNAME_EXIST){
            this.formErrors.username = this.validationMessages.username.exist;
        }else if(error === EMAIL_EXIST){
            this.formErrors.email = this.validationMessages.email.exist;
        }else{
            this.showErrorSnack();
        }
    }

    private showErrorSnack(){
        this.snackBar.open("Server connection error!", "Ok", {
            duration: 10000
        });
    }

    private getFormGroup(){
        return {
            'username': [this.user.username, [Validators.required]],
            'email': [this.user.email, [Validators.required,
                Validators.pattern(/^[a-z0-9!#$%&'*+\/=?^_`{|}~.-]+@[a-z0-9]([a-z0-9-]*[a-z0-9])?(\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$/i)]],
            'password': [this.user.password, [Validators.required, passwordValidator]],
            'passwordConfirm': [this.user.passwordConfirm, [Validators.required, passwordValidator]]
        }
    }

    private onFormChange(){
        const form = this.registrationForm;

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

    close(result: string = "Cancel"){
        this.dialogRef.close(result);
    }
}

export function passwordValidator(inputControl: AbstractControl): {[key: string]: boolean;} {
    let parentControl = inputControl.parent;
    if(!parentControl) return null;

    let password = parentControl.get('password').value;
    let confirmation = parentControl.get('passwordConfirm').value;

    if(password == '' || confirmation == '')
        return null;

    return password == confirmation ? null : {'incorrect': true};
}