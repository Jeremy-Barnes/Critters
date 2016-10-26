﻿/// <reference path="../Libraries/typings/jquery/jquery.d.ts" />
/// <reference path="../Libraries/typings/jqueryui/jqueryui.d.ts" />
import {User, Pet, PetColor, PetSpecies, CreateAccountRequest, Friendship} from './dtos'

export class ServiceMethods {
    static baseURL: string = "http://581c949b.ngrok.io/api/critters/";//"http://localhost:8080/api/critters/";
    static selectorValidator: string[];

    private static doAjax(functionName: string, functionService: string, parameters: any, type: string = "POST"): JQueryPromise<any> {
        var param = JSON.stringify(parameters);
        var settings: JQueryAjaxSettings = {
            url: ServiceMethods.baseURL + functionService + "/" + functionName,
            type: type,
            contentType: "application/json",
            
            headers: {
                
                SelectorValidator: ServiceMethods.selectorValidator ? ServiceMethods.selectorValidator[0] + ':' + ServiceMethods.selectorValidator[1] : null,
            },

            success: (json, status, args) => {
                if (args.getResponseHeader("SelectorValidator")) {
                    ServiceMethods.selectorValidator = args.getResponseHeader("SelectorValidator").split(":");
                }
            },
            data: param,
            crossDomain: true,
        };
        return jQuery.ajax(settings);
    }

    public static logIn(user: User): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromLogin", "users", user);
    }

    public static getUserFromToken(selector: string, validator: string): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromToken", "users", { selector: selector, validator: validator });
    }

    public static createUser(userCreateRequest: CreateAccountRequest): JQueryPromise<User> {
        userCreateRequest.user.birthdate = new Date(Date.parse(userCreateRequest.user.birthdate.toString()));
        return ServiceMethods.doAjax("createUser", "users", userCreateRequest);
    }

    public static createPet(pet: Pet): JQueryPromise<Pet> {
        return ServiceMethods.doAjax("addPet", "users", pet);
    }

    public static changeUserInformation(user: User): JQueryPromise<User> {
        return ServiceMethods.doAjax("changeUserInformation", "users", user);
    }

    public static getPetSpecies(): JQueryPromise<PetSpecies[]> {
        return ServiceMethods.doAjax("getPetSpecies", "meta", null, "GET");
    }

    public static getPetColors(): JQueryPromise<PetColor[]> {
        return ServiceMethods.doAjax("getPetColors", "meta", null, "GET");
    }

    public static sendFriendRequest(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("createFriendship", "friends", request);
    }
}