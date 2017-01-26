import { Component, Input, OnInit } from '@angular/core';
<<<<<<< HEAD
=======
import { Router } from '@angular/router';

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
<<<<<<< HEAD
    selector: 'acct',
=======
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
    templateUrl: '../../templates/accountform.template.htm'
})

export class AccountFormComponent {
    user: User;
    confirmPassword: string;

<<<<<<< HEAD
    ngOnInit() { this.user = Application.user }

    onSubmit() {
=======

    constructor(private router: Router) { }


    ngOnInit() { this.user = Application.getApp().user }

    onSubmit() {
        var self = this;

        Application.submitUserAccountUpdate(this.user).then((u: User) => {
        }).fail((error: JQueryXHR) => {
            alert("Error text received from server (do something with this later): \n\n" + error.responseText)
        });

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
        return false;
    }

    
}