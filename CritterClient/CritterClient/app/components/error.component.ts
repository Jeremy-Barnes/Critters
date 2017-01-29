﻿import { Component, Input, OnInit  } from '@angular/core';
import { User } from '../dtos'
import {ServiceMethods} from "../servicemethods"
import {Application} from "../appservice"

@Component({
    selector: 'login',
    templateUrl: '../../templates/error.template.htm'
})

export class ErrorComponent implements OnInit {
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
        });
        return false;
    }
}