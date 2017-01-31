/**
 * Created by xoll on 08.01.2017.
 */
import {Component} from "@angular/core";
import {MdDialogRef} from "@angular/material";

@Component({
    selector: 'registration',
    templateUrl: 'app/registration/registration.template.html',
})
export class RegistrationDialog{
    constructor(private dialogRef: MdDialogRef<RegistrationDialog>){}

    close(){
        this.dialogRef.close('Cancel');
    }
}