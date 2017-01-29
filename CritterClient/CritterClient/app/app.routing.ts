import { NgModule }             from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AccountCreateBasicInfoComponent } from './components/accountcreate-basicinfo.component';
import { AccountCreateDetailsComponent } from './components/accountcreate-userdetail.component';
import { AccountCreatePetComponent } from './components/accountcreate-petcreate.component';
import { AccountFormComponent } from './components/accountform.component';
import { CreatePetComponent } from './components/petcreate.component';
import { DashboardComponent } from './components/dashboard.component';
import { AppComponent } from './components/app.component';
import { UserProfileComponent } from './components/userprofile.component';
import { MessageComponent } from './components/messages.component';



const appRoutes: Routes = [
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        component: DashboardComponent
    },
    {
        path: 'signUp',
        component: AccountCreateBasicInfoComponent
    },
    {
        path: 'signUp-2',
        component: AccountCreateDetailsComponent
    },
    {
       path: 'signUp-3',
       component: AccountCreatePetComponent
    },
    {
        path: 'account',
        component: AccountFormComponent
    },
    {
        path: 'newpet',
        component: CreatePetComponent
    },
    {
        path: 'viewUser/:id',
        component: UserProfileComponent
    },
    {
        path: 'messages',
        component: MessageComponent
    }
];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }