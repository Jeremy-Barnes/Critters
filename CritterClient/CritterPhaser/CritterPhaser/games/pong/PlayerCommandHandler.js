System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var PlayerCommandHandler, PlayerCommandMapping;
    return {
        setters:[],
        execute: function() {
            PlayerCommandHandler = (function () {
                function PlayerCommandHandler() {
                }
                PlayerCommandHandler.createMapping = function (keyboard, keyCode, onUp, onDown, onHold, gameThis) {
                    var key = keyboard.addKey(keyCode);
                    keyboard.addKeyCapture(keyCode);
                    if (onDown) {
                        key.onDown.removeAll();
                        key.onDown.add(onDown.bind(gameThis));
                    }
                    if (onUp) {
                        key.onUp.removeAll();
                        key.onUp.add(onUp.bind(gameThis));
                    }
                    if (onHold) {
                        key.onHoldCallback = null;
                        key.onHoldCallback = onHold.bind(gameThis);
                    }
                };
                PlayerCommandHandler.createSingleMapping = function (keyboard, keyCode, handlerFunc, gameThis) {
                    PlayerCommandHandler.createMapping(keyboard, keyCode, handlerFunc, handlerFunc, handlerFunc, gameThis);
                };
                return PlayerCommandHandler;
            }());
            exports_1("PlayerCommandHandler", PlayerCommandHandler);
            PlayerCommandMapping = (function () {
                function PlayerCommandMapping() {
                }
                return PlayerCommandMapping;
            }());
        }
    }
});
//# sourceMappingURL=PlayerCommandHandler.js.map