import {TestBed, inject, async} from "@angular/core/testing";
import {TokenService} from "../src/app/services/token.service";
/**
 * Created by Andrew Zelenskiy on 21.01.2017.
 */

describe('TokenService', () => {
    let tokenService: TokenService = null;

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

        TestBed.configureTestingModule({
            providers: [TokenService]
        });
    });


    describe('Token test', ()=> {

        beforeEach(inject([TokenService], (tokenSer: TokenService) => {
            tokenService = tokenSer;
        }));

        describe('Without token', () => {
            it('subscribe without token', async(() => {
                tokenService.token.subscribe((token: string) => {
                    expect(token).toBeNull();
                });
            }));
        });

        describe("Subscribe on token and set it", () => {
            it('subscribe on token', async(() => {
                let isFirst = true;
                let expectedToken = "expectedToken";

                tokenService.token.subscribe((token: string) => {
                    if(isFirst){
                        expect(token).toBeNull();
                        isFirst = false;
                    }else{
                        expect(token).toEqual(expectedToken);
                    }
                });

                tokenService.setToken(expectedToken);
            }));
        });
    })
});