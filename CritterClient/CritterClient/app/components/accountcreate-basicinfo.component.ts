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
    birthDay: number;
    birthMonth: { number: number, name: string };

    months = [{ number: 1, name: "January" }, { number: 2, name: "February" }, { number: 3, name: "March" }, { number: 4, name: "April" }, //i18n? Nah!
        { number: 5, name: "May" }, { number: 6, name: "June" }, { number: 7, name: "July" }, { number: 8, name: "August" },
        { number: 9, name: "September" }, { number: 10, name: "October" }, { number: 11, name: "November" }, { number: 12, name: "December" }]
    days : number[] = [];

    constructor(private router: Router) { }

    ngOnInit() { this.user = Application.getApp().user }

    onSubmit() {
        let link = ['/signUp-2'];
        this.router.navigate(link);

        return false;
    }

    onChange(event: any) {
        this.birthMonth = event; 
        var max = 0;

        if (this.birthMonth.number == 9 || this.birthMonth.number == 4 || this.birthMonth.number == 6 || this.birthMonth.number == 11) { //30 days hath september, doing this right is too tiring and pointless.
            max = 30;
        }
        else if (this.birthMonth.number == 2) {
            max = 29;
        } else {
            max = 31;
        }
        this.days.length = 0;
        for (var i = 1; i <= max; i++) {
            this.days.push(i)
        }
    }

    onChangeDay(event: number) {
        this.user.birthDay = event;
        this.user.birthMonth = this.birthMonth.number;
    }

    
}