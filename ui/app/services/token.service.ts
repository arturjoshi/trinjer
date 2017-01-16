import {Injectable} from "@angular/core";
import {UserDTO} from "../models/user.dto.interface";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class TokenService{
    private tokenKey: string = 'token';

    saveToken(token: string){
        localStorage.setItem(this.tokenKey, token);
    }

    deleteToken(){
        localStorage.removeItem(this.tokenKey);
    }

    isTokenPresent(){
        let tokenItem = localStorage.getItem(this.tokenKey);
        return tokenItem !== null;
    }

    getToken() {
        if(this.isTokenPresent()){
            return localStorage.getItem(this.tokenKey);
        }else{
            throw Error("Can't find token! User is not authorize");
        }
    }
}