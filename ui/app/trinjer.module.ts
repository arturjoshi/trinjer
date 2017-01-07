import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app.component";
import {TrinjerRoutingModule} from "./trinjer-routing.module";
/**
 * Created by xoll on 06.01.2017.
 */


@NgModule({
    imports: [
        BrowserModule,
        TrinjerRoutingModule
    ],
    declarations: [
        AppComponent
    ],
    bootstrap: [AppComponent]
})
export class TrinjerModule{

}