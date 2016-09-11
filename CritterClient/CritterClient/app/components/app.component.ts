import { Component } from '@angular/core';
import { User } from '../dtos'

@Component({
    selector: 'my-app',
    template: '<h1>My First Angular 2 App {{seluser.userName}} <acct-form [user]="seluser"></acct-form></h1>'
    
})
export class AppComponent {
    seluser: User = {
        userID: 1,
        birthdate: null,
        city: 'Chicago',
        country: 'USA!',
        critterbuxx: 100,
        emailAddress: "",
        friends: null,
        lastName: "",
        password: "",
        postcode: "",
        salt: "",
        state: "",
        tokenSelector: "",
        tokenValidator: "",
        userName: "poshSpice",
        firstName: 'fuckFace'
    }
}
