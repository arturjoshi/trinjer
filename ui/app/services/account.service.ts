import {Injectable} from "@angular/core";
import {IAccount} from "../models/account.interface";
import {AccountDTO} from "../models/account";
import {BehaviorSubject, Observable} from "rxjs/Rx";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

@Injectable()
export class AccountService{
    private key: string = 'account';
    private accountBehavior: BehaviorSubject<IAccount>;

    constructor(){
        let account = this.getAccountFromLocalStorage();

        this.accountBehavior = new BehaviorSubject<IAccount>(account);
    }

    get account(): Observable<IAccount> {
        return this.accountBehavior.asObservable();
    }

    saveAccount(account: IAccount): void{
        let obj = JSON.stringify(account);
        localStorage.setItem(this.key, obj);
        this.accountBehavior.next(account);
    }

    removeAccount(): void{
        localStorage.removeItem(this.key);
        this.accountBehavior.next(null);
    }

    private getAccountFromLocalStorage(): IAccount{
        let obj = localStorage.getItem(this.key);

        if(obj === null)
            return null;

        return AccountDTO.getFromJson(JSON.parse(obj));
    }
}