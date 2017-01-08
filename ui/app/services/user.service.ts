import {Injectable} from "@angular/core";
import {UserDTO} from "../models/user.interface";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class UserService{
    private localStorageKey: string = 'user';

    login(user: UserDTO){
        localStorage.setItem(this.localStorageKey, JSON.stringify(user));
    }

    logout(){
        localStorage.removeItem(this.localStorageKey);
    }

    registration(user: UserDTO){
        console.log("Registration user " + JSON.stringify(user));
    }

    registrationAndLogin(user: UserDTO){
        this.registration(user);
        this.login(user);
    }

    isAuth(user: UserDTO){
        let localUser = localStorage.getItem(this.localStorageKey);
        return localUser == JSON.stringify(user);
    }
}