import {Component} from "@angular/core";
import {AuthenticateService} from "./authenticate.service";
import {LoginUser} from "../models/login-user.model";
import {AccountService} from "../services/account.service";
import {MdDialogRef} from "@angular/material";
import {NgForm, FormBuilder, FormGroup, Validators} from "@angular/forms";
/**
 * Created by xoll on 08.01.2017.
 */

@Component({
    selector: 'login',
    templateUrl: 'app/login/login.template.html',
    styleUrls: ['app/login/login.css'],
    providers: [
        AuthenticateService,
        AccountService
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
            'required': 'Username in require'
        },
        'password': {
            'required': 'Password is require'
        }
    };

    constructor(
        private dialogRef: MdDialogRef<LoginDialog>,
        private authenticateService: AuthenticateService,
        private formBuilder: FormBuilder
    ){
        this.loginForm = this.formBuilder.group({
            'username': [this.user.username, [Validators.required]],
            'password': [this.user.password, [Validators.required]]
        });
        this.loginForm.valueChanges.subscribe((data: any) => {
            this.onFormChange(data);
        })
    }

    login(){
        //If form valid
        if(this.loginForm._status != "INVALID") {
            this.isLoginProcessed = true;
            this.authenticateService.authenticate(this.user)
                .subscribe(
                    () => {
                        this.isLoginProcessed = false;
                        this.close();
                    },
                    (error: any): void => {
                        this.isLoginProcessed = false;
                        if (error == "No such account") {
                            console.log("Username failed");
                        } else if (error == "Bad credentials") {
                            console.log("Credentials");
                        } else {
                            // Handle connection error
                            // console.log(error);
                        }
                    });
        }else{
            this.formErrors.password = this.validationMessages.password.required;
            this.formErrors.username = this.validationMessages.username.required;
        }
    }

    close(){
        this.dialogRef.close('Cancel');
    }

    private onFormChange(data? : any){
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