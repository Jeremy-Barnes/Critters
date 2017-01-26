﻿/// <reference path="../Libraries/typings/jquery/jquery.d.ts" />
/// <reference path="../Libraries/typings/jqueryui/jqueryui.d.ts" />
<<<<<<< HEAD
import {User} from './dtos'

export class ServiceMethods {
    static baseURL: string = "http://c96979c7.ngrok.io/api/critters/";//"http://localhost:8080/api/critters/";
    static selectorValidator: string[];

    private static doAjax(functionName: string, functionService: string, parameters: any): JQueryPromise<any> {
        var param = JSON.stringify(parameters);
        var settings: JQueryAjaxSettings = {
            url: ServiceMethods.baseURL + functionService + "/" + functionName,
            type: "POST",
            contentType: "application/json",
            headers: {
                SelectorValidator: ServiceMethods.selectorValidator ? ServiceMethods.selectorValidator[0] + ':' + ServiceMethods.selectorValidator[1] : null,
            },

=======
import {User, Pet, PetColor, PetSpecies, CreateAccountRequest, Friendship} from './dtos'

export class ServiceMethods {
    static baseURL: string = "http://581c949b.ngrok.io/api/critters/";// "http://localhost:8080/api/critters/"; //"http://581c949b.ngrok.io/api/critters/";//"http://localhost:8080/api/critters/";
    static selectorValidator: string[];
    static jsessionID: string;

    private static doAjax(functionName: string, functionService: string, parameters: any, type: string = "POST"): JQueryPromise<any> {
        var param = JSON.stringify(parameters);
        var pathParams = type == "GET" && parameters != null ? "/" + param : "";
        var settings: JQueryAjaxSettings = {
            url: ServiceMethods.baseURL + functionService + "/" + functionName + pathParams,
            type: type,
            contentType: "application/json",
            xhrFields: {
                withCredentials: true
            },

            headers: {
                Cookie: ServiceMethods.jsessionID,
            },
            beforeSend: (xhr: any) => {  
                xhr.setRequestHeader('Cookie', ServiceMethods.jsessionID);
            },
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
            success: (json, status, args) => {
                if (args.getResponseHeader("SelectorValidator")) {
                    ServiceMethods.selectorValidator = args.getResponseHeader("SelectorValidator").split(":");
                }
<<<<<<< HEAD
            },
            data: param,
=======
                if (args.getResponseHeader("JSESSIONID")) {
                    ServiceMethods.jsessionID = args.getResponseHeader("JSESSIONID");
                }
            },
            data: type == "POST" ? param : "",
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
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

<<<<<<< HEAD
    public static createUser(user: User): JQueryPromise<User> {
        user.birthdate = new Date(Date.parse(user.birthdate.toString()));
        return ServiceMethods.doAjax("createUser", "users", user);
=======
    public static createUser(userCreateRequest: CreateAccountRequest): JQueryPromise<User> {
        userCreateRequest.user.birthdate = new Date(Date.parse(userCreateRequest.user.birthdate.toString()));
        return ServiceMethods.doAjax("createUser", "users", userCreateRequest);
    }

    public static createPet(pet: Pet): JQueryPromise<Pet> {
        return ServiceMethods.doAjax("addPet", "users", pet);
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
    }

    public static changeUserInformation(user: User): JQueryPromise<User> {
        return ServiceMethods.doAjax("changeUserInformation", "users", user);
    }

<<<<<<< HEAD

}
=======
    public static getPetSpecies(): JQueryPromise<PetSpecies[]> {
        return ServiceMethods.doAjax("getPetSpecies", "meta", null, "GET");
    }

    public static getPetColors(): JQueryPromise<PetColor[]> {
        return ServiceMethods.doAjax("getPetColors", "meta", null, "GET");
    }

    public static sendFriendRequest(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("createFriendship", "friends", request);
    }

    public static getUserFromID(id: number): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromID", "users", id, "GET");
    }
}
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
