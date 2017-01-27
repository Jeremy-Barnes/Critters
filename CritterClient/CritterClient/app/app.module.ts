import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
<<<<<<< HEAD
=======
import { ReactiveFormsModule }          from '@angular/forms';
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

import { Routing } from './app.routing'
import { AppComponent }   from './components/app.component';
import { LoginComponent }   from './components/login.component';
import { AccountFormComponent }   from './components/accountform.component';
import { AccountCreateBasicInfoComponent }   from './components/accountcreate-basicinfo.component';
import { AccountCreatePetComponent }   from './components/accountcreate-petcreate.component';
import { AccountCreateDetailsComponent }   from './components/accountcreate-userdetail.component';
<<<<<<< HEAD
=======
import { CreatePetComponent }   from './components/petcreate.component';
import { DashboardComponent } from './components/dashboard.component';
import { UserProfileComponent } from './components/userprofile.component';


>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a


@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        Routing
    ],
<<<<<<< HEAD
    declarations: [LoginComponent, AccountFormComponent, AppComponent, AccountCreateBasicInfoComponent, AccountCreateDetailsComponent, AccountCreatePetComponent],
    bootstrap: [AppComponent],
    providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }]
=======
    declarations: [LoginComponent, AccountFormComponent, AppComponent, AccountCreateBasicInfoComponent,
        AccountCreateDetailsComponent, AccountCreatePetComponent, CreatePetComponent, DashboardComponent,
        UserProfileComponent],
    bootstrap: [AppComponent],
    providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }]

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
})
export class AppModule { }