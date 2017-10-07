System.register(['./PlayerCommandHandler'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var PlayerCommandHandler_1;
    var GameLauncher, GameEngine, Paddle, Point;
    return {
        setters:[
            function (PlayerCommandHandler_1_1) {
                PlayerCommandHandler_1 = PlayerCommandHandler_1_1;
            }],
        execute: function() {
            GameLauncher = (function () {
                function GameLauncher(server) {
                    GameLauncher.webSocket = server;
                    GameLauncher.gameEngine = new GameEngine(GameLauncher.webSocket);
                    GameLauncher.game = new Phaser.Game(800, 600, Phaser.AUTO, 'critterCanvas', {
                        create: GameLauncher.gameEngine.create.bind(GameLauncher.gameEngine),
                        update: GameLauncher.gameEngine.update.bind(GameLauncher.gameEngine),
                        preload: GameLauncher.gameEngine.preload.bind(GameLauncher.gameEngine)
                    });
                }
                GameLauncher.game = null;
                GameLauncher.webSocket = null;
                return GameLauncher;
            }());
            exports_1("GameLauncher", GameLauncher);
            GameEngine = (function () {
                function GameEngine(socket) {
                    this.hasStarted = false;
                    this.tickNumber = 0;
                    this.myInstanceId = -1;
                    this.ballYVector = 0; //updown
                    this.ballXVector = 0; //leftright
                    this.ballSpeed = 1;
                    this.ballDiameter = 1;
                    this.server = null;
                    this.server = socket;
                }
                GameEngine.prototype.setUpGameComms = function () {
                    this.server.onmessage = this.handleIncomingMessage.bind(this);
                    this.server.send(JSON.stringify({ startGame: true }));
                };
                GameEngine.prototype.handleIncomingMessage = function (event) {
                    var data = JSON.parse(event.data);
                    if (!this.hasStarted && data.startTickingNow) {
                        this.myInstanceId = data.assignedInstanceId;
                        for (var i = 0; i < data.deltaObjects.length; i++) {
                            var object = data.deltaObjects[i]; //should only be a ball, but we can keep it like this and do EntityID comparisons if we expand
                            if (object.entityID == 0) {
                                this.ballLocation = { x: object.x, y: object.y };
                                this.ballXVector = object.xVector;
                                this.ballYVector = object.yVector;
                                this.ballSpeed = object.BALL_VELOCITY;
                                this.ballDiameter = object.BALL_DIAMETER;
                            }
                        }
                        for (var i = 0; i < data.deltaPlayers.length; i++) {
                            var player = data.deltaPlayers[i]; //probably only 2, but maybe spectators?
                            if (player.physicsComponent) {
                                var paddle = new Paddle();
                                paddle.instanceId = player.physicsComponent.GAME_INSTANCE_ID;
                                paddle.location = { x: player.physicsComponent.x, y: player.physicsComponent.y };
                                paddle.paddleSpeed = player.physicsComponent.PADDLE_VELOCITY;
                                paddle.PADDLE_LENGTH = player.physicsComponent.PADDLE_HEIGHT;
                                paddle.yVector = player.physicsComponent.yVector;
                                if (player.physicsComponent.GAME_INSTANCE_ID == this.myInstanceId) {
                                    this.playerPaddle = paddle;
                                }
                                else {
                                    this.enemyPaddle = paddle;
                                }
                            }
                        }
                        this.hasStarted = true;
                        this.game.paused = false;
                    }
                    else {
                        if (data.deltaObjects)
                            for (var i = 0; i < data.deltaObjects.length; i++) {
                                var object = data.deltaObjects[i]; //should only be a ball, but we can keep it like this and do EntityID comparisons if we expand
                                if (object.entityID == 0) {
                                    this.ballLocation.x = object.x;
                                    this.ballLocation.y = object.y;
                                    this.ballXVector = object.xVector;
                                    this.ballYVector = object.yVector;
                                    this.ballSpeed = object.BALL_VELOCITY;
                                    this.ballDiameter = object.BALL_DIAMETER;
                                }
                            }
                        if (data.deltaPlayers)
                            for (var i = 0; i < data.deltaPlayers.length; i++) {
                                var player = data.deltaPlayers[i]; //probably only 2, but maybe spectators?
                                if (player.physicsComponent) {
                                    if (player.physicsComponent.GAME_INSTANCE_ID == this.playerPaddle.instanceId) {
                                        this.playerPaddle.location.x = player.physicsComponent.x;
                                        this.playerPaddle.location.y = player.physicsComponent.y;
                                        this.playerPaddle.paddleSpeed = player.physicsComponent.PADDLE_VELOCITY;
                                        this.playerPaddle.PADDLE_LENGTH = player.physicsComponent.PADDLE_HEIGHT;
                                        this.playerPaddle.yVector = player.physicsComponent.yVector;
                                    }
                                    else if (player.physicsComponent.GAME_INSTANCE_ID == this.enemyPaddle.instanceId) {
                                        this.enemyPaddle.location.x = player.physicsComponent.x;
                                        this.enemyPaddle.location.y = player.physicsComponent.y;
                                        this.enemyPaddle.paddleSpeed = player.physicsComponent.PADDLE_VELOCITY;
                                        this.enemyPaddle.PADDLE_LENGTH = player.physicsComponent.PADDLE_HEIGHT;
                                        this.enemyPaddle.yVector = player.physicsComponent.yVector;
                                    }
                                }
                            }
                    }
                };
                GameEngine.prototype.upCallback = function (key) {
                    if (key.isDown) {
                        this.playerPaddle.yVector = 1;
                    }
                    else {
                        this.playerPaddle.yVector = 0;
                    }
                };
                GameEngine.prototype.downCallback = function (key) {
                    if (key.isDown) {
                        this.playerPaddle.yVector = -1;
                    }
                    else {
                        this.playerPaddle.yVector = 0;
                    }
                };
                GameEngine.prototype.talkToServer = function () {
                    if (this.server != null) {
                        var cmds = [];
                        if (this.playerPaddle.yVector == 1) {
                            cmds.push("W");
                        }
                        if (this.playerPaddle.yVector == -1) {
                            cmds.push("S");
                        }
                        if (cmds.length > 0)
                            this.server.send(JSON.stringify({ commands: cmds }));
                    }
                };
                GameEngine.prototype.tickEstimate = function (elapsedMS) {
                    this.movePaddles(elapsedMS);
                    this.moveBall(elapsedMS);
                };
                GameEngine.prototype.moveBall = function (elapsedMS) {
                    this.ballLocation.x += elapsedMS * this.ballXVector * this.ballSpeed;
                    this.ballLocation.y += elapsedMS * this.ballYVector * this.ballSpeed;
                    var screenLoc = this.unitsToPixels(this.ballLocation);
                    this.ballSprite.x = screenLoc.x;
                    this.ballSprite.y = screenLoc.y;
                    if (this.ballLocation.y <= GameEngine.BOTTOM_Y || this.ballLocation.y >= GameEngine.TOP_Y) {
                        this.ballYVector = this.ballYVector * -1;
                    }
                    //if (this.ballLocation.x <= GameEngine.LEFT_X || this.ballLocation.x >= GameEngine.RIGHT_X) {
                    //    if ((this.ballLocation.y > this.playerPaddle.location.y - GameEngine.PADDLE_LENGTH / 2 &&
                    //        this.ballLocation.y < this.playerPaddle.location.y + GameEngine.PADDLE_LENGTH / 2 &&
                    //        this.ballLocation.x <= GameEngine.LEFT_X) ||
                    //       (this.ballLocation.x >= GameEngine.RIGHT_X &&
                    //        this.ballLocation.y > this.enemyPaddle.location.y - GameEngine.PADDLE_LENGTH / 2 &&
                    //        this.ballLocation.y < this.enemyPaddle.location.y + GameEngine.PADDLE_LENGTH / 2)) {
                    //        this.ballXVector = this.ballXVector * -1;
                    //    } else {
                    //        //score!
                    //    }
                    //}
                };
                GameEngine.prototype.movePaddles = function (elapsedMS) {
                    this.playerPaddle.location.y -= this.playerPaddle.paddleSpeed * this.playerPaddle.yVector * elapsedMS;
                    this.playerPaddle.location.y += this.playerPaddle.paddleSpeed * this.playerPaddle.yVector * elapsedMS;
                    this.playerSprite.y = this.unitsToPixelsY(this.playerPaddle.location.y);
                };
                ;
                GameEngine.prototype.unitsToPixels = function (coordinate) {
                    var translation = new Point();
                    translation.x = (800 / 150) * coordinate.x; //todo remove magic numbers and replace with constants: SCREEN_WIDTH and SCREEN_HEIGHT
                    translation.y = (600 / 100) * coordinate.y;
                    return translation;
                };
                GameEngine.prototype.unitsToPixelsY = function (y) {
                    return (600 / 100) * y;
                };
                /************ PHASER METHODS ************/
                GameEngine.prototype.update = function (game) {
                    this.talkToServer();
                    this.tickEstimate(game.time.physicsElapsedMS);
                };
                GameEngine.prototype.preload = function (game) {
                    game.load.image('paddle', 'img/games/pong/item_full_Glide_Paddle.jpg');
                    game.load.image('ball', 'img/games/pong/ball.png');
                    this.game = game;
                };
                GameEngine.prototype.create = function (game) {
                    game.stage.backgroundColor = "#888";
                    PlayerCommandHandler_1.PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.W, this.upCallback, this);
                    PlayerCommandHandler_1.PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.S, this.downCallback, this);
                    this.playerSprite = game.add.sprite(0, 0, 'paddle');
                    this.playerSprite.anchor.set(.5, .5);
                    this.playerSprite.scale.set(.1);
                    this.enemySprite = game.add.sprite(0, 0, 'paddle');
                    this.enemySprite.anchor.set(.5, .5);
                    this.enemySprite.scale.set(.1);
                    this.ballSprite = game.add.sprite(0, 0, 'ball');
                    this.ballSprite.anchor.set(.5, .5);
                    this.ballSprite.scale.set(.25);
                    if (this.server != null)
                        this.setUpGameComms();
                    game.scale.fullScreenScaleMode = Phaser.ScaleManager.SHOW_ALL;
                    if (!this.hasStarted) {
                        game.paused = true;
                    }
                };
                GameEngine.RIGHT_X = 150;
                GameEngine.LEFT_X = 0;
                GameEngine.TOP_Y = 100;
                GameEngine.BOTTOM_Y = 0;
                return GameEngine;
            }());
            Paddle = (function () {
                function Paddle() {
                    this.PADDLE_LENGTH = 12; //units, not pixels
                    this.paddleSpeed = 0.025; //25 units per sec (top to bottom in 4 seconds)
                    this.yVector = 0;
                    this.instanceId = -1;
                }
                return Paddle;
            }());
            Point = (function () {
                function Point() {
                }
                return Point;
            }());
        }
    }
});
//# sourceMappingURL=gameLauncher.js.map