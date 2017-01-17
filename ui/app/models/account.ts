import {IAccount} from "./account.interface";
/**
 * Created by Andrew Zelenskiy on 17.01.2017.
 */

export class AccountDTO implements IAccount{
    id: number;
    username: string;
    email: string;
    createdTime: string;
    isConfirmed: boolean;
    isTemp: boolean;

    constructor(){
        this.id = 0;
        this.username = '';
        this.email = '';
        this.createdTime = '';
        this.isConfirmed = false;
        this.isTemp = false;
    }
}