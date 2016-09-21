import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: '../../templates/accountcreate-petcreate.template.htm'
})

export class AccountCreatePetComponent {
    user: User;
    confirmPassword: string;

    ngOnInit() { this.user = Application.user }

    onSubmit() {
        return false;
    }

    
}