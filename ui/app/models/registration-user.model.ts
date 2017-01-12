import {UserDTO} from "./user.interface";
import {RegistrationUserDTO} from "./registration-user.interface";
/**
 * Created by xoll on 08.01.2017.
 */

export class RegistrationUser implements RegistrationUserDTO{
    private constructor(
        public username: string,
        public email: string,
        public password: string,
        public passwordConfirm: string
    ){}

    public static getNewRegistrationUser(){
        return new RegistrationUser('', '', '', '');
    }
}
