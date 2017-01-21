import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs/Rx";
/**
 * Created by xoll on 08.01.2017.
 */

@Injectable()
export class TokenService{
    private key: string;

    getToken(): string{
        return null;
    }

    saveToken(token: string): void{
        console.log(token);
    }

    removeToken(): void{
        console.log("Delete token");
    }
}