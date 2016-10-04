import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: '../../templates/accountcreate-basicinfo.template.htm'
})

export class AccountCreateBasicInfoComponent {
    user: User;
    confirmPassword: string;

    constructor(private router: Router) { }

    ngOnInit() { this.user = Application.user }

    onSubmit() {
        let link = ['/signUp-2'];
        this.router.navigate(link);

        return false;
    }

    
}