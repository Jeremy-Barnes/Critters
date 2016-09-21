import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: '../../templates/accountcreate-userdetail.template.htm'
})

export class AccountCreateDetailsComponent {
    user: User;
    confirmPassword: string;

    ngOnInit() { this.user = Application.user }

    onSubmit() {
        return false;
    }

    
}