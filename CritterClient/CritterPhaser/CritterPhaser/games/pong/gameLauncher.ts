import {PlayerCommandHandler} from './PlayerCommandHandler'
import {PongPaddle, PongBall} from './PongObjects'
import {Player, User, GameObject, SocketGameResponse} from '../SocketGameResponse'

export class GameLauncher {

    public static SCREEN_WIDTH = 800;
    public static SCREEN_HEIGHT = 600;

    constructor(server: WebSocket) {
        GameLauncher.webSocket = server;
        GameLauncher.gameEngine = new GameEngine(GameLauncher.webSocket);
        GameLauncher.game = new Phaser.Game(GameLauncher.SCREEN_WIDTH, GameLauncher.SCREEN_HEIGHT, Phaser.AUTO, 'critterCanvas', {
            create: GameLauncher.gameEngine.create.bind(GameLauncher.gameEngine),
            update: GameLauncher.gameEngine.update.bind(GameLauncher.gameEngine),
            preload: GameLauncher.gameEngine.preload.bind(GameLauncher.gameEngine)
        });
    }
    private static game: Phaser.Game = null;
    private static gameEngine: GameEngine;
    private static webSocket: WebSocket = null;
}

enum EntityTypes {
    Paddle,
    Ball
}

class GameEngine {

    public handleIncomingMessage(event: MessageEvent) {
        var data = <SocketGameResponse>JSON.parse(event.data);
        if (!this.hasStarted && data.startTickingNow) {
            this.startedTime = data.startTime;
            this.myInstanceId = data.assignedInstanceId;
            this.hasStarted = true;
            this.game.paused = false;
        }

        if (this.hasStarted) {
            if (data.deltaObjects)
                for (var i = 0; i < data.deltaObjects.length; i++) {
                    let object = data.deltaObjects[i];
                    if (object.ENTITY_TYPE_ID == EntityTypes.Ball) {
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
                    if (player.physicsComponent && player.physicsComponent.ENTITY_TYPE_ID == EntityTypes.Paddle) {
                        let paddle: PongPaddle = <PongPaddle>player.physicsComponent;
                        if (paddle.GAME_INSTANCE_ID == this.myInstanceId) { //it me, i'm that
                            this.playerPaddle.y = paddle.y;
                            this.playerPaddle.x = paddle.x;
                            this.playerPaddle.PADDLE_VELOCITY = paddle.PADDLE_VELOCITY;
                            this.playerPaddle.PADDLE_HEIGHT = paddle.PADDLE_HEIGHT;
                            this.playerPaddle.yVector = paddle.yVector;
                            (<any>this.playerPaddle).type = (<any>paddle).type;

                        } else {
                            this.enemyPaddle.y = paddle.y;
                            this.enemyPaddle.x = paddle.x;
                            this.enemyPaddle.PADDLE_VELOCITY = paddle.PADDLE_VELOCITY;
                            this.enemyPaddle.PADDLE_HEIGHT = paddle.PADDLE_HEIGHT;
                            this.enemyPaddle.yVector = paddle.yVector;
                        }
                    }

                    if (this.players.filter(p => p.user.userID == player.user.userID).length == 0) {
                        this.players.push(player);
                    }
                }
            this.tickNumber = data.tickNumber;
            this.lastServerTime = data.currentTime;
        }

        if (data.gameOver) {
            alert("Goodbye");
        }
    }

    server: WebSocket = null;

    public hasStarted: boolean = false;
    public tickNumber: number = 0;
    public myInstanceId = -1;

    public startedTime: number;
    public lastServerTime: number;

    public players: Array<Player> = [];

    public playerPaddle: PongPaddle = new PongPaddle();
    public enemyPaddle: PongPaddle = new PongPaddle();

    //world units
    public static RIGHT_X : number = 1500;
    public static LEFT_X: number = 0;
    public static TOP_Y: number = 1000;
    public static BOTTOM_Y: number = 0;

    //ball data
    public ballLocation: Point = new Point();
    public ballSprite: Phaser.Sprite;
    public ballYVector: number = 0; //updown
    public ballXVector: number = 0; //leftright
    public ballSpeed: number = 1;
    public ballDiameter: number = 20;

    //player data
    public playerSprite: Phaser.Sprite;

    //enemy data
    public enemySprite: Phaser.Sprite;

    public game: Phaser.Game;
    public text: Phaser.Text;
    public keyboard: Phaser.Keyboard;
    public spaceBar = false;
    public upPressed = false;
    public downPressed = false;
    public upKey: Phaser.Key;
    public downKey: Phaser.Key;
    public mouse: Phaser.Mouse;


    public constructor(socket: WebSocket) {
        this.server = socket;
    }

    public upCallback(key: Phaser.Key) {
        if (this.hasStarted)
            if (key.isDown) {
                this.upPressed = true;
                this.playerPaddle.yVector = -1;
            } else {
                this.upPressed = false;
            }
    }

    public downCallback(key: Phaser.Key) {
        if (this.hasStarted)
            if (key.isDown) {
                this.downPressed = true;
                this.playerPaddle.yVector = 1;
            } else {
                this.downPressed = false;
            }
    }

    public talkToServer() {
        if (this.server != null) {
            var cmds = [];
            if (this.upPressed) {
                cmds.push("W");
            }
            if (this.downPressed) {
                cmds.push("S");
            }
            if ((this.upPressed == false && this.downPressed == false)) {
                cmds.push("WS");
            }

            if (this.spaceBar) {
                this.spaceBar = false;
                cmds.push(" ");
            }

            if (cmds.length > 0) {
                this.server.send(JSON.stringify({ commands: cmds, clientState: this.playerPaddle }));
            }
        }
    }

    public tickEstimate(elapsedMS: number) {
        //input checks
        this.playerPaddle.yVector = 0;
        this.upCallback(this.upKey);
        this.downCallback(this.downKey);

        this.movePaddles(elapsedMS);
        this.moveBall(elapsedMS);
        var timeRemainingSec = Math.floor((this.lastServerTime - this.startedTime) / 1000);
        var minutes : any = Math.floor(timeRemainingSec / 60);
        var seconds : any = timeRemainingSec - (minutes * 60);
        if (minutes < 10) { minutes = "0" + minutes; }
        if (seconds < 10) { seconds = "0" + seconds; }

        var myScore;
        var enemyScore;
        for (var i = 0; i < this.players.length; i++) {
            var player = this.players[i];
            if (player.physicsComponent.GAME_INSTANCE_ID == this.myInstanceId) {
                myScore = player.score;
            } else {
                enemyScore = player.score;
            }
        }
        var scoreStr = this.myInstanceId == 1 ? myScore + ":" + enemyScore : enemyScore + ":" + myScore;
        this.text.setText(minutes + ":" + seconds + "\n" + scoreStr);
    }

    public moveBall(elapsedMS: number) {
        this.ballLocation.x += (elapsedMS * this.ballXVector * this.ballSpeed);
        this.ballLocation.y += (elapsedMS * this.ballYVector * this.ballSpeed);

        if (this.ballLocation.y <= GameEngine.BOTTOM_Y || this.ballLocation.y >= GameEngine.TOP_Y) {
            this.ballYVector = this.ballYVector * -1;
        }

        var ballPoint1 = this.ballLocation.x + this.ballDiameter / 2;
        var ballPoint2 = this.ballLocation.x - this.ballDiameter / 2;

        //check paddle collisions
        if ((this.ballLocation.y > this.playerPaddle.y && this.ballLocation.y < this.playerPaddle.y + this.playerPaddle.PADDLE_HEIGHT) &&
            ((ballPoint1 <= this.playerPaddle.x && this.playerPaddle.x <= ballPoint2) || (ballPoint2 <= this.playerPaddle.x && this.playerPaddle.x <= ballPoint1))) {
            this.ballXVector *= -1;

            this.ballYVector = (((this.ballLocation.y - this.playerPaddle.y + this.playerPaddle.PADDLE_HEIGHT/2) / (this.playerPaddle.PADDLE_HEIGHT / 6)) * this.ballSpeed) +
                (this.playerPaddle.yVector * this.playerPaddle.PADDLE_VELOCITY);

            this.ballLocation.x += (elapsedMS * this.ballXVector * this.ballSpeed);
            this.ballLocation.y += (elapsedMS * this.ballYVector * this.ballSpeed);

        }


        //check paddle collisions
        if ((this.ballLocation.y > this.enemyPaddle.y && this.ballLocation.y < this.enemyPaddle.y + this.enemyPaddle.PADDLE_HEIGHT) &&
            ((ballPoint1 <= this.enemyPaddle.x && this.enemyPaddle.x <= ballPoint2) || (ballPoint2 <= this.enemyPaddle.x && this.enemyPaddle.x <= ballPoint1))) {
            this.ballXVector *= -1;

            this.ballYVector = (((this.ballLocation.y - this.enemyPaddle.y + this.enemyPaddle.PADDLE_HEIGHT / 2) / (this.enemyPaddle.PADDLE_HEIGHT / 6)) * this.ballSpeed) +
                (this.enemyPaddle.yVector * this.enemyPaddle.PADDLE_VELOCITY);

            this.ballLocation.x += (elapsedMS * this.ballXVector * this.ballSpeed);
            this.ballLocation.y += (elapsedMS * this.ballYVector * this.ballSpeed);

        }

        var screenLoc = this.unitsToPixels(this.ballLocation);
        this.ballSprite.x = screenLoc.x;
        this.ballSprite.y = screenLoc.y;
        
    }

    public movePaddles(elapsedMS: number) {
        this.playerPaddle.y += this.playerPaddle.yVector * this.playerPaddle.PADDLE_VELOCITY * elapsedMS;
        var location = this.unitsToPixels(this.playerPaddle);
        this.playerSprite.x = location.x;
        this.playerSprite.y = location.y;
           
        var enemyLocation = this.unitsToPixels(this.enemyPaddle);
        this.enemySprite.x = enemyLocation.x;
        this.enemySprite.y = enemyLocation.y;
    };

    public unitsToPixels(coordinate: Point) {
        var translation = new Point();
        translation.x = (GameLauncher.SCREEN_WIDTH / GameEngine.RIGHT_X) * coordinate.x;
        translation.y = (GameLauncher.SCREEN_HEIGHT / GameEngine.TOP_Y) * coordinate.y;
        return translation;
    }

    public unitsToPixelsY(y: number) {
        return (GameLauncher.SCREEN_HEIGHT / GameEngine.TOP_Y) * y;
    }

    /************ PHASER METHODS ************/

    public update(game: Phaser.Game): void {
        this.tickEstimate(game.time.physicsElapsedMS);
        this.talkToServer();
    }

    public preload(game: Phaser.Game) {
        game.load.image('paddle', 'img/games/pong/item_full_Glide_Paddle.jpg');
        game.load.image('ball', 'img/games/pong/ball.png');
        this.game = game;
    }

    public create(game: Phaser.Game): void {
        game.stage.disableVisibilityChange = true;
        game.stage.backgroundColor = "#888";
        this.upKey = game.input.keyboard.addKey(Phaser.Keyboard.W);
        this.downKey = game.input.keyboard.addKey(Phaser.Keyboard.S);
        PlayerCommandHandler.createSingleMapping(game.input.keyboard, Phaser.Keyboard.SPACEBAR, () => { this.spaceBar = true; }, this);

        this.playerSprite = game.add.sprite(0, 0, 'paddle');
        this.playerSprite.anchor.set(0, 0);
        this.playerSprite.width = 10;
        this.playerSprite.height = GameLauncher.SCREEN_HEIGHT / GameEngine.TOP_Y * 120;

        this.enemySprite = game.add.sprite(0, 0, 'paddle');
        this.enemySprite.anchor.set(0,0);
        this.enemySprite.width = 10;
        this.enemySprite.height = GameLauncher.SCREEN_HEIGHT / GameEngine.TOP_Y * 120;

        this.ballSprite = game.add.sprite(0, 0, 'ball');
        this.ballSprite.anchor.set(.5, .5);
        this.ballSprite.width = GameLauncher.SCREEN_WIDTH / GameEngine.RIGHT_X * this.ballDiameter; 
        this.ballSprite.height = GameLauncher.SCREEN_HEIGHT / GameEngine.TOP_Y * this.ballDiameter;

        if (this.server != null)
            this.server.onmessage = this.handleIncomingMessage.bind(this);

        game.scale.fullScreenScaleMode = Phaser.ScaleManager.SHOW_ALL;
        if (!this.hasStarted) {
            game.paused = true;
        }
        this.text = game.add.text(GameLauncher.SCREEN_WIDTH / 2, 50, "Newest code!", {
            font: "12px Arial", align: "center"
        });
        this.text.anchor.set(0.5);      
    }
}

class Point {
    x: number;
    y: number;
}
