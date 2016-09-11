import { Component, Input } from '@angular/core';
import { User } from '../dtos'

@Component({
    selector: 'acct-form',
    templateUrl: '../../templates/accountform.template.htm'
})

export class AccountFormComponent {
    @Input()
    user: User;

    confirmPassword: string;
}