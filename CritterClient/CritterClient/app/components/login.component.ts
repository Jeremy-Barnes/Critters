import { Component, Input, OnInit  } from '@angular/core';
import { User } from '../dtos'
import {ServiceMethods} from "../servicemethods"
import {Application} from "../appservice"

@Component({
    selector: 'login',
    templateUrl: '../../templates/login.template.htm'
})

<<<<<<< HEAD
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
=======
export class LoginComponent implements OnInit {
    user: User;
    confirmPassword: string;
    app = Application.getApp();
    ngOnInit() { this.user = this.app.user }

    onSubmit() {
        var self = this;
        Application.logIn(this.app.user).then((u: User) => {
            (<any>$("#log-in")).modal('hide'); //I'm not happy about this either.
        }).fail((error: JQueryXHR) => {
            alert("Error text received from server (do something with this later): \n\n" + error.responseText)
            self.confirmPassword = "";
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
        });
        return false;
    }
}