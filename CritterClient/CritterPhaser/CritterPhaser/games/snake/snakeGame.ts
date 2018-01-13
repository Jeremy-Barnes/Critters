import {SnakeFood, SnakeHead, SnakeTail} from './snakeobjects'

export class GameEngine {
    /************ PHASER METHODS ************/

    game: Phaser.Game;
    cursors: Phaser.CursorKeys; 
    snake: SnakeHead;

    public update(game: Phaser.Game): void {
        game.debug.cameraInfo(game.camera, 32, 32);
        
 
        var yV = (this.cursors.down.isDown ? 1 : 0) + (this.cursors.up.isDown ? -1 : 0);
        var xV = (this.cursors.right.isDown ? 1 : 0) + (this.cursors.left.isDown ? -1 : 0);
        if (! (yV != 0 && xV != 0))
        this.snake.updateVector(xV, yV);

        this.snake.update(game.time.physicsElapsed);
    }

    public preload(game: Phaser.Game) {
        this.game = game;
        game.load.image('food', 'img/games/snake/Snake.gif');
        game.load.image('head', 'img/games/snake/blueSkull1.png');
        game.load.image('body', 'img/games/snake/skull1.png');
        game.load.image('block', 'img/games/snake/miniTiles.png');
    }

    public create(game: Phaser.Game): void {
        this.cursors = game.input.keyboard.createCursorKeys();
        game.stage.disableVisibilityChange = true;
        game.stage.backgroundColor = "#888";
        game.world.setBounds(0, 0, 800, 800);
        game.camera.x = 0;
        game.camera.y = 0;
        game.camera.setBoundsToWorld();



        new SnakeFood(game, 790, 790);
        this.snake = new SnakeHead(game, 400, 400);
        new SnakeHead(game, 0, 0);
        new SnakeFood(game, 0, 790);
        new SnakeFood(game, 790, 0);


    }
}
