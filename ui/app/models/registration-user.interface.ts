/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */
import {UserDTO} from "./user.interface";

export interface RegistrationUserDTO extends UserDTO{
    username: string;
    email: string;
    password: string;
    passwordConfirm: string;
}