import { Response, ResponseOptions } from '@angular/http';
import { AccountDTO } from './../src/app/models/account';
import { RegistrationUser } from './../src/app/models/registration-user.model';
import { AbstractControl } from '@angular/forms';
import { MdSnackBar } from '@angular/material';
import { BaseRequestOptions, Http, ResponseType, RequestMethod } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { HttpUtils } from './../src/app/services/http-utils.service';
import { AccountService } from './../src/app/services/account.service';
import { TokenService } from './../src/app/services/token.service';
import { AuthenticateService } from './../src/app/login/authenticate.service';
import { RegistrationService } from './../src/app/registration/registration.service';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule, MdDialogRef } from '@angular/material';
import { RegistrationDialog } from './../src/app/registration/registration.component';
import { ComponentFixture, TestBed, async, tick, fakeAsync, inject } from '@angular/core/testing';

class MdDialogRefMock{
    private callback: any;

    close(result: string){
        if(this.callback != null){
            this.callback(result);
        }
    }

    setCloseCallback(callback: any){
        this.callback = callback;
    }
}

describe('Registration dialog test', () => {
    let fixture: ComponentFixture<RegistrationDialog>;
    let registrationDialog: RegistrationDialog;

    let providers = {
        mdDialogRefProvider: {
            provide: MdDialogRef,
            useValue: new MdDialogRefMock()
        },
        mdSnackBarProvider: {
            provide: MdSnackBar,
            useValue: {}
        }
    }

    
    beforeEach(async(() => {
        let httpProvider = {
            provide: Http,
            useFactory: (mockBackend: MockBackend, baseRequestOptions: BaseRequestOptions) => {
                return new Http(mockBackend, baseRequestOptions);
            },
            deps: [
                MockBackend,
                BaseRequestOptions
            ]
        }

        TestBed.configureTestingModule({
            imports: [
                MaterialModule.forRoot(),
                ReactiveFormsModule
            ],
            declarations: [RegistrationDialog],
            providers: [
                RegistrationService,
                AuthenticateService,
                TokenService,
                AccountService,
                HttpUtils,

                httpProvider,
                providers.mdDialogRefProvider,
                providers.mdSnackBarProvider,

                MockBackend,
                BaseRequestOptions
            ]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(RegistrationDialog);
        registrationDialog = fixture.componentInstance;

        registrationDialog.user.email = "";
        registrationDialog.user.password = "";
        registrationDialog.user.username = "";
        registrationDialog.user.username = "";

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
    })
    
    
    describe('Form validation', () => {
        let usernameControl: AbstractControl;
        let emailControl: AbstractControl;
        let passwordControl: AbstractControl;
        let passwordConfirmationControl: AbstractControl;

        beforeEach(() => {
            fixture = TestBed.createComponent(RegistrationDialog);
            registrationDialog = fixture.componentInstance;

            registrationDialog.user.email = "";
            registrationDialog.user.password = "";
            registrationDialog.user.username = "";
            registrationDialog.user.username = "";

            fixture.detectChanges();
        })

        beforeEach(() => {
            usernameControl = registrationDialog.registrationForm.controls['username'];
            emailControl = registrationDialog.registrationForm.controls['email'];
            passwordControl = registrationDialog.registrationForm.controls['password'];
            passwordConfirmationControl = registrationDialog.registrationForm.controls['passwordConfirm']
        });
        
        it("Empty form", () => {
            usernameControl.markAsDirty();
            emailControl.markAsDirty();
            passwordControl.markAsDirty();
            passwordConfirmationControl.markAsDirty();

            registrationDialog.registration();

            //No need test any field becourse algoritm manipulate with array
            for(let field in registrationDialog.formErrors){
                expect(registrationDialog.formErrors[field]).toEqual(registrationDialog.validationMessages[field].required);
            }
        })

        it("Passwords are not equal", () => {
            passwordControl.markAsDirty();            
            passwordControl.setValue("123123");

            fixture.detectChanges();
            expect(registrationDialog.formErrors.passwordConfirm).toEqual("");

            passwordConfirmationControl.markAsDirty();
            passwordConfirmationControl.setValue("123");

            fixture.detectChanges();

            fixture.whenStable().then(() => {
                expect(registrationDialog.formErrors.passwordConfirm).toEqual(registrationDialog.validationMessages.passwordConfirm.incorrect + " ");
                expect(registrationDialog.formErrors.password).toEqual("")
            })
        });

        it("Change password when confirm equal", () => {
            let password = "123123";
            passwordControl.markAsDirty();
            passwordControl.setValue(password);
            passwordConfirmationControl.markAsDirty();
            passwordConfirmationControl.setValue(password);

            fixture.detectChanges();

            expect(registrationDialog.formErrors.password).toEqual("");
            expect(registrationDialog.formErrors.passwordConfirm).toEqual("");

            passwordControl.setValue(password +  "1");

            fixture.detectChanges();

            expect(registrationDialog.formErrors.password).toEqual(registrationDialog.validationMessages.password.incorrect + " ");
        });
    });
    
    describe("Sucess registration", () => {
        let mockBackend: MockBackend;

        beforeEach(inject([MockBackend], (_mockBackend: MockBackend) => {
            mockBackend = _mockBackend;
        }));

        it("Sucess registration and login", fakeAsync(() => {
            let token = "testtoken";
            let user = RegistrationUser.getNewRegistrationUser();
            user.email = "test@email.com";
            user.password = "123123";
            user.passwordConfirm = "123123";
            user.username = "testusername";
            
            let account = new AccountDTO();
            account.id = 12;
            account.email = user.email;
            account.createdTime = new Date().toISOString();
            account.username = user.username;
            account.isConfirmed = false;
            account.isTemp = false;

            let isClosed = false;

            providers.mdDialogRefProvider.useValue.setCloseCallback((message: string) => {
                expect(message).toEqual("Registration");
                expect(localStorage.getItem("token")).toEqual(token);
                expect(localStorage.getItem("account")).toEqual(JSON.stringify(account));
                isClosed = true;
            });

            for(let field in registrationDialog.registrationForm.controls){
                registrationDialog.registrationForm.controls[field].setValue(user[field]);
                registrationDialog.registrationForm.controls[field].markAsDirty();
            }

            fixture.detectChanges();
            for(let error in registrationDialog.formErrors){
                expect(registrationDialog.formErrors[error]).toEqual("");
            }

            registrationDialog.registration();

            tick();

            expect(isClosed).toBeTruthy();

            mockBackend.connections.subscribe((mockConnection: MockConnection) => {
                expect(mockConnection.request.method).toEqual(RequestMethod.Post);
                expect(mockConnection.request.url).toEqual("http://localhost:8080/register/");
                expect(mockConnection.request.getBody()).toEqual(user);

                mockConnection.mockRespond(new Response(new ResponseOptions({
                    status: 200,
                    body: {
                        account: account,
                        token: token
                    }
                })));
            })
        }));
    })
    
    xdescribe('Server error handling', () => {
        
    });
});
