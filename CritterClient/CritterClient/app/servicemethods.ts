﻿﻿/// <reference path="../Libraries/typings/jquery/jquery.d.ts" />
/// <reference path="../Libraries/typings/jqueryui/jqueryui.d.ts" />
import {User, Pet, PetColor, PetSpecies, CreateAccountRequest, Friendship, Message, Notification, Store} from './dtos'

export class ServiceMethods {
    static baseURL: string = "http://localhost:8080/api/critters/";;//"http://581c949b.ngrok.io/api/critters/";//"http://localhost:8080/api/critters/";
    static selectorValidator: string[];
    static jsessionID: string = null;

    private static doAjax(functionName: string, functionService: string, parameters: any, type: string = "POST"): JQueryPromise<any> {
        var param = JSON.stringify(parameters);
        var pathParams = type == "GET" && parameters != null ? "/" + param : "";
        var settings: JQueryAjaxSettings = {
            url: ServiceMethods.baseURL + functionService + "/" + functionName + pathParams + (ServiceMethods.jsessionID != null ? ";jsessionid=" + ServiceMethods.jsessionID : ""),
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
            success: (json, status, args) => {
                if (args.getResponseHeader("SelectorValidator")) {
                    ServiceMethods.selectorValidator = args.getResponseHeader("SelectorValidator").split(":");
                }
                if (args.getResponseHeader("JSESSIONID")) {
                    ServiceMethods.jsessionID = args.getResponseHeader("JSESSIONID");
                }
            },
            data: type == "POST" ? param : "",
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

    public static getUserFromID(id: number): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromID", "users", id, "GET");
    }

    public static startLongPolling(): JQueryPromise<Notification> {
        return ServiceMethods.doAjax("pollForNotifications", "meta", null, "GET");
    }

    /************** Friend Stuff **************/
    public static sendFriendRequest(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("createFriendship", "friends", request);
    }

    public static respondToFriendRequest(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("respondToFriendRequest", "friends", request);
    }

    public static cancelFriendRequest(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("cancelFriendRequest", "friends", request);
    }

    public static deleteFriendship(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("deleteFriendship", "friends", request);
    }

    /************** Store Stuff **************/
    public static getStorefront(request: Store): JQueryPromise<void> {
        return ServiceMethods.doAjax("getStorefront", "commerce", request);
    }

    /************** Chat Stuff **************/
    public static sendMessage(request: Message): JQueryPromise<void> {
        return ServiceMethods.doAjax("sendMessage", "chat", request);
    }

    /************** Inventory Stuff **************/
    public static getInventory(request: User): JQueryPromise<void> {
        return ServiceMethods.doAjax("getInventory", "users", request);
    }
}
