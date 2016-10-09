import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup }        from '@angular/forms';

import { User, PetColor, PetSpecies, Pet } from '../dtos'
import {Application} from "../appservice"



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
    activeSpecies: PetSpecies = null;
    petAndColorSelected: boolean = false;
    petName: string = "";

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
        alert("Successful account creation");

        var self = this;
        var pet = new Pet();
        pet.petName = this.petName;
        pet.petColor = this.activeColorObject;
        pet.petSpecies = this.activeSpecies
        Application.submitUserAccountCreationRequest(this.user, pet).then((u: User) => {
            Application.user = u;
            let link = ['/'];
            self.router.navigate(link);
        }).fail((error: JQueryXHR) => {
            alert("Error text received from server (do something with this later): \n\n" + error.responseText)
        });

        return false;
    }

    onChange(color: PetColor) {
        this.activeColor = "_" + this.activeColorObject.petColorName;
        if (this.activeSpecies != null) this.petAndColorSelected = true;
    }

    onPetSelect(pet: PetSpecies) {
        this.activeSpecies = pet;
        if (this.activeColor != null) this.petAndColorSelected = true;
    }

    private userIsValid(): boolean {
        return <boolean><any>(this.user.birthdate && this.user.city && this.user.country &&
            this.user.emailAddress && this.user.firstName && this.user.lastName &&
            this.user.password && this.user.postcode && this.user.state && this.user.userName);
    }

    
}