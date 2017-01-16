import {IAccount} from "../models/account.interface";
/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

export interface IProject{
    id: number;
    name: string;
    isVisible: boolean;
    owner: IAccount;
}