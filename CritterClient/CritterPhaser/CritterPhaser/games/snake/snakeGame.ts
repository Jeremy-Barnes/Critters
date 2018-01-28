import {SnakeFood, SnakeHead} from './snakeobjects'

export class GameEngine {
    /************ PHASER METHODS ************/

    game: Phaser.Game;

    public update(game: Phaser.Game): void {
        game.debug.cameraInfo(game.camera, 32, 32);
        game.camera.scale.x = .5
        game.camera.scale.y = .5
    }

    public preload(game: Phaser.Game) {
        this.game = game;
        game.load.image('food', 'img/games/snake/Snake.gif');
        game.load.image('head', 'img/games/snake/blueSkull1.png');
        game.load.image('body', 'img/games/snake/skull1.png');
        game.load.image('block', 'img/games/snake/miniTiles.png');
    }

    public create(game: Phaser.Game): void {
        game.stage.disableVisibilityChange = true;
        game.stage.backgroundColor = "#888";

        game.world.setBounds(0, 0, 1000, 1000);
        new SnakeFood(game, 988, 988);
        new SnakeHead(game, 500, 500);
        new SnakeHead(game, 0, 0);
        new SnakeFood(game, 0, 988);
        new SnakeFood(game, 988, 0);


    }
}
