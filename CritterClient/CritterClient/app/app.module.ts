import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { ReactiveFormsModule }          from '@angular/forms';

import { Routing } from './app.routing'
import { AppComponent }   from './components/app.component';
import { LoginComponent }   from './components/login.component';
import { AccountFormComponent }   from './components/accountform.component';
import { AccountCreateBasicInfoComponent }   from './components/accountcreate-basicinfo.component';
import { AccountCreatePetComponent }   from './components/accountcreate-petcreate.component';
import { AccountCreateDetailsComponent }   from './components/accountcreate-userdetail.component';
import { CreatePetComponent }   from './components/petcreate.component';



@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        Routing
    ],
    declarations: [LoginComponent, AccountFormComponent, AppComponent, AccountCreateBasicInfoComponent, AccountCreateDetailsComponent, AccountCreatePetComponent],
    bootstrap: [AppComponent],
    providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }]

})
export class AppModule { }