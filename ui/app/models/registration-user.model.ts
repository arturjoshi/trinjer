import {UserDTO} from "./user.interface";
import {RegistrationUserDTO} from "./registration-user.interface";
/**
 * Created by xoll on 08.01.2017.
 */

export class RegistrationUser implements RegistrationUserDTO{
    private constructor(
        private _username: string,
        private _email: string,
        private _password: string,
        private _passwordConfirm: string
    ){}

    public static getNewRegistrationUser(){
        return new RegistrationUser('', '', '', '');
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

    get passwordConfirm(): string {
        return this._passwordConfirm;
    }

    set passwordConfirm(value: string) {
        this._passwordConfirm = value;
    }
}
