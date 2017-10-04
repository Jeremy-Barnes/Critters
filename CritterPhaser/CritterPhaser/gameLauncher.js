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
                    this.paddleSpeed = 25; //units per sec (top to bottom in 4 seconds)
                    this.upvector = 0;
                    this.downvector = 0;
                    this.ballUpV = 0; //updown
                    this.ballLeftV = 0; //leftright
                    this.ballSpeed = 35;
                    this.server = null;
                    this.server = socket;
                    if (socket != null)
                        this.setUpGameComms();
                }
                GameEngine.prototype.setUpGameComms = function () {
                    this.server.onmessage = this.handleIncomingMessage;
                    this.server.send(JSON.stringify({ startGame: true }));
                };
                GameEngine.prototype.handleIncomingMessage = function (event) {
                    if (!this.hasStarted && event.data.startTickingNow) {
                        this.hasStarted = true;
                        this.game.paused = false;
                    }
                };
                GameEngine.prototype.upCallback = function (key) {
                    if (key.isDown) {
                        this.upvector = 1;
                        this.downvector = 0;
                    }
                    else {
                        this.upvector = 0;
                    }
                };
                GameEngine.prototype.downCallback = function (key) {
                    if (key.isDown) {
                        this.upvector = 0;
                        this.downvector = 1;
                    }
                    else {
                        this.downvector = 0;
                    }
                };
                GameEngine.prototype.talkToServer = function () {
                    if (this.server != null) {
                        var cmds = [];
                        if (this.upvector) {
                            cmds.push("W");
                        }
                        if (this.downvector) {
                            cmds.push("S");
                        }
                        if (cmds.length > 0)
                            this.server.send(JSON.stringify({ commands: cmds }));
                    }
                };
                GameEngine.prototype.tickEstimate = function (elapsedSec) {
                    this.movePaddles(elapsedSec);
                    this.moveBall(elapsedSec);
                };
                GameEngine.prototype.moveBall = function (elapsedSec) {
                    this.ballLocation.x += elapsedSec * this.ballLeftV * this.ballSpeed;
                    this.ballLocation.y += elapsedSec * this.ballUpV * this.ballSpeed;
                    var screenLoc = this.unitsToPixels(this.ballLocation);
                    this.ballSprite.x = screenLoc.x;
                    this.ballSprite.y = screenLoc.y;
                    if (this.ballLocation.y <= GameEngine.BOTTOM_Y || this.ballLocation.y >= GameEngine.TOP_Y) {
                        this.ballUpV = this.ballUpV * -1;
                    }
                    if (this.ballLocation.x <= GameEngine.LEFT_X || this.ballLocation.x >= GameEngine.RIGHT_X) {
                        if ((this.ballLocation.y > this.playerYLocation - GameEngine.PADDLE_LENGTH / 2 &&
                            this.ballLocation.y < this.playerYLocation + GameEngine.PADDLE_LENGTH / 2 &&
                            this.ballLocation.x <= GameEngine.LEFT_X) ||
                            (this.ballLocation.x >= GameEngine.RIGHT_X &&
                                this.ballLocation.y > this.enemyYLocation - GameEngine.PADDLE_LENGTH / 2 &&
                                this.ballLocation.y < this.enemyYLocation + GameEngine.PADDLE_LENGTH / 2)) {
                            this.ballLeftV = this.ballLeftV * -1;
                        }
                        else {
                        }
                    }
                };
                GameEngine.prototype.movePaddles = function (elapsedSec) {
                    this.playerYLocation -= this.paddleSpeed * this.upvector * elapsedSec;
                    this.playerYLocation += this.paddleSpeed * this.downvector * elapsedSec;
                    this.playerSprite.y = this.unitsToPixelsY(this.playerYLocation);
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
                    this.tickEstimate(game.time.physicsElapsed);
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
                    var startLoc = this.unitsToPixels({ x: GameEngine.LEFT_X, y: GameEngine.TOP_Y / 2 });
                    this.playerYLocation = GameEngine.TOP_Y / 2;
                    this.playerSprite = game.add.sprite(startLoc.x, startLoc.y, 'paddle');
                    this.playerSprite.anchor.set(.5, .5);
                    this.playerSprite.scale.set(.1);
                    startLoc = this.unitsToPixels({ x: GameEngine.RIGHT_X, y: GameEngine.TOP_Y / 2 });
                    this.enemySprite = game.add.sprite(startLoc.x, startLoc.y, 'paddle');
                    this.enemySprite.anchor.set(.5, .5);
                    this.enemySprite.scale.set(.1);
                    this.ballLocation = { x: 75, y: 50 };
                    //startLoc = this.unitsToPixels(this.ballLocation)
                    this.ballSprite = game.add.sprite(startLoc.x, startLoc.y, 'ball');
                    this.ballSprite.anchor.set(.5, .5);
                    this.ballSprite.scale.set(.25);
                    this.ballUpV = -1;
                    this.ballLeftV = -1;
                    game.scale.fullScreenScaleMode = Phaser.ScaleManager.SHOW_ALL;
                    if (!this.hasStarted) {
                    }
                };
                GameEngine.RIGHT_X = 150;
                GameEngine.LEFT_X = 0;
                GameEngine.TOP_Y = 100;
                GameEngine.BOTTOM_Y = 0;
                GameEngine.PADDLE_LENGTH = 12; //units, not pixels
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