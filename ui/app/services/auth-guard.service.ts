import {Injectable, Inject} from "@angular/core";
import {CanActivate} from "@angular/router";
import {TokenService} from "./token.service";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class AuthGuard implements CanActivate{
    constructor(private tokenService: TokenService){}

    canActivate(): boolean {
        return this.isAuthenticated();
    }

    isAuthenticated(): boolean{
        return this.tokenService.isTokenPresent();
    }

    logout(){
        this.tokenService.deleteToken();
    }
}