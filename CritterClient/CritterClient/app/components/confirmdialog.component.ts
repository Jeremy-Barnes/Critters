import { Component, Input, OnInit  } from '@angular/core';
import { User, UINotification } from '../dtos'
import {ServiceMethods} from "../servicemethods"
import {Application} from "../appservice"

@Component({
    selector: 'confirmDialog',
    templateUrl: '../../templates/confirmdialog.template.htm'
})

export class ConfirmDialogComponent implements OnInit {
    note: UINotification = new UINotification();
    result: JQueryDeferred<boolean>;
    ngOnInit() { 
        Application.getApp().showDialogCallback = this.showDialog.bind(this);
    }
    
    showDialog(title: string, body: string, customBodyHTML: string, dangerButtonText: string, noButtonText: string) {
        this.note = new UINotification();
        this.note.title = title;
        this.note.body = body;
        this.note.customBodyHTML = customBodyHTML;
        this.note.dangerButtonText = dangerButtonText;
        this.note.noButtonText = noButtonText;
        (<any>$("#confirmDialog")).modal('show'); //I'm not happy about this either.
        this.result = $.Deferred();
        return this.result.promise();
    }

    respond(accept: boolean) {
        this.result.resolve(accept);
        (<any>$("#confirmDialog")).modal('hide'); //I'm not happy about this either.

    }
}
