import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: '../../templates/accountcreate-userdetail.template.htm'
})

export class AccountCreateDetailsComponent {
    user: User;
    confirmPassword: string;


    constructor(private router: Router) { }

    ngOnInit() {
        this.user = Application.getApp().user;
        if (!this.userIsValid()) {
            let link = ['/signUp'];
            this.router.navigate(link);
        }
    }

    onSubmit() {
        let link = ['/signUp-3'];
        this.router.navigate(link);

        return false;
    }

    private userIsValid(): boolean {
        return <boolean><any>(this.user.birthdate &&
            this.user.emailAddress && this.user.firstName && 
            this.user.password && this.user.userName);
    }

    
}