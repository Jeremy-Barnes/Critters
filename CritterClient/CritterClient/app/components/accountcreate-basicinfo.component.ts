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

<<<<<<< HEAD
    ngOnInit() { this.user = Application.user }
=======
    ngOnInit() { this.user = Application.getApp().user }
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

    onSubmit() {
        let link = ['/signUp-2'];
        this.router.navigate(link);

        return false;
    }

    
}