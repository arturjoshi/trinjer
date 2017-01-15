import {TestBed, inject} from "@angular/core/testing";
import {
    HttpModule, Http, BaseRequestOptions, ConnectionBackend, Connection, RequestMethod,
    Response, ResponseOptions
} from "@angular/http";
import {TokenService} from "../app/services/token.service";
import {AuthenticateService} from "../app/login/authenticate.service";
import {HttpUtils} from "../app/services/http-utils.service";
import {MockBackend, MockConnection} from "@angular/http/testing";
import {LoginUser} from "../app/models/login-user.model";
/**
 * Created by xoll on 15.01.2017.
 */

describe('AuthenticateService', () => {
    beforeEach(() => {
        let httpUtilsProvider = {
            provide: HttpUtils,
            useFactory: (http: Http, tokenService: TokenService) => {
                return new HttpUtils(http, tokenService);
            },
            deps: [
                Http, TokenService
            ]
        };

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

        TestBed.configureTestingModule({
            imports: [HttpModule],
            providers: [
                TokenService,
                AuthenticateService,
                httpProvider,
                httpUtilsProvider,
                MockBackend,
                BaseRequestOptions
            ]
        });

        let storage = {};

        spyOn(localStorage, 'setItem').and.callFake((key: string, value: string) => {
            return storage[key] = <string>value;
        });

        spyOn(localStorage, 'getItem').and.callFake((key:string) => {
            return storage[key] || null;
        });
    });

    describe('Authentication', () => {
        it('should be authenticate',
            inject([AuthenticateService, MockBackend], (
                authenticateService: AuthenticateService,
                mockBackend: MockBackend) => {

                let token = "testtoken";

                mockBackend.connections.subscribe((connection: MockConnection) => {
                    expect(connection.request.method).toEqual(RequestMethod.Post);

                    let body = JSON.parse(connection.request.text());

                    expect(body.username).toBeDefined();
                    expect(body.password).toBeDefined();
                    expect(body.email).toEqual('');

                    connection.mockRespond(new Response(new ResponseOptions({
                        body: {token: token},
                        status: 200
                    })));

                });

                let user = LoginUser.getNewLoginUser();
                user.username = "Unit user";
                user.password = "password";
                user.email = '';

                authenticateService.authenticate(user).subscribe((user) => {
                    expect(user).toBeDefined();
                    expect(localStorage.getItem('token')).toBeDefined();
                });
            }))
    });
});