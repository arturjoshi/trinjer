import {IAccount} from "../models/account.interface";
import {Injectable} from "@angular/core";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

@Injectable()
export class AccountService{
    private key: string = "account";

    saveAccount(account: IAccount): void{
        localStorage.setItem(this.key, JSON.stringify(account));
    }

    getAccount(): IAccount{
        return localStorage.getItem(this.key) || null;
    }

    removeAccount(): void{
        localStorage.removeItem(this.key);
    }
}