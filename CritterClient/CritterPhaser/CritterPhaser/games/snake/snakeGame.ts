import {SnakeFood, SnakeHead, SnakeTail} from './snakeobjects'

export class GameEngine {
    /************ PHASER METHODS ************/

    game: Phaser.Game;
    score: Phaser.Text;
    //arrows
    up: Phaser.Key;
    wKey: Phaser.Key;
    down: Phaser.Key;
    sKey: Phaser.Key;
    left: Phaser.Key;
    aKey: Phaser.Key;
    right: Phaser.Key;
    dKey: Phaser.Key;
    

    snake: SnakeHead;
    food: SnakeFood;

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

        this.food.update(this.snake, this.score);
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
        game.world.setBounds(0, 0, 800, 800);

        game.input.touch.enabled = true;
        game.input.mouse.capture = true;
        game.stage.disableVisibilityChange = true;
        game.camera.x = 0;
        game.camera.y = 0;
        this.score = game.add.text(game.camera.bounds.centerX, game.camera.bounds.bottom - 100, "Score: 0 points", null);
        this.score.anchor.setTo(.5, .5);
        this.food = new SnakeFood(game, 100, 190);
        this.snake = new SnakeHead(game, 400, 400, 200);


    }
}
