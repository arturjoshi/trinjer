import {Component} from "@angular/core";
import {AuthenticateService} from "./authenticate.service";
import {LoginUser} from "../models/login-user.model";
import {AccountService} from "../services/account.service";
import {MdDialogRef, MdSnackBar} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
/**
 * Created by xoll on 08.01.2017.
 */

@Component({
    selector: 'login',
    moduleId: module.id,
    templateUrl: 'login.template.html',
    styleUrls: ['login.css'],
    providers: [
        AuthenticateService
    ]
})
export class LoginDialog{
    user: LoginUser = LoginUser.getNewLoginUser();
    isLoginProcessed: boolean = false;
    loginForm: FormGroup;
    formErrors = {
        'username': '',
        'password': '',
    };
    validationMessages = {
        'username': {
            'required': 'Username in require',
            'wrong': "Can`t find user with such name"
        },
        'password': {
            'required': 'Password is require',
            'wrong': 'Wrong password'
        }
    };

    constructor(
        private dialogRef: MdDialogRef<LoginDialog>,
        private authenticateService: AuthenticateService,
        private formBuilder: FormBuilder,
        private snackBar: MdSnackBar
    ){
        this.loginForm = this.formBuilder.group({
            'username': [this.user.username, [Validators.required]],
            'password': [this.user.password, [Validators.required]]
        });
        this.loginForm.valueChanges.subscribe(() => {
            this.onFormChange();
        })
    }

    //noinspection JSUnusedGlobalSymbols
    login(){
        if(!this.loginForm.invalid) {
            //Activate spinner
            this.isLoginProcessed = true;

            this.authenticateService.authenticate(this.user)
                .subscribe(
                    () => {
                        this.isLoginProcessed = false;
                        this.close("Login");
                    },
                    (error: any): void => {
                        this.isLoginProcessed = false;
                        this.handleError(error);
                    });
        }else{
            this.formErrors.password = this.validationMessages.password.required;
            this.formErrors.username = this.validationMessages.username.required;
        }
    }

    private handleError(error: any): void {
        if (error == "No such account") {
            this.formErrors.username = this.validationMessages.username.wrong;
        } else if (error == "Bad credentials") {
            this.formErrors.password = this.validationMessages.password.wrong;
        } else {
            this.showErrorSnack();
        }
    }

    private showErrorSnack(){
        this.snackBar.open("Server connection error!", "Ok", {
            duration: 10000
        });
    }

    close(result: string = 'Cancel'){
        this.dialogRef.close(result);
    }

    private onFormChange(){
        if(!this.loginForm) return ;
        const form = this.loginForm;

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