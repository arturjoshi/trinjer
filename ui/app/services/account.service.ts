import {Injectable} from "@angular/core";
import {IAccount} from "../models/account.interface";
import {AccountDTO} from "../models/account";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

@Injectable()
export class AccountService{
    private key: string = "account";

    getAccount(): IAccount{
        let result = JSON.parse(localStorage.getItem(this.key) || null);
        let dto = new AccountDTO();

        dto.username = result.username;
        dto.email = result.email;
        dto.id = result.id;

        return dto;
    }

    saveAccount(account: IAccount): void{
        localStorage.setItem(this.key, JSON.stringify(account));
    }

    removeAccount(): void{
        localStorage.removeItem(this.key);
    }
}