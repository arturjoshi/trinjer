import {UserDTO} from "./user.interface";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

export class LoginUser implements UserDTO{
    private constructor(
        public username: string,
        public email: string,
        public password: string
    ){}
    public static getNewLoginUser(): LoginUser{
        return new LoginUser('', '', '');
    }
}