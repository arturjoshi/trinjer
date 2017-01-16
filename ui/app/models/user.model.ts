import {UserDTO} from "./user.dto.interface";
/**
 * Created by xoll on 08.01.2017.
 */

export class User implements UserDTO{
    private constructor(
        private _username: string,
        private _email: string,
        private _password: string,
    ){}

    public static getNewUser(){
        return new User('', '','');
    }

    get username(): string {
        return this._username;
    }

    set username(value: string) {
        this._username = value;
    }

    get email(): string {
        return this._email;
    }

    set email(value: string) {
        this._email = value;
    }

    get password(): string {
        return this._password;
    }

    set password(value: string) {
        this._password = value;
    }
}