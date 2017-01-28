/**
 * Created by xoll on 07.01.2017.
 */
import {Component} from "@angular/core";
import {TokenService} from "./services/token.service";
import {AuthGuard} from "./services/auth-guard.service";

//TODO: Connect material or bootstrap to project
@Component({
    selector: 'app',
    templateUrl: 'app/app.template.html',
    styleUrls: ['app/app.css'],
    providers: [AuthGuard]
})
export class AppComponent{
    title = "Trinjer";

    constructor(private authGuard: AuthGuard){}

    isAuth(){
        return this.authGuard.isAuthenticated();
    }

    logout(){
        this.authGuard.logout();
    }
}