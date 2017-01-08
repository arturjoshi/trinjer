/**
 * Created by xoll on 08.01.2017.
 */
import {Component} from "@angular/core";
import {RegistrationUser} from "./registration-user.model";
import {UserService} from "../services/user.service";

@Component({
    selector: 'registration',
    templateUrl: 'app/registration/registration.template.html',
    providers: [UserService]
})
export class RegistrationComponent{
    user: RegistrationUser;

    constructor(private userService: UserService){
        this.user = RegistrationUser.getNewRegistrationUser();
    }

    onSubmit(){
        this.userService.registrationAndLogin(this.user);
    }
}