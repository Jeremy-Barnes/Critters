import { Component, Input, OnInit  } from '@angular/core';
import { User } from '../dtos'
import {ServiceMethods} from "../servicemethods"
import {Application} from "../appservice"

@Component({
    selector: 'login',
    templateUrl: '../../templates/login.template.htm'
})

export class LoginComponent {
    user: User;

    confirmPassword: string = "this is the logincomponent";

    ngOnInit() { this.user = Application.user }

    onSubmit(loginForm: any, $event: any) {
        var self = this;
        ServiceMethods.logIn(this.user).fail((u: User) => {
        //self.user = u;
            self.user = {
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
                userName: "testest",
                firstName: 'fuckFace'
            }
        });
        return false;
    }
}