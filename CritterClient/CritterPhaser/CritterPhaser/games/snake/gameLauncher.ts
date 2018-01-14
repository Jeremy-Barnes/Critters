import {Player, User, GameObject, SocketGameResponse} from '../SocketGameResponse'
import {GameEngine} from "./snakegame"

export class GameLauncher {

    public static SCREEN_WIDTH = 800;
    public static SCREEN_HEIGHT = 600;

    constructor() {
        GameLauncher.gameEngine = new GameEngine();
        GameLauncher.game = new Phaser.Game(GameLauncher.SCREEN_WIDTH, GameLauncher.SCREEN_HEIGHT, Phaser.AUTO, 'critterCanvas', {
            create: GameLauncher.gameEngine.create.bind(GameLauncher.gameEngine),
            update: GameLauncher.gameEngine.update.bind(GameLauncher.gameEngine),
            preload: GameLauncher.gameEngine.preload.bind(GameLauncher.gameEngine)
            
        });
        GameLauncher.game.state.add("loading", LoadingScreen);
        GameLauncher.game.state.add("mainmenu", MainMenu);
        GameLauncher.game.state.add("play", GameLauncher.gameEngine);
        GameLauncher.game.state.add("gameover", GameLauncher.gameEngine);


        GameLauncher.game.state.start("loading");
    }
    private static game: Phaser.Game = null;
    private static gameEngine: GameEngine;
}

export class LoadingScreen {
    public text: Phaser.Text;
    public loading: boolean;
    public constructor() {
    }
    public init() {
    }
    public preload(game: Phaser.Game) {
        game.load.image('loading', 'img/games/snake/loading.gif');          
    }
    public create(game: Phaser.Game) {
        var sprite = game.add.sprite(0, 0, 'loading');
        sprite.width = 800;
        sprite.height = 600;
        game.load.reset(true, true);
        game.load.onLoadStart.add(() => { this.text = game.add.text(game.world.centerX, 4 * game.world.bottom / 5, "Loading", { fill: "red" }); this.text.anchor.setTo(.5, .5); }, this);
        game.load.onFileComplete.add(this.fileComplete, this);
        game.load.onLoadComplete.add(() => { game.state.start("mainmenu") }, this);
        
    }
    public update(game: Phaser.Game) {
        if (!this.loading) {
            this.loading = true;
            game.load.image('food', 'img/games/snake/Snake.gif');
            game.load.image('head', 'img/games/snake/blueSkull1.png');
            game.load.image('body', 'img/games/snake/skull1.png');
            game.load.image('block', 'img/games/snake/miniTiles.png');
            game.load.start();
        }
    }

    public fileComplete(progress, cacheKey, success, totalLoaded, totalFiles) {
        this.text.setText("Loading: " + progress + "%");
    }

}

export class MainMenu {
    public constructor() {
    }

    public init(game: Phaser.Game) {

    }
    public preload(game: Phaser.Game) {
    }
    public create(game: Phaser.Game) {
        game.scale.fullScreenScaleMode = Phaser.ScaleManager.SHOW_ALL;
        game.stage.backgroundColor = '#337799';

        var title = game.add.text(game.world.centerX, 50, "Snek", null);
        title.anchor.setTo(.5, .5);

        var text = game.add.text(game.world.centerX, 250, "Start Game" , null);
        text.anchor.setTo(.5, .5);
        text.inputEnabled = true;
        text.events.onInputUp.add(() => { game.state.start("play"); });

        var text2 = game.add.text(game.world.centerX, 350, "Enter Fullscreen", null);
        text2.anchor.setTo(.5, .5);
        text2.inputEnabled = true;
        text2.events.onInputUp.add(() => {
            if (game.scale.isFullScreen) {
                text2.setText("Enter Fullscreen");
                game.scale.stopFullScreen();
            }
            else {
                text2.setText("Exit Fullscreen");
                game.scale.startFullScreen(false);
            }
         });
       
    }
    public update(game: Phaser.Game) {
    }

}

export class GameOver {
    public constructor() {
    }
    public init(game: Phaser.Game) {
    }
    public preload(game: Phaser.Game) {
    }
    public create(game: Phaser.Game) {
    }
    public update(game: Phaser.Game) {
    }
}