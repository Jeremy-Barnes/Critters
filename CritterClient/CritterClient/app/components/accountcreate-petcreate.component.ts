import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';


import { User, PetColor, PetSpecies, Pet } from '../dtos'
import {Application} from "../appservice"
import { FormGroup }        from '@angular/forms';


@Component({
    templateUrl: '../../templates/accountcreate-petcreate.template.htm'
})

export class AccountCreatePetComponent implements OnInit {
    user: User;
    confirmPassword: string;
    colors: PetColor[];
    species: PetSpecies[];
    activeColor: string = "";
    activeColorObject: PetColor = null;

    constructor(private router: Router) { }

    ngOnInit() {
        this.user = Application.user;
        if (this.userIsValid()) {
            this.species = Application.getPetSpecies(null);
            this.colors = Application.getPetColors(null);
        } else {
            let link = ['/signUp'];
            this.router.navigate(link);
        }
    }

    onSubmit() {
        return false;
    }

    onChange(color: PetColor) {
        this.activeColor = "_" + this.activeColorObject.petColorName;
    }


    private userIsValid(): boolean {
        return <boolean><any>(this.user.birthdate && this.user.city && this.user.country &&
            this.user.emailAddress && this.user.firstName && this.user.lastName &&
            this.user.password && this.user.postcode && this.user.state && this.user.userName);
    }

    
}