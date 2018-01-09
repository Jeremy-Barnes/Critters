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
    }
    private static game: Phaser.Game = null;
    private static gameEngine: GameEngine;
}