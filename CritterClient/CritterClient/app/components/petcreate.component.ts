import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup }        from '@angular/forms';

import { User, PetColor, PetSpecies, Pet } from '../dtos'
import {Application} from "../appservice"



@Component({
    templateUrl: '../../templates/petcreate.template.htm'
})

export class CreatePetComponent implements OnInit {
    user: User;
    colors: PetColor[];
    species: PetSpecies[];

    activeColor: string = "";
    activeColorObject: PetColor = null;
    activeSpecies: PetSpecies = null;
    petAndColorSelected: boolean = false;
    petName: string = "";
    activeSex: string = "";

    constructor(private router: Router) { }

    ngOnInit() {
        this.user = Application.getApp().user;
    }

    onSubmit() {
        var self = this;
        var pet = new Pet();
        pet.petName = this.petName;
        pet.petColor = this.activeColorObject;
        pet.petSpecies = this.activeSpecies;
        pet.sex = this.activeSex;

        Application.submitCreatePet(pet).fail((error: JQueryXHR) => {
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
}