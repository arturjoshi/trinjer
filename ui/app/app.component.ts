/**
 * Created by xoll on 07.01.2017.
 */
import {Component} from "@angular/core";
import {UserService} from "./services/user.service";

//TODO: Connect material or bootstrap to project
@Component({
    selector: 'app',
    templateUrl: 'app/app.template.html',
    providers: [UserService]
})
export class AppComponent{
    title = "Trinjer";

    constructor(private userService: UserService){}

    isAuth(){
        return this.userService.isAuth();
    }

    logout(){
        console.log("logout");
        this.userService.logout();
    }
}