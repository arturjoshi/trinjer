import {Injectable} from "@angular/core";
import {CanActivate} from "@angular/router";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class AuthGuard implements CanActivate{
    canActivate(): boolean {
        return false;
    }

}