import {Component, Output, Input, EventEmitter} from "@angular/core";
import { Comparable } from '../dtos'


@Component({
    selector: "spinner",
    template: `
<div style="width: 100%; margin-bottom: 20px; text-align: center;">
     <button (click)="increment()" class="btn btn-primary inlineblock form-control" style="width: auto;"> <i class="fa fa-plus"></i> </button>
     <input style="width: 60px;" type="text" (change)="validate()" [(ngModel)]="value" class="inlineblock form-control" /> 
    <button (click)="decrement()"  class="btn btn-primary inlineblock form-control" style=""> <i class="fa fa-minus"></i> </button>
<div>
    `

})
export class Spinner {
    @Input("maximumValue") maxVal: number; 
    @Input("minimumValue") minVal: number;
    public value: string = "0"; 

    @Output("result-out") public quantity = new EventEmitter();


    validate() {
        if (isNaN(<any>this.value)) {
            this.value = "0";
        } else {
            var val = +this.value;
            if (val < this.minVal) {
                this.value = "" +this.minVal;
            } else if (val > this.maxVal) {
                this.value = ""+this.maxVal;
            }
        }
        this.quantity.emit(+this.value);
    }

    increment() {
        this.value = ""+((+this.value)+1);
        this.validate();
    }

    decrement() {
        this.value = "" + ((+this.value) - 1);
        this.validate();
    }


}