import {Injectable} from "@angular/core";
import {UserDTO} from "../models/user";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class UserService{
    private localStorageKey: string = 'user';

    login(user: UserDTO){
        if(!this.isAuth(user)){
            localStorage.setItem(this.localStorageKey, JSON.stringify(user));
        }
    }

    logout(){
        localStorage.removeItem(this.localStorageKey);
    }

    isAuth(user: UserDTO){
        let localUser = localStorage.getItem(this.localStorageKey);
        return localUser == JSON.stringify(user);
    }
}