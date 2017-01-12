import {Injectable, Inject} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {TokenService} from "./token.service";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class AuthGuard implements CanActivate{
    constructor(
        private tokenService: TokenService,
        private router: Router
    ){}

    canActivate(): boolean {
        return this.isAuthenticated();
    }

    isAuthenticated(): boolean{
        return this.tokenService.isTokenPresent();
    }

    //TODO: Move to other service
    logout(){
        this.tokenService.deleteToken();
        this.router.navigateByUrl('/');
    }
}