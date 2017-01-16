/**
 * Created by Andrew Zelenskiy on 16.01.2017.
 */

export interface IAccount{
    id: number;
    username: string;
    email: string;
    createdTime: string;
    isConfirmed: boolean;
    isTemp: boolean;
}