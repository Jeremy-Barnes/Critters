System.register(['./gameLauncher'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var gameLauncher_1;
    var game;
    return {
        setters:[
            function (gameLauncher_1_1) {
                gameLauncher_1 = gameLauncher_1_1;
            }],
        execute: function() {
            game = new gameLauncher_1.GameLauncher(null);
        }
    }
});
//# sourceMappingURL=main.js.map