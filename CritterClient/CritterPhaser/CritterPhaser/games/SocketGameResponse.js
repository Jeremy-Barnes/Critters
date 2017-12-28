System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var SocketGameResponse, Player, GameObject, User;
    return {
        setters:[],
        execute: function() {
            SocketGameResponse = (function () {
                function SocketGameResponse() {
                }
                return SocketGameResponse;
            }());
            exports_1("SocketGameResponse", SocketGameResponse);
            Player = (function () {
                function Player() {
                }
                return Player;
            }());
            exports_1("Player", Player);
            GameObject = (function () {
                function GameObject() {
                }
                return GameObject;
            }());
            exports_1("GameObject", GameObject);
            User = (function () {
                function User() {
                    this.userID = 0;
                    this.userName = "";
                    this.firstName = "";
                    this.lastName = "";
                    this.userImagePath = "";
                }
                return User;
            }());
            exports_1("User", User);
        }
    }
});
//# sourceMappingURL=SocketGameResponse.js.map