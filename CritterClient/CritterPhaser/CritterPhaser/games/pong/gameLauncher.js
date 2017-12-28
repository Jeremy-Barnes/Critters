System.register(['./PlayerCommandHandler'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var PlayerCommandHandler_1;
    var GameLauncher, GameEngine, Point;
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
                    this.debugMessages = [];
                    this.ballYVector = 0; //updown
                    this.ballXVector = 0; //leftright
                    this.ballSpeed = 1;
                    this.ballDiameter = 1;
                    this.spaceBar = false;
                    this.server = null;
                    this.server = socket;
                }
                GameEngine.prototype.setUpGameComms = function () {
                    this.server.onmessage = this.handleIncomingMessage.bind(this);
                };
                GameEngine.prototype.handleIncomingMessage = function (event) {
                    var data = JSON.parse(event.data);
                    if (!this.hasStarted && data.startTickingNow) {
                        this.startedTime = data.startTime;
                        this.myInstanceId = data.assignedInstanceId;
                        for (var i = 0; i < data.deltaObjects.length; i++) {
                            var object = data.deltaObjects[i]; //should only be a ball, but we can keep it like this and do EntityID comparisons if we expand
                            if (object.ENTITY_TYPE_ID == 0) {
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
                                var paddle = player.physicsComponent;
                                if (paddle.GAME_INSTANCE_ID == this.myInstanceId) {
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
                    else if (this.hasStarted) {
                        if (data.deltaObjects)
                            for (var i = 0; i < data.deltaObjects.length; i++) {
                                var object = data.deltaObjects[i]; //should only be a ball, but we can keep it like this and do EntityID comparisons if we expand
                                if (object.ENTITY_TYPE_ID == 0) {
                                    this.debugMessages.push("WHEN DIFFERENT LOCAL X: " + this.ballLocation.x + " Y: " + this.ballLocation.y + " XV: " + this.ballXVector + " YV: " + this.ballYVector);
                                    this.debugMessages.push("WHEN DIFFERENT REMOTE X: " + object.x + " Y: " + object.y + " XV: " + object.xVector + " YV: " + object.yVector);
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
                                    var paddle = player.physicsComponent;
                                    if (paddle.GAME_INSTANCE_ID == this.playerPaddle.GAME_INSTANCE_ID) {
                                        this.playerPaddle.y = paddle.y;
                                        this.playerPaddle.PADDLE_VELOCITY = paddle.PADDLE_VELOCITY;
                                        this.playerPaddle.PADDLE_HEIGHT = paddle.PADDLE_HEIGHT;
                                        this.playerPaddle.yVector = paddle.yVector;
                                    }
                                    else {
                                        this.enemyPaddle.y = paddle.y;
                                        this.enemyPaddle.PADDLE_VELOCITY = paddle.PADDLE_VELOCITY;
                                        this.enemyPaddle.PADDLE_HEIGHT = paddle.PADDLE_HEIGHT;
                                        this.enemyPaddle.yVector = paddle.yVector;
                                    }
                                }
                            }
                    }
                    if (data.gameOver) {
                        alert("Goodbye");
                    }
                    this.text.setText("" + (data.currentTime / 1000 - this.startedTime / 1000));
                };
                GameEngine.prototype.upCallback = function (key) {
                    if (this.hasStarted)
                        if (key.isDown) {
                            this.playerPaddle.yVector = 1;
                        }
                        else {
                            this.playerPaddle.yVector = 0;
                        }
                };
                GameEngine.prototype.downCallback = function (key) {
                    if (this.hasStarted)
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
                        if (this.playerPaddle.yVector == 0) {
                            cmds.push("WS");
                        }
                        if (this.spaceBar) {
                            this.spaceBar = false;
                            cmds.push(" ");
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
                    this.ballLocation.x += (elapsedMS * this.ballXVector * this.ballSpeed);
                    this.ballLocation.y += (elapsedMS * this.ballYVector * this.ballSpeed);
                    var screenLoc = this.unitsToPixels(this.ballLocation);
                    this.ballSprite.x = screenLoc.x;
                    this.ballSprite.y = screenLoc.y;
                    if (this.ballLocation.y <= GameEngine.BOTTOM_Y || this.ballLocation.y >= GameEngine.TOP_Y) {
                        this.ballYVector = this.ballYVector * -1;
                    }
                    this.debugMessages.push("X: " + this.ballLocation.x + " Y: " + this.ballLocation.y + " XV: " + this.ballXVector + " YV: " + this.ballYVector);
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
                    this.playerPaddle.y += this.playerPaddle.yVector * this.playerPaddle.PADDLE_VELOCITY * elapsedMS;
                    this.playerSprite.y = this.unitsToPixelsY(this.playerPaddle.y);
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
                    var _this = this;
                    game.stage.backgroundColor = "#888";
                    PlayerCommandHandler_1.PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.W, this.upCallback, this);
                    PlayerCommandHandler_1.PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.S, this.downCallback, this);
                    PlayerCommandHandler_1.PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.SPACEBAR, function () { _this.spaceBar = true; }, this);
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
                    this.text = game.add.text(350, 400, "Your score:", {
                        font: "15px Arial", align: "center"
                    });
                    this.text.anchor.set(0.5);
                };
                GameEngine.RIGHT_X = 150;
                GameEngine.LEFT_X = 0;
                GameEngine.TOP_Y = 100;
                GameEngine.BOTTOM_Y = 0;
                return GameEngine;
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