import {Http, BaseRequestOptions, HttpModule, RequestMethod, Response, ResponseOptions} from "@angular/http";
import {MockBackend, MockConnection} from "@angular/http/testing";
import {TestBed, inject, async} from "@angular/core/testing";
import {HttpUtils} from "../../src/app/services/http-utils.service";
import {TokenService} from "../../src/app/services/token.service";
/**
 * Created by Andrew Zelenskiy on 18.01.2017.
 */

describe('HttpUtilsService', ()=>{
    let token = 'testtoken';
    let tokenHeaderName = 'x-auth-token';

    let httpProvider = {
        provide: Http,
        useFactory: (mockBackend: MockBackend, baseRequestOptions: BaseRequestOptions) => {
            return new Http(mockBackend, baseRequestOptions);
        },
        deps: [
            MockBackend,
            BaseRequestOptions
        ]
    };


    beforeEach(() => {
        let storage = {'token': token};
        spyOn(localStorage, 'getItem').and.callFake((key: string) => {
            return storage[key] || null;
        });
        spyOn(localStorage, 'setItem').and.callFake((key: string, value: string) => {
            storage[key] = value;
        });
        spyOn(localStorage, 'removeItem').and.callFake((key: string) => {
            delete storage[key];
        });
    });

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpModule],
            providers: [
                httpProvider,
                MockBackend,
                HttpUtils,
                TokenService,
                BaseRequestOptions
            ]
        });
    });

    describe('Http request', () => {
        let httpUtils: HttpUtils;
        let tokenService: TokenService;
        let mockBackend: MockBackend;

        beforeEach(inject(
            [HttpUtils, TokenService, MockBackend],
            (httpUtilsInput: HttpUtils, tokenServiceInput: TokenService, mockBackendInput: MockBackend) => {
                httpUtils = httpUtilsInput;
                tokenService = tokenServiceInput;
                mockBackend = mockBackendInput;
            })
        );

        it('post without token', async(() => {
            //Check request options
            mockBackend.connections.subscribe((connection: MockConnection) => {
                expect(connection.request.method).toEqual(RequestMethod.Post);
                expect(connection.request.headers.get(tokenHeaderName)).toBeNull();

                let jsonBody = JSON.parse(connection.request.getBody());

                expect(jsonBody).toEqual({});

                connection.mockRespond(new Response(new ResponseOptions()));
            });

            tokenService.removeToken();

            httpUtils.makePostWithoutToken('/test', {}).subscribe();
        }));

        it('Post with token', async(() => {
            mockBackend.connections.subscribe((connection: MockConnection) => {
                expect(connection.request.method).toEqual(RequestMethod.Post);
                expect(connection.request.headers.get(tokenHeaderName)).toEqual(token);

                let jsonBody = JSON.parse(connection.request.getBody());

                expect(jsonBody).toEqual({});

                connection.mockRespond(new Response(new ResponseOptions()));
            });

            tokenService.setToken(token);

            httpUtils.makePost('/test', {}).subscribe()
        }));
    });
});