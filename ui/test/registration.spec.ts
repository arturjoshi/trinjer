import { AbstractControl } from '@angular/forms';
import { MdSnackBar } from '@angular/material';
import { BaseRequestOptions, Http } from '@angular/http';
import { MockBackend } from '@angular/http/testing';
import { HttpUtils } from './../src/app/services/http-utils.service';
import { AccountService } from './../src/app/services/account.service';
import { TokenService } from './../src/app/services/token.service';
import { AuthenticateService } from './../src/app/login/authenticate.service';
import { RegistrationService } from './../src/app/registration/registration.service';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule, MdDialogRef } from '@angular/material';
import { RegistrationDialog } from './../src/app/registration/registration.component';
import { ComponentFixture, TestBed, async, tick, fakeAsync } from '@angular/core/testing';

fdescribe('Registration dialog test', () => {
    let fixture: ComponentFixture<RegistrationDialog>;
    let registrationDialog: RegistrationDialog;

    let providers = {
        mdDialogRefProvider: {
            provide: MdDialogRef,
            useValue: {}
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
    
    
    xdescribe('Server error handling', () => {
        
    });
});
