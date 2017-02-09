import { Router } from '@angular/router';
import { AuthGuard } from './../../services/auth-guard.service';
import { AccountDTO } from './../../models/account';
import { AccountService } from './../../services/account.service';
import { Component } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: "main",
    templateUrl: "main.template.html",
    styleUrls: ["main.css"]
})
export class MainComponent{
    account: AccountDTO;
    
    constructor(
        private accountService: AccountService, 
        private authGuard: AuthGuard,
        private router: Router
    ){
        accountService.account.subscribe((account: AccountDTO) => {
            this.account = account;
        });
    }

    logout(){
        this.authGuard.logout();
        this.router.navigateByUrl("");
    }
}