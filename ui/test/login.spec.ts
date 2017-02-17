import { AccountDTO } from './../src/app/models/account';
import { Response } from '@angular/http';
import { ResponseOptions, ResponseType } from '@angular/http';
import { RequestMethod } from '@angular/http';
import { MockConnection } from '@angular/http/testing';
import { AbstractControl } from '@angular/forms';
import { inject } from '@angular/core/testing';
import { MdSnackBar, MdDialog, MdDialogRef } from '@angular/material';
import { BaseRequestOptions, Http } from '@angular/http';
import { MockBackend } from '@angular/http/testing';
import { HttpUtils } from './../src/app/services/http-utils.service';
import { AccountService } from './../src/app/services/account.service';
import { TokenService } from './../src/app/services/token.service';
import { AuthenticateService } from './../src/app/login/authenticate.service';
import { ComponentFixture, TestBed, tick, fakeAsync } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '@angular/material';
import { LoginDialog } from './../src/app/login/login.dialog';


class MdDialogRefMock{
    private callback: any = null;

    constructor(){}

    setOnCloseCallback(callback: any){
        this.callback = callback;
    }

    close(result: string){
        if(result != null){
            this.callback(result);
        }
    }
}

class MdSnackBarRefMock{
    private callback: any = null;

    setOnOpenCallback(callback:any){
        this.callback = callback;
    }

    open(){
        if(this.callback != null){
            this.callback();
        }
    }
}

class MockError extends Response implements Error {
    name:any
    message:any
}

describe('Login dialog test', () => {
    let fixture: ComponentFixture<LoginDialog>;
    let loginDialog: LoginDialog;

    let providers = getProviders();

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                MaterialModule.forRoot(),
                ReactiveFormsModule,
            ],
            declarations: [LoginDialog],
            providers: [
                AuthenticateService,
                TokenService,
                AccountService,
                HttpUtils,

                providers.httpProvider,
                providers.mdDialogRefProvider,
                providers.mdSnackBarProvider,

                MockBackend,
                BaseRequestOptions
            ]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(LoginDialog);
        loginDialog = fixture.componentInstance;

        loginDialog.user.email = "";
        loginDialog.user.password = "";
        loginDialog.user.username = "";

        fixture.detectChanges();
    });

    beforeEach(() => {
        let storage = {};
        spyOn(localStorage, 'getItem').and.callFake((key: string) => {
            return storage[key] || null;
        });
        spyOn(localStorage, 'setItem').and.callFake((key: string, value: string) => {
            storage[key] = value;
        });
    });
        
    
    it("Define user model in dialog", () => {
        fixture.detectChanges();
        expect(loginDialog.user).toBeDefined();
    });
    
    describe("Form validation", () => {
        let usernameControl: AbstractControl;
        let passwordControl: AbstractControl;

        beforeEach(() => {
            passwordControl = loginDialog.loginForm.controls['password'];
            usernameControl = loginDialog.loginForm.controls['username'];
        })

        it("Empty username", () => {
            passwordControl.setValue("123");
            fixture.detectChanges();
            usernameControl.markAsDirty();
            passwordControl.markAsDirty();
            loginDialog.login();
            expect(loginDialog.formErrors.username).toEqual(loginDialog.validationMessages.username.required + " ");
            expect(loginDialog.formErrors.password).toEqual('');
        });

        it("Empty password", () => {
            usernameControl.setValue("Test username");
            fixture.detectChanges();
            usernameControl.markAsDirty();
            passwordControl.markAsDirty();
            loginDialog.login();
            expect(loginDialog.formErrors.password).toEqual(loginDialog.validationMessages.password.required + " ");
            expect(loginDialog.formErrors.username).toEqual('');
        });
    });

    describe("Parse response errors", () => {
        let usernameControl: AbstractControl;
        let passwordControl: AbstractControl;
        let mockBackend: MockBackend;

        let token: string;
        let password: string;
        let account: AccountDTO;

        let apiPath: string;

        beforeEach(() => {
            usernameControl = loginDialog.loginForm.controls["username"];
            passwordControl = loginDialog.loginForm.controls["password"];
            apiPath = "http://localhost:8080/api/authenticate/";

            token = "testtoken";
            password = "123123";
            account = AccountDTO.getFromJson({
                id: 12,
                username: "Test username",
                email: "Test@email.com",
                createdDate: new Date(),
                isConfirmed: false,
                isTemp: false
            });
        });

        beforeEach(inject([MockBackend], (MockBackend: MockBackend) => {
            mockBackend = MockBackend;
        }));

        it("Sucess log in", (done) => {
            providers.mdDialogRefProvider.useValue.setOnCloseCallback((result: string) => {
                expect(result).toEqual("Login");
                expect(localStorage.getItem("token")).toEqual(token);
                expect(localStorage.getItem("account")).toEqual(JSON.stringify(account));
                
                done();
            })

            mockBackend.connections.subscribe((connection: MockConnection) => {

                expect(connection.request.method).toEqual(RequestMethod.Post);
                expect(connection.request.url).toEqual(apiPath);

                connection.mockRespond(new Response(new ResponseOptions({
                    status: 400,
                    body: {
                        token: token,
                        account: account
                    }
                })));
            });

            usernameControl.setValue(account.username);
            passwordControl.setValue(password);

            fixture.detectChanges();

            loginDialog.login();
        });

        it("No such user", fakeAsync(() => {
            mockBackend.connections.subscribe((connection: MockConnection) => {
                connection.mockError(new MockError(new ResponseOptions({
                    type: ResponseType.Error,
                    status: 500,
                    body: "No such account"
                })))
            });

            usernameControl.setValue(account.username);
            passwordControl.setValue(password);

            fixture.detectChanges();

            loginDialog.login();

            tick(100);

            fixture.detectChanges();

            fixture.whenStable().then(() => {
                expect(loginDialog.formErrors.username).toEqual(loginDialog.validationMessages.username.wrong);
                expect(loginDialog.formErrors.password).toEqual("");     
            })
        }));

        it("Bad credentials", fakeAsync(() => {
            mockBackend.connections.subscribe((connection: MockConnection) => {
                connection.mockError(new MockError(new ResponseOptions({
                    type: ResponseType.Error,
                    status: 500,
                    body: "Bad credentials"
                })))
            });

            usernameControl.setValue(account.username);
            passwordControl.setValue(password);

            fixture.detectChanges();

            loginDialog.login();

            tick(1000);

            fixture.detectChanges();

            fixture.whenStable().then(() => {
                expect(loginDialog.formErrors.username).toEqual("");
                expect(loginDialog.formErrors.password).toEqual(loginDialog.validationMessages.password.wrong);
            })
        }))
    });
});

function getProviders(){
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

    let mdDialogRefProvider = {
        provide: MdDialogRef,
        useValue: new MdDialogRefMock()
    };

    let mdSnackBarProvider = {
        provide: MdSnackBar,
        useValue: new MdSnackBarRefMock()
    }

    return {httpProvider, mdDialogRefProvider, mdSnackBarProvider}
}
