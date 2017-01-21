import {Injectable} from "@angular/core";
import {Observable, BehaviorSubject} from "rxjs/Rx";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class TokenService{
    private key: string;
    private tokenBehavior: BehaviorSubject<string>;

    constructor(){
        this.key = 'token';
        let token = localStorage.getItem(this.key);
        this.tokenBehavior = new BehaviorSubject<string>(token);
    }

    get token(): Observable<string>{
        this.tokenBehavior.next(localStorage.getItem(this.key));
        return this.tokenBehavior.asObservable();
    }

    setToken(token: string){
        localStorage.setItem(this.key, token);
        this.tokenBehavior.next(token);
    }

    isTokenPresent(): boolean{
        return localStorage.getItem(this.key) === null;
    }

    removeToken(): void{
        localStorage.removeItem(this.key);
    }
}