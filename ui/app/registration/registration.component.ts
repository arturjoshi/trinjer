/**
 * Created by xoll on 08.01.2017.
 */
import {Component} from "@angular/core";
import {RegistrationUser} from "../models/registration-user.model";
import {TokenService} from "../services/token.service";
import {RegistrationService} from "./registration.service";
import {AuthenticateService} from "../login/authenticate.service";

//TODO: Go to reactive forms
@Component({
    selector: 'registration',
    templateUrl: 'app/registration/registration.template.html',
    providers: [RegistrationService, AuthenticateService]
})
export class RegistrationComponent{
    user: RegistrationUser;

    constructor(private registrationService: RegistrationService){
        this.user = RegistrationUser.getNewRegistrationUser();
    }

    onSubmit(){
        this.registrationService.registration(this.user);
    }
}