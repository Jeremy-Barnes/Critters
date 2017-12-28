System.register(['../SocketGameResponse'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __extends = (this && this.__extends) || function (d, b) {
        for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
    var SocketGameResponse_1;
    var PongBall, PongPaddle;
    return {
        setters:[
            function (SocketGameResponse_1_1) {
                SocketGameResponse_1 = SocketGameResponse_1_1;
            }],
        execute: function() {
            PongBall = (function (_super) {
                __extends(PongBall, _super);
                function PongBall() {
                    _super.apply(this, arguments);
                }
                return PongBall;
            }(SocketGameResponse_1.GameObject));
            exports_1("PongBall", PongBall);
            PongPaddle = (function (_super) {
                __extends(PongPaddle, _super);
                function PongPaddle() {
                    _super.apply(this, arguments);
                }
                return PongPaddle;
            }(SocketGameResponse_1.GameObject));
            exports_1("PongPaddle", PongPaddle);
        }
    }
});
//# sourceMappingURL=PongObjects.js.map