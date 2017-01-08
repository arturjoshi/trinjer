/**
 * Created by xoll on 08.01.2017.
 */
import {Component} from "@angular/core";
import {RegistrationUser} from "./registration-user.model";

@Component({
    selector: 'registration',
    templateUrl: 'app/registration/registration.template.html'
})
export class RegistrationComponent{
    user: RegistrationUser;

    constructor(){
        this.user = RegistrationUser.getNewRegistrationUser();
    }

    onSubmit(){
        console.log(this.user);
    }
}