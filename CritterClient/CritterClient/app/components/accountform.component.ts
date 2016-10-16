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
        return false;
    }

    
}