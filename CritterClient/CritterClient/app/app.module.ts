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
import { DashboardComponent } from './components/dashboard.component';
import { UserProfileComponent } from './components/userprofile.component';
import { MessageComponent } from './components/messages.component';
import { AutocompleteList } from './components/autocomplete'




@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        Routing
    ],
    declarations: [LoginComponent, AccountFormComponent, AppComponent, AccountCreateBasicInfoComponent,
        AccountCreateDetailsComponent, AccountCreatePetComponent, CreatePetComponent, DashboardComponent,
        UserProfileComponent, MessageComponent, AutocompleteList],
    bootstrap: [AppComponent],
    providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }]

})
export class AppModule { }