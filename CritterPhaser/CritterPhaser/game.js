System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Game;
    return {
        setters:[],
        execute: function() {
            Game = (function () {
                function Game() {
                }
                Game.prototype.create = function () {
                    alert('boorp');
                };
                Game.prototype.update = function () {
                };
                return Game;
            }());
            exports_1("Game", Game);
        }
    }
});
//# sourceMappingURL=game.js.map