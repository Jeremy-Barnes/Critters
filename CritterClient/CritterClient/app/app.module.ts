import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';

import { AppComponent }   from './components/app.component';
import { AccountFormComponent }   from './components/accountform.component';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [AppComponent, AccountFormComponent],
    bootstrap: [AppComponent]
})
export class AppModule { }