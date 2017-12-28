import {PlayerCommandHandler} from './PlayerCommandHandler'
import {PongPaddle, PongBall} from './PongObjects'
import {Player, User, GameObject, SocketGameResponse} from '../SocketGameResponse'

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
    public myInstanceId = -1;
    public game: Phaser.Game;
    public playerPaddle: PongPaddle;
    public enemyPaddle: PongPaddle;

    public debugMessages: Array<String> = [];

    public setUpGameComms() {
        this.server.onmessage = this.handleIncomingMessage.bind(this);
    }

    public handleIncomingMessage(event: MessageEvent) {
        var data = <SocketGameResponse> JSON.parse(event.data);
        if (!this.hasStarted && data.startTickingNow) {
            this.startedTime = data.startTime;
            this.myInstanceId = data.assignedInstanceId;
            for (var i = 0; i < data.deltaObjects.length; i++) {
                let object = data.deltaObjects[i]; //should only be a ball, but we can keep it like this and do EntityID comparisons if we expand
                if (object.ENTITY_TYPE_ID == 0) { //todo make an enum. 0 = ball, 1 = paddle
                    this.ballLocation = { x: object.x, y: object.y };
                    this.ballXVector = object.xVector;
                    this.ballYVector = object.yVector;
                    this.ballSpeed = (<PongBall>object).BALL_VELOCITY;
                    this.ballDiameter = (<PongBall>object).BALL_DIAMETER;
                }
            }

            for (var i = 0; i < data.deltaPlayers.length; i++) {
                let player = data.deltaPlayers[i]; //probably only 2, but maybe spectators?
                if (player.physicsComponent) {
                    let paddle: PongPaddle = <PongPaddle>player.physicsComponent;
                    if (paddle.GAME_INSTANCE_ID == this.myInstanceId) { //it me, i'm that
                        this.playerPaddle = paddle;
                    } else {
                        this.enemyPaddle = paddle;
                    }
                }
            }
            this.hasStarted = true;
            this.game.paused = false;
        } else if (this.hasStarted) {
            if (data.deltaObjects)
            for (var i = 0; i < data.deltaObjects.length; i++) {
                let object = data.deltaObjects[i]; //should only be a ball, but we can keep it like this and do EntityID comparisons if we expand
                if (object.ENTITY_TYPE_ID == 0) { //todo make an enum. 0 = ball, 1 = paddle
                    this.debugMessages.push("WHEN DIFFERENT LOCAL X: " + this.ballLocation.x + " Y: " + this.ballLocation.y + " XV: " + this.ballXVector + " YV: " + this.ballYVector);
                    this.debugMessages.push("WHEN DIFFERENT REMOTE X: " + object.x + " Y: " + object.y + " XV: " + object.xVector + " YV: " + object.yVector);

                    this.ballLocation.x = object.x;
                    this.ballLocation.y = object.y;
                    this.ballXVector = object.xVector;
                    this.ballYVector = object.yVector;
                    this.ballSpeed = (<PongBall>object).BALL_VELOCITY;
                    this.ballDiameter = (<PongBall>object).BALL_DIAMETER;


                } 
            }
            if(data.deltaPlayers)
            for (var i = 0; i < data.deltaPlayers.length; i++) {
                let player = data.deltaPlayers[i]; //probably only 2, but maybe spectators?
                if (player.physicsComponent) {
                    let paddle: PongPaddle = <PongPaddle>player.physicsComponent;
                    if (paddle.GAME_INSTANCE_ID == this.playerPaddle.GAME_INSTANCE_ID) { //it me, i'm that
                        this.playerPaddle.y = paddle.y;
                        this.playerPaddle.PADDLE_VELOCITY = paddle.PADDLE_VELOCITY;
                        this.playerPaddle.PADDLE_HEIGHT = paddle.PADDLE_HEIGHT;
                        this.playerPaddle.yVector = paddle.yVector;
                    } else {
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

        this.text.setText("" + (data.currentTime/1000 - this.startedTime/1000));
    }

    public static RIGHT_X : number = 150;
    public static LEFT_X: number = 0;
    public static TOP_Y: number = 100;
    public static BOTTOM_Y: number = 0;

    //ball data
    public ballLocation: Point;
    public ballSprite: Phaser.Sprite;
    public ballYVector: number = 0; //updown
    public ballXVector: number = 0; //leftright
    public ballSpeed: number = 1;
    public ballDiameter: number = 1;

    //player data
    public playerSprite: Phaser.Sprite;

    //enemy data
    public enemySprite: Phaser.Sprite;

    public text: Phaser.Text;
    public startedTime: number;
    public spaceBar = false;

    public keyboard: Phaser.Keyboard;
    public mouse: Phaser.Mouse;

    server : WebSocket = null;

    public constructor(socket: WebSocket) {
        this.server = socket;
    }

    public upCallback(key: Phaser.Key) {
        if (this.hasStarted)
        if (key.isDown) {
            this.playerPaddle.yVector = 1;
        } else {
            this.playerPaddle.yVector = 0;
        }
    }

    public downCallback(key: Phaser.Key) {
        if (this.hasStarted)
        if (key.isDown) {
            this.playerPaddle.yVector = -1;
        } else {
            this.playerPaddle.yVector = 0;
        }
    }

    public talkToServer() {
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
    }

    public tickEstimate(elapsedMS: number) {
        this.movePaddles(elapsedMS);
        this.moveBall(elapsedMS);
    }

    public moveBall(elapsedMS: number) {
        this.ballLocation.x += (elapsedMS * this.ballXVector * this.ballSpeed);
        this.ballLocation.y += (elapsedMS * this.ballYVector * this.ballSpeed);
      
        var screenLoc = this.unitsToPixels(this.ballLocation);
        this.ballSprite.x = screenLoc.x;
        this.ballSprite.y = screenLoc.y;

        if (this.ballLocation.y <= GameEngine.BOTTOM_Y || this.ballLocation.y >= GameEngine.TOP_Y) {
            this.ballYVector = this.ballYVector * -1;
        }

        this.debugMessages.push("X: " + this.ballLocation.x + " Y: " + this.ballLocation.y + " XV: " + this.ballXVector + " YV: " + this.ballYVector)

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
    }

    public movePaddles(elapsedMS: number) {
        this.playerPaddle.y += this.playerPaddle.yVector * this.playerPaddle.PADDLE_VELOCITY * elapsedMS;
        this.playerSprite.y = this.unitsToPixelsY(this.playerPaddle.y);
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
        this.tickEstimate(game.time.physicsElapsedMS);
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
        PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.SPACEBAR, () => { this.spaceBar = true; }, this);


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

    }
}

class Point {
    x: number;
    y: number;
}
