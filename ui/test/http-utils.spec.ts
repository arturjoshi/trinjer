import {Http, BaseRequestOptions, HttpModule, RequestMethod, Response, ResponseOptions} from "@angular/http";
import {MockBackend, MockConnection} from "@angular/http/testing";
import {TestBed, inject, async} from "@angular/core/testing";
import {HttpUtils} from "../app/services/http-utils.service";
import {TokenService} from "../app/services/token.service";
/**
 * Created by Andrew Zelenskiy on 18.01.2017.
 */

describe('HttpUtilsService', ()=>{
    let token = 'testtoken';

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

    let httpUtilsProvider = {
        provide: HttpUtils,
        useFactory: (http: Http, tokenService: TokenService) => {
            return new HttpUtils(http, tokenService);
        },
        deps: [
            Http,
            TokenService
        ]
    };

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

    beforeEach(() => {

        let storage = {'token': token};

        spyOn(localStorage, 'getItem').and.callFake((key: string, value: string) => {
            return storage[key] || null;
        });

    });

    describe('withoutToken', () => {
        it('post', async(() => {
            inject(
                [HttpUtils, MockBackend],
                (httpUtils: HttpUtils, mockBackend: MockBackend) => {
                    let testObj = {
                        test: 'test'
                    };

                    mockBackend.connections.subscribe((connection: MockConnection) => {
                        expect(connection.request.method).toEqual(RequestMethod.Post);
                        expect(connection.request.headers.get('X-Auth-Token')).toBeUndefined();

                        expect(connection.request.getBody()).toEqual(testObj);

                        connection.mockRespond(new Response(new ResponseOptions()));
                    });

                    httpUtils.makePostWithoutToken('/test', testObj).subscribe(() => {});

                });
        }));

        it('get', async(() => {
            inject([HttpUtils, MockBackend], (httpUtils: HttpUtils, mockBackend: MockBackend) => {
                mockBackend.connections.subscribe((connection: MockConnection) => {
                    expect(connection.request.method).toEqual(RequestMethod.Get);
                });

                httpUtils.makeGet('/test');
            });
        }))
})});