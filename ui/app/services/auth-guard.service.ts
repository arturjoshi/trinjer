import {Injectable, Inject} from "@angular/core";
import {CanActivate} from "@angular/router";
import {UserService} from "./user.service";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class AuthGuard implements CanActivate{
    constructor(private userService: UserService){}

    canActivate(): boolean {
        return this.userService.isAuth();
    }

}