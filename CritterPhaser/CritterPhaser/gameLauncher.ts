import {PlayerCommandHandler} from './PlayerCommandHandler'

export class GameLauncher {

    constructor(server: WebSocket) {
        GameLauncher.webSocket = server;
        GameLauncher.gameEngine = new GameEngine(GameLauncher.webSocket);
        GameLauncher.game = new Phaser.Game(800, 600, Phaser.AUTO, 'critterCanvas', {
            create: GameLauncher.gameEngine.create.bind(GameLauncher.gameEngine),
            update: GameLauncher.gameEngine.update.bind(GameLauncher.gameEngine),
            preload: GameLauncher.gameEngine.preload.bind(GameLauncher.gameEngine)
        });
    }
    private static game: Phaser.Game = null;
    private static gameEngine: GameEngine;
    private static webSocket: WebSocket = null;
}

class GameEngine {

    public hasStarted: boolean = false;
    public iAmHost: boolean;
    public tickNumber: number = 0;
    public game: Phaser.Game;

    public setUpGameComms() {
        this.server.onmessage = this.handleIncomingMessage;
        this.server.send(JSON.stringify({ startGame: true }));
    }

    public handleIncomingMessage(event: MessageEvent) {
        if (!this.hasStarted && event.data.startTickingNow) {
            this.hasStarted = true;
            this.game.paused = false;
        } else {
      
        }
    }

    public static RIGHT_X : number = 150;
    public static LEFT_X: number = 0;
    public static TOP_Y: number = 100;
    public static BOTTOM_Y: number = 0;
    public static PADDLE_LENGTH: number = 12;//units, not pixels

    public paddleSpeed: number = 25; //units per sec (top to bottom in 4 seconds)
    public upvector: number = 0;
    public downvector: number = 0;

    //ball data
    public ballLocation: Point;
    public ballSprite: Phaser.Sprite;
    public ballUpV: number = 0; //updown
    public ballLeftV: number = 0; //leftright
    public ballSpeed: number = 35;

    //player data
    public playerSprite: Phaser.Sprite;

    //enemy data
    public enemySprite: Phaser.Sprite;

    public keyboard: Phaser.Keyboard;
    public mouse: Phaser.Mouse;

    server : WebSocket = null;

    public constructor(socket: WebSocket) {
        this.server = socket;
        if(socket != null)
        this.setUpGameComms();
    }

    public upCallback(key: Phaser.Key) {
        if (key.isDown) {
            this.upvector = 1;
            this.downvector = 0;
        } else {
            this.upvector = 0;
        }
    }

    public downCallback(key: Phaser.Key) {
        if (key.isDown) {
            this.upvector = 0;
            this.downvector = 1;
        } else {
            this.downvector = 0;
        }
    }

    public talkToServer() {
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
    }

    public tickEstimate(elapsedSec: number) {
        this.movePaddles(elapsedSec);
        this.moveBall(elapsedSec);
    }

    public moveBall(elapsedSec: number) {
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
            } else {
                //score!
            }
        }
    }

    public movePaddles(elapsedSec: number) {
        this.playerYLocation -= this.paddleSpeed * this.upvector * elapsedSec;
        this.playerYLocation += this.paddleSpeed * this.downvector * elapsedSec;
        this.playerSprite.y = this.unitsToPixelsY(this.playerYLocation);
    };

    public unitsToPixels(coordinate: Point) {
        var translation = new Point();
        translation.x = (800 / 150) * coordinate.x; //todo remove magic numbers and replace with constants: SCREEN_WIDTH and SCREEN_HEIGHT
        translation.y = (600 / 100) * coordinate.y;
        return translation;
    }

    public unitsToPixelsY(y: number) {
        return (600 / 100) * y;
    }

    /************ PHASER METHODS ************/

    public update(game: Phaser.Game): void {
        this.talkToServer();
        this.tickEstimate(game.time.physicsElapsed);
    }

    public preload(game: Phaser.Game) {
        game.load.image('paddle', 'img/games/pong/item_full_Glide_Paddle.jpg');
        game.load.image('ball', 'img/games/pong/ball.png');
        this.game = game;
    }

    public create(game: Phaser.Game): void {
        game.stage.backgroundColor = "#888";
        PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.W, this.upCallback, this);
        PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.S, this.downCallback, this);

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
            game.paused = true;
        }
    }
}

class Paddle {
    public location: Point;
    public PADDLE_LENGTH: number = 12;//units, not pixels

    public paddleSpeed: number = 25; //units per sec (top to bottom in 4 seconds)
    public yVector: number = 0;
    public instanceId: number = -1;
}

class Point {
    x: number;
    y: number;
}
