import {IAccount, IAccount} from "./account.interface";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

export class Account implements Account{
    private _id: number;
    private _username: string;
    private _email: string;
    private _createdTime: string;
    private _isConfirmed: boolean;
    private _isTemp: boolean;

    public static createFromObject(obj: Object): Account{
        let newAccount = new Account();

        for(let prop of newAccount){
            if(!obj.hasOwnProperty(prop)){
                throw new Error("Object does not contains: " + prop);
            }else{
                newAccount[prop] = obj[prop];
            }
        }

        return newAccount;
    }

    get id(): number {
        return this._id;
    }

    get username(): string {
        return this._username;
    }

    get email(): string {
        return this._email;
    }

    get createdTime(): string {
        return this._createdTime;
    }

    get isConfirmed(): boolean {
        return this._isConfirmed;
    }

    get isTemp(): boolean {
        return this._isTemp;
    }
}