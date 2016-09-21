﻿/// <reference path="../Libraries/typings/jquery/jquery.d.ts" />
/// <reference path="../Libraries/typings/jqueryui/jqueryui.d.ts" />
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

    public static createUser(user: User): JQueryPromise<User> {
        user.birthdate = new Date(Date.parse(user.birthdate.toString()));
        return ServiceMethods.doAjax("createUser", "users", user);
    }

    public static changeUserInformation(user: User): JQueryPromise<User> {
        return ServiceMethods.doAjax("changeUserInformation", "users", user);
    }


}