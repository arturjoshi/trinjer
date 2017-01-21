import {Validator, AbstractControl, NG_VALIDATORS} from "@angular/forms";
import {Directive, forwardRef, Attribute} from "@angular/core";
/**
 * Created by xoll on 08.01.2017.
 */

@Directive({
    selector: '[validateEqual][formControlName],[validateEqual][formControl],[validateEqual][ngModel]',
    providers: [
        {provide: NG_VALIDATORS, useExisting: forwardRef(() => EqualValidatorDirective), multi: true}
    ]
})
export class EqualValidatorDirective implements Validator{
    constructor(
        @Attribute('validateEqual') public validateEqual: string,
        @Attribute('reverse') public reverse: any
    ){
        //Remove this shit
        if(!this.reverse)
            this.reverse = false;
        else
            this.reverse = this.reverse === 'true';
    }

    //TODO: refactoring
    validate(c: AbstractControl): {[key: string]: any} {
        let value = c.value;

        let expected = c.root.get(this.validateEqual);

        if(expected && value !== expected.value && !this.reverse)
            return{validateEqual: false};

        if(expected && expected.value === value && this.reverse){
            delete expected.errors['validateEqual'];
            if(!Object.keys(expected.errors).length)
                expected.setErrors(null);
        }

        if(expected && value !== expected.value && this.reverse)
            expected.setErrors({validateEqual: false});

        return null;
    }

}