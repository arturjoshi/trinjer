import {Injectable} from "@angular/core";
import {IAccount} from "../models/account.interface";
import {Account} from "../models/account";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

@Injectable()
export class AccountService{
    private key: string = "account";

    getAccount(): IAccount{
        let result = localStorage.getItem(this.key) || null;
        return Account.createFromObject(result);
    }

    saveAccount(account: IAccount): void{
        localStorage.setItem(this.key, JSON.stringify(account));
    }

    removeAccount(): void{
        localStorage.removeItem(this.key);
    }
}