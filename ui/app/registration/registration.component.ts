/**
 * Created by xoll on 08.01.2017.
 */
import {Component} from "@angular/core";
import {RegistrationUser} from "../models/registration-user.model";
import {TokenService} from "../services/token.service";

//TODO: Go to reactive forms
@Component({
    selector: 'registration',
    templateUrl: 'app/registration/registration.template.html',
    providers: [TokenService]
})
export class RegistrationComponent{
    user: RegistrationUser;

    constructor(){
        this.user = RegistrationUser.getNewRegistrationUser();
    }

    onSubmit(){
    }
}