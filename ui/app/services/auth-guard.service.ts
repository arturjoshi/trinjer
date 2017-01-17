import {Injectable, Inject} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {TokenService} from "./token.service";
import {AccountService} from "./account.service";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class AuthGuard implements CanActivate{
    constructor(
        private tokenService: TokenService,
        private accountService: AccountService,
        private router: Router
    ){}

    canActivate(): boolean {
        return this.isAuthenticated();
    }

    isAuthenticated(): boolean{
        return this.tokenService.isTokenPresent();
    }

    logout(){
        this.tokenService.deleteToken();
        this.accountService.removeAccount();

        this.router.navigateByUrl('/');
    }
}