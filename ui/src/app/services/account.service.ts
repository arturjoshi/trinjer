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
    private _account: IAccount;
    private accountBehavior: BehaviorSubject<IAccount>;

    constructor(){
        this._account = this.getAccountFromLocalStorage();

        this.accountBehavior = new BehaviorSubject<IAccount>(this._account);
    }

    get account(): Observable<IAccount> {
        return this.accountBehavior.asObservable();
    }

    saveAccount(account: IAccount): void{
        this._account = account;

        let obj = JSON.stringify(account);
        localStorage.setItem(this.key, obj);
        
        this.notifySubscribers();
    }

    removeAccount(): void{
        this._account = null;
        localStorage.removeItem(this.key);
        this.notifySubscribers();
    }

    private getAccountFromLocalStorage(): IAccount{
        let obj = localStorage.getItem(this.key);

        if(obj === null)
            return null;

        return AccountDTO.getFromJson(JSON.parse(obj));
    }

    private notifySubscribers(){
        this.accountBehavior.next(this._account);
    }
}