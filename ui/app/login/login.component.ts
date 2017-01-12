import {Component} from "@angular/core";
import {AuthenticateService} from "./authenticate.service";
import {LoginUser} from "../models/login-user.model";
import {Router} from "@angular/router";
/**
 * Created by xoll on 08.01.2017.
 */

@Component({
    selector: 'login',
    templateUrl: 'app/login/login.template.html',
    providers: [
        AuthenticateService
    ]
})
export class LoginComponent{
    user: LoginUser = LoginUser.getNewLoginUser();

    constructor(
        private authenticateService: AuthenticateService,
        private router: Router
    ){}

    onSubmit(){
        this.authenticateService.authenticate(this.user)
            .subscribe(() => {this.router.navigate(['/dashboard'])});
    }
}