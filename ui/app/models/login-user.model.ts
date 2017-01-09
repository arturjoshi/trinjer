/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

export class LoginUser{
    private constructor(
        private _username: string,
        private _password: string){}

    get username(): string {
        return this._username;
    }

    set username(value: string) {
        this._username = value;
    }

    get password(): string {
        return this._password;
    }

    set password(value: string) {
        this._password = value;
    }

    public static getNewLoginUser(): LoginUser{
        return new LoginUser('', '');
    }
}