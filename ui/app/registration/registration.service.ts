import {Injectable} from "@angular/core";
import {TokenService} from "../services/token.service";
import {Http} from "@angular/http";
import {UserDTO} from "../models/user.interface";
/**
 * Created by Andrew Zelenskiy on 09.01.2017.
 */

@Injectable()
export class RegistrationService{
    constructor(
        private tokenService: TokenService,
        private http: Http
    ){}

    registration(user: UserDTO){

    }
}