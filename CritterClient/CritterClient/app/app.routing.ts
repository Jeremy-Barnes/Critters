﻿import { ModuleWithProviders }  from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AccountCreateBasicInfoComponent }      from './components/accountcreate-basicinfo.component';
import { AccountCreateDetailsComponent }      from './components/accountcreate-userdetail.component';
import { AccountCreatePetComponent }      from './components/accountcreate-petcreate.component';
import { AppComponent }      from './components/app.component';



const appRoutes: Routes = [
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        component: AppComponent
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
    }
];

export const Routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);