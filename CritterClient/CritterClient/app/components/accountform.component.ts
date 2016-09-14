import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    selector: 'acct',
    templateUrl: '../../templates/accountform.template.htm'
})

export class AccountFormComponent {
    user: User;
    confirmPassword: string;

    ngOnInit() { this.user = Application.user }

    onSubmit() {
        return false;
    }

    
}