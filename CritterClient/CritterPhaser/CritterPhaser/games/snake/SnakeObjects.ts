import {Player, User, GameObject, SocketGameResponse} from '../SocketGameResponse'

export class SnakeFood extends GameObject {
    sprite: Phaser.Sprite;

    constructor(game: Phaser.Game, x, y) {
        super();
        this.x = x; this.y = y;
        this.sprite = game.add.sprite(0, 0, 'food');
        this.sprite.anchor.set(0.5, 0.5);
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        game.world.add(this.sprite);
    }

    public update() {
    }
    
    public respawnInNewLocation(snake: SnakeHead) {
    }

}

export class SnakeHead extends GameObject {
    sprite: Phaser.Sprite;

    constructor(game: Phaser.Game, x, y) {
        super();
        this.x = x; this.y = y;
        this.sprite = game.add.sprite(0, 0, 'head');
        this.sprite.anchor.set(0.5, 0.5);
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        game.world.add(this.sprite);
    }

     
    
}

export class SnakeTail extends GameObject {

}

export class Obstacle extends GameObject {

}