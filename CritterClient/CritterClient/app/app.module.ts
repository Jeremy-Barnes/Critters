import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';

import { AppComponent }   from './components/app.component';
import { LoginComponent }   from './components/login.component';
import { AccountFormComponent }   from './components/accountform.component';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [LoginComponent, AccountFormComponent, AppComponent],
    bootstrap: [AppComponent]
})
export class AppModule { }