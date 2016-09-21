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
    confirmPassword: string;;

    ngOnInit() { this.user = Application.user }

    onSubmit() {
        var self = this;
        ServiceMethods.logIn(this.user).then((u: User) => {
            self.user = u;
        }).fail((error: JQueryXHR) => {
            alert("Error text received from server (do something with this later): \n\n" + error.responseText)
        });
        return false;
    }
}