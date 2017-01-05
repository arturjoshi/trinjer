import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {HelloWorldComponent} from "./hello-world.component";
/**
 * Created by xoll on 06.01.2017.
 */


@NgModule({
    imports: [ BrowserModule],
    declarations: [HelloWorldComponent],
    bootstrap: [HelloWorldComponent]
})
export class TrinjerAppModule{

}