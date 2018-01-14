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


export class SnakeTail extends GameObject {
    sprite: Phaser.Sprite;
    game: Phaser.Game;

    next: SnakeTail;
    prev: SnakeTail;
    destination: Phaser.Point;
    inflects = [];

    constructor(game: Phaser.Game, x, y, sprite: string) {
        super();
        this.game = game;
        this.x = x; this.y = y;
        this.sprite = game.add.sprite(0, 0, sprite ? sprite : 'body');
        this.sprite.anchor.set(0.5, 0.5);
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        game.world.add(this.sprite);
    }

    getPoint() {
        return this.inflects.length > 0 ? this.inflects.shift() : null;
    }

    update(dt: number, hx, hy, hnx, hny) {
        if (this.prev.prev) {
            var x1 = (this.x <= hx && this.prev.x >= hnx);
            var x2 = (this.x >= hx && this.prev.x <= hnx);
            var y1 = (this.y <= hy && this.prev.y >= hny);
            var y2 = (this.y >= hy && this.prev.y <= hny);
            if ((x1 || x2) && (y1 || y2)) {
                alert('crossed paths');
            }
        } else if (this.destination) {
            var x1 = (this.x <= hx && this.destination.x >= hnx);
            var x2 = (this.x >= hx && this.destination.x <= hnx);
            var y1 = (this.y <= hy && this.destination.y >= hny);
            var y2 = (this.y >= hy && this.destination.y <= hny);
            if ((x1 || x2) && (y1 || y2)) {
                alert('crossed paths');
            }
        }

        this.x = this.x + 100 * dt * this.xVector;
        this.y = this.y + 100 * dt * this.yVector;
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        if (this.destination == null || Phaser.Math.fuzzyEqual(this.x, this.destination.x, 100 * dt * .5) && Phaser.Math.fuzzyEqual(this.y, this.destination.y, 100 * dt * .5)) {
            if (this.destination != null) {
                this.inflects.push(this.destination);
                this.x = this.destination.x;
                this.y = this.destination.y;
            }
            this.destination = this.prev.getPoint();
            if (this.destination != null) {
                this.xVector = this.destination.x - this.x;
                this.xVector = this.xVector / Math.abs(this.xVector == 0 ? 1 : this.xVector);
                this.yVector = this.destination.y - this.y;
                this.yVector = this.yVector / Math.abs(this.yVector == 0 ? 1 : this.yVector);
            } else {
                this.destination = new Phaser.Point(this.prev.x, this.prev.y);
                this.xVector = this.prev.xVector;
                this.yVector = this.prev.yVector;
            }
        }

        if (this.next != null) {
            this.next.update(dt, hx, hy, hnx, hny);
        }
    }

    addBody() {
        if (this.next) {
            this.next.addBody();
        } else {
            var newtail = new SnakeTail(this.game, this.x - this.xVector * 50, this.y - this.yVector * 50, null);
            newtail.xVector = this.xVector;
            newtail.yVector = this.yVector;
            this.next = newtail;
            newtail.prev = this;
        }
    }
}


export class SnakeHead extends SnakeTail {
    constructor(game: Phaser.Game, x, y) {
        super(game,x,y, 'head');
        this.x = x; this.y = y; this.xVector = -1; this.yVector = 0;
   
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
        this.addBody();
    }
    updateVector(xV: number, yV: number) {
        if ((xV != 0 && yV != 0) || (xV == 0 && yV == 0) || (xV == this.xVector && yV == this.yVector)) {
            return;
        }

        this.xVector = xV;
        this.yVector = yV;
        if (this.next)
            this.inflects.push(new Phaser.Point(this.x, this.y));
    }

    update(dt: number) {
        var nx = this.x + 100 * dt * this.xVector;
        var ny = this.y + 100 * dt * this.yVector;

        if (this.next)
            this.next.update(dt, this.x, this.y, nx, ny);
        this.x = nx;
        this.y = ny;
        this.sprite.x = this.x;
        this.sprite.y = this.y;

        if (this.x >= this.game.world.bounds.right || this.x <= this.game.world.bounds.x || this.y <= this.game.world.bounds.y || this.y >= this.game.world.bounds.bottom) {
            this.game.state.start("gameover");
        }
    }    
}

export class Obstacle extends GameObject {
    sprite: Phaser.Sprite;

    constructor(game: Phaser.Game, x, y) {
        super();
        this.x = x; this.y = y;
        this.sprite = game.add.sprite(0, 0, 'block');
        this.sprite.anchor.set(0.5, 0.5);
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        game.world.add(this.sprite);
    }
}