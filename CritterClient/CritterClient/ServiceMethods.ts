class ServiceMethods {
    static baseURL: string = "http://localhost:8080/api/critters/";
    static selectorValidator: string[];

    public static doAjax(functionName: string, functionService: string, parameters: any): JQueryPromise<any> {
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

    public static logIn(user: UserModel): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromLogin", "users", ko.mapping.toJS(user));
    }

    public static getUserFromToken(selector: string, validator: string): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromToken", "users", { selector: selector, validator: validator });
    }

    public static createUser(user: UserModel): JQueryPromise<User> {
        return ServiceMethods.doAjax("createUser", "users", ko.toJS(user));
    }

    public static changeUserInformation(user: UserModel): JQueryPromise<User> {
        return ServiceMethods.doAjax("changeUserInformation", "users", ko.mapping.toJS(user));
    }


}