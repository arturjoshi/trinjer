import { inject } from '@angular/core/testing';
import { MdSnackBar, MdDialog, MdDialogRef } from '@angular/material';
import { BaseRequestOptions, Http } from '@angular/http';
import { MockBackend } from '@angular/http/testing';
import { HttpUtils } from './../src/app/services/http-utils.service';
import { AccountService } from './../src/app/services/account.service';
import { TokenService } from './../src/app/services/token.service';
import { AuthenticateService } from './../src/app/login/authenticate.service';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '@angular/material';
import { LoginDialog } from './../src/app/login/login.dialog';

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
        let usernameControl;
        let passwordControl;

        beforeEach(() => {
            loginDialog.user.email = "";
            loginDialog.user.password = "";
            loginDialog.user.username = "";
        });
        
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
    })
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
        useValue: {}
    };

    let mdSnackBarProvider = {
        provide: MdSnackBar,
        useValue: {}
    }

    return {httpProvider, mdDialogRefProvider, mdSnackBarProvider}
}