﻿import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup }        from '@angular/forms';

import { User, PetColor, PetSpecies, Pet } from '../dtos'
import {Application} from "../appservice"



@Component({
    templateUrl: '../../templates/accountcreate-petcreate.template.htm'
})

export class AccountCreatePetComponent implements OnInit {
    user: User;
    colors: PetColor[];
    species: PetSpecies[];

    activeColor: string = "";
    activeColorObject: PetColor = null;
    activeSpecies: PetSpecies = null;
    petAndColorSelected: boolean = false;
    petName: string = "";
    activeSex: string = "";
    confirmedSpecies = false;
    showOptions = false;
    constructor(private router: Router) {
        var self = this;
        Application.getPetColors().done(() => {
            self.colorChange(this.colors[0]);
        }); 
        Application.getPetSpecies();
    }

    ngOnInit() {
        this.user = Application.getApp().user;
        if (this.userIsValid()) {
            this.species = Application.getApp().petSpecies;
            this.colors = Application.getApp().petColors;
        } else {
            let link = ['/signUp'];
            this.router.navigate(link);
        }
    }

    onSubmit() {
        var self = this;
        var pet = new Pet();
        pet.petName = this.petName;
        pet.petColor = this.activeColorObject;
        pet.petSpecies = this.activeSpecies;
        pet.sex = this.activeSex;

        Application.submitUserAccountCreationRequest(this.user, pet).then((u: User) => {
            let link = ['/'];
            self.router.navigate(link);
        }).fail((error: JQueryXHR) => {
            alert("Error text received from server (do something with this later): \n\n" + error.responseText)
        });

        return false;
    }

    colorChange(color: PetColor) {
        this.activeColorObject = color;
        this.activeColor = "_" + this.activeColorObject.petColorName;
        if (this.activeSpecies != null) this.petAndColorSelected = true;
    }

    onPetSelect(pet: PetSpecies) {
        this.showOptions = !this.showOptions;
        this.activeSpecies = pet;
        if (this.activeColorObject != null) this.petAndColorSelected = true;
    }

    private userIsValid(): boolean {
        return <boolean><any>(this.user.city && this.user.country &&
            this.user.emailAddress && this.user.firstName && this.user.lastName &&
            this.user.password && this.user.postcode && this.user.state && this.user.userName && this.user.birthDay && this.user.birthMonth);
    }

    confirmSpecies($event: any) {
        $event.stopPropagation();
        this.confirmedSpecies = true;
        this.showOptions = false;
    }

    showInfo($event: any) {
        (<any>$("#viewSpecies")).modal('show'); //I'm not happy about this either.
        $event.stopPropagation();
    }
}