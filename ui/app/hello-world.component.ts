/**
 * Created by xoll on 06.01.2017.
 */
import {Component} from "@angular/core";


@Component({
    selector: 'hello-world',
    template: '<h1>Hello, {{name}}!</h1>'
})
export class HelloWorldComponent{
    name = "Trinjer";
}