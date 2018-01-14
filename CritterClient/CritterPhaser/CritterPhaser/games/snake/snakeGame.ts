import {SnakeFood, SnakeHead, SnakeTail} from './snakeobjects'

export class GameEngine {
    /************ PHASER METHODS ************/

    game: Phaser.Game;

    //arrows
    up: Phaser.Key;
    wKey: Phaser.Key;
    down: Phaser.Key;
    sKey: Phaser.Key;
    left: Phaser.Key;
    aKey: Phaser.Key;
    right: Phaser.Key;
    dKey: Phaser.Key;
    Phaser

    snake: SnakeHead;

    public update(game: Phaser.Game): void {
        game.debug.cameraInfo(game.camera, 32, 32);
        
        //input
        var yV = ((this.down.isDown || this.sKey.isDown) ? 1 : 0) + ((this.up.isDown || this.wKey.isDown) ? -1 : 0);
        var xV = ((this.right.isDown || this.dKey.isDown) ? 1 : 0) + ((this.left.isDown || this.aKey.isDown) ? -1 : 0);
        if (yV == 0 && xV == 0 && game.input.activePointer.isDown) {
            var deltaX = game.input.activePointer.positionDown.x - game.input.activePointer.position.x;
            var deltaY = game.input.activePointer.positionDown.y - game.input.activePointer.position.y;
            var aDX = Math.abs(deltaX);
            var aDY = Math.abs(deltaY);
            if (aDX > aDY) {
                xV = deltaX > 0 ? -1 : 1;
            } else if (aDY > aDX) {
                yV = deltaY > 0 ? -1 : 1;
            }
        }
        if (! (yV != 0 && xV != 0))
            this.snake.updateVector(xV, yV);

        this.snake.update(game.time.physicsElapsed);
    }

    public preload(game: Phaser.Game) {
        this.game = game;
    }

    public create(game: Phaser.Game): void {

        this.up = game.input.keyboard.addKey(Phaser.Keyboard.UP);
        this.down = game.input.keyboard.addKey(Phaser.Keyboard.DOWN);
        this.left = game.input.keyboard.addKey(Phaser.Keyboard.LEFT);
        this.right = game.input.keyboard.addKey(Phaser.Keyboard.RIGHT);

        this.wKey = game.input.keyboard.addKey(Phaser.Keyboard.W);
        this.aKey = game.input.keyboard.addKey(Phaser.Keyboard.A);
        this.sKey = game.input.keyboard.addKey(Phaser.Keyboard.S);
        this.dKey = game.input.keyboard.addKey(Phaser.Keyboard.D);
        game.input.touch.enabled = true;
        game.input.mouse.capture = true;
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
