import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/dashboard.template.htm"
})

export class DashboardComponent {
    user: User;

    ngOnInit() { this.user = Application.user; }
}
