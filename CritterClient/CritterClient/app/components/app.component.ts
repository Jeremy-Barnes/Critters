import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    selector: 'viewport',
    templateUrl: "../../templates/app.template.htm"
})

<<<<<<< HEAD
export class AppComponent {
    user: User;

    ngOnInit() { this.user = Application.user }

    summonLogin() {
    }

=======
export class AppComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    ngOnInit() { this.user = this.app.user; prepDisplay(); }

    summonLogin() {
    }
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
}
