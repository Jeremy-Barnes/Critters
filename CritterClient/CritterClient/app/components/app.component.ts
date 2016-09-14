import { Component } from '@angular/core';
import { User } from '../dtos'
import {ServiceMethods} from "../servicemethods"

@Component({
    selector: 'my-app',
    template: '<h1>My First Angular 2 App {{seluser.userName}} <acct [user]="seluser"></acct-form></h1>'
    
})
export class AppComponent {
    
}
