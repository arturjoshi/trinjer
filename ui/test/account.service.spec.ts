import {AccountService} from "../app/services/account.service";
import {TestBed, inject, async} from "@angular/core/testing";
import {AccountDTO} from "../app/models/account";
import {IAccount} from "../app/models/account.interface";
/**
 * Created by Andrew Zelenskiy on 21.01.2017.
 */
describe('AccountService test', () => {
    let accountService: AccountService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [AccountService]
        })
    });

    beforeEach(inject([AccountService], (as: AccountService) => {
        accountService = as;
    }));

    beforeEach(() => {
        let storage = {};
        spyOn(localStorage, 'setItem').and.callFake((key: string, value: string) => {
            storage[key] = value;
        });
        spyOn(localStorage, 'getItem').and.callFake((key: string) => {
            return storage[key] || null;
        });
        spyOn(localStorage, 'removeItem').and.callFake((key: string) => {
            storage[key] = null;
        });
    });

    it('Save account', async(() => {
        let account = AccountDTO.getFromJson({
            id: 122,
            username: 'Testusername',
            email: 'test@email.com',
            //TODO: Go to real date
            createdDate: 'testCreatedDate',
            isTemp: false,
            isConfirm: false
        });
        let isFirst = true;

        accountService.account.subscribe((account: IAccount) => {
            if(isFirst){
                expect(account).toBeNull();
                isFirst = false;
            }else{
                expect(account).toEqual(account);
            }
        });

        accountService.saveAccount(account);
    }));
});