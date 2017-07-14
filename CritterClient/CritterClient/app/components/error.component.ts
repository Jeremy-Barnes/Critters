import { Component, Input, OnInit  } from '@angular/core';
import { User } from '../dtos'
import {ServiceMethods} from "../servicemethods"
import {Application} from "../appservice"

@Component({
    selector: 'error',
    templateUrl: '../../templates/error.template.htm'
})

export class ErrorComponent implements OnInit {
    public errorText: string = "Boy there had really better be an error message here.";
    ngOnInit() {
        Application.getApp().errorCallback = this.showErrorCallback.bind(this);

    }
    showErrorCallback(error: string) {
        if (error.includes("Operation time out")) //special case, not a real error
            return;
        this.errorText = error;
        (<any>$("#error")).modal('show'); //I'm not happy about this either.
    }
}