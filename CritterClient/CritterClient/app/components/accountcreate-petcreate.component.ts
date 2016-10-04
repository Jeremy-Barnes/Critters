import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { User } from '../dtos'
import {Application} from "../appservice"


@Component({
    templateUrl: '../../templates/accountcreate-petcreate.template.htm'
})

export class AccountCreatePetComponent implements OnInit {
    user: User;
    confirmPassword: string;

    constructor(private router: Router) { }

    ngOnInit() {
        this.user = Application.user;
        if (this.userIsValid()) {
            Application.getPetOptions(null);
        } else {
            let link = ['/signUp'];
            this.router.navigate(link);
        }
    }

    onSubmit() {
        return false;
    }

    private userIsValid(): boolean {
        return <boolean><any>(this.user.birthdate && this.user.city && this.user.country &&
            this.user.emailAddress && this.user.firstName && this.user.lastName &&
            this.user.password && this.user.postcode && this.user.state && this.user.userName);
    }

    
}