import { ModuleWithProviders }  from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

<<<<<<< HEAD
import { AccountCreateBasicInfoComponent }      from './components/accountcreate-basicinfo.component';
import { AccountCreateDetailsComponent }      from './components/accountcreate-userdetail.component';
import { AccountCreatePetComponent }      from './components/accountcreate-petcreate.component';
import { AppComponent }      from './components/app.component';
=======
import { AccountCreateBasicInfoComponent } from './components/accountcreate-basicinfo.component';
import { AccountCreateDetailsComponent } from './components/accountcreate-userdetail.component';
import { AccountCreatePetComponent } from './components/accountcreate-petcreate.component';
import { AccountFormComponent } from './components/accountform.component';
import { CreatePetComponent } from './components/petcreate.component';
import { DashboardComponent } from './components/dashboard.component';
import { AppComponent } from './components/app.component';
import { UserProfileComponent } from './components/userprofile.component';

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a



const appRoutes: Routes = [
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    },
    {
        path: 'home',
<<<<<<< HEAD
        component: AppComponent
=======
        component: DashboardComponent
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
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
<<<<<<< HEAD
        path: 'signUp-3',
        component: AccountCreatePetComponent
=======
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
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
    }
];

export const Routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
