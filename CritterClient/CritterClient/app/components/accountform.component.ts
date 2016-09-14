import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'

@Component({
    selector: 'acct',
    templateUrl: '../../templates/accountform.template.htm'
})

export class AccountFormComponent {
    @Input()
    user: User;

    ngOnInit() {}


    confirmPassword: string;
}