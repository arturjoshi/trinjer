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

    public static getFromJson(json: Object): AccountDTO{
        let account = new AccountDTO();

        account.id = json['id'] || null;
        account.username = json['username'] || null;
        account.email = json['email'] || null;
        account.createdTime = json['createdTime'] || null;
        account.isTemp = json['isTemp'] || null;
        account.isConfirmed = json['isConfirmed'] || null;

        return account;
    }
}