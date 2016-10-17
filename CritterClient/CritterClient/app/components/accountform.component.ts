import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: '../../templates/accountform.template.htm'
})

export class AccountFormComponent {
    user: User;
    confirmPassword: string;


    constructor(private router: Router) { }


    ngOnInit() { this.user = Application.user }

    onSubmit() {
        var self = this;

        Application.submitUserAccountUpdate(this.user).then((u: User) => {
        }).fail((error: JQueryXHR) => {
            alert("Error text received from server (do something with this later): \n\n" + error.responseText)
        });

        return false;
    }

    
}