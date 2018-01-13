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
    body: SnakeTail[] = [];
    game: Phaser.Game
    inflects = [];

    next: SnakeTail;

    constructor(game: Phaser.Game, x, y) {
        super();
        this.game = game;
        this.x = x; this.y = y; this.xVector = -1; this.yVector = 0;
        this.sprite = game.add.sprite(0, 0, 'head');

        this.sprite.anchor.set(0.5, 0.5);
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        game.world.add(this.sprite);
        //this.body.push(new SnakeTail(game, x + 40, y));
        //this.body.push(new SnakeTail(game, x + 80, y));
        //this.body.push(new SnakeTail(game, x + 120, y));
        //this.body.push(new SnakeTail(game, x + 160, y));
        //this.body.push(new SnakeTail(game, x + 200, y));
        //this.body.push(new SnakeTail(game, x + 240, y));
        //this.body.push(new SnakeTail(game, x + 280, y));
        //this.body.push(new SnakeTail(game, x + 320, y));
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
        if (this.next)
            this.next.update(dt);
        this.x += 800 * dt * this.xVector;
        this.y += 800* dt * this.yVector;
        this.sprite.x = this.x;
        this.sprite.y = this.y;
    }

    getPoint() {
        return this.inflects.length > 0 ? this.inflects.shift() : null;
    }     

    addBody() {
        if (this.next) {
            this.next.addBody();
        } else {
            var newtail = new SnakeTail(this.game, this.x - this.xVector * 50, this.y - this.yVector * 50, null);
            newtail.xVector = this.xVector;
            newtail.yVector = this.yVector;
            this.next = newtail;
            newtail.prev = <any> this;
        }
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
        this.x = x; this.y = y; this.xVector = -1; this.yVector = 0;
        this.sprite = game.add.sprite(0, 0, sprite ? sprite : 'body');
        this.sprite.anchor.set(0.5, 0.5);
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        game.world.add(this.sprite);
    }

    getPoint() {
        return this.inflects.length > 0 ? this.inflects.shift() : null;
    }     

    update(dt : number) {
        //if (this.next != null) {
        //    this.next.update();
        //}
        this.x += 800 *dt* this.xVector;
        this.y += 800*dt *this.yVector;
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        if (this.destination == null || Phaser.Math.fuzzyEqual(this.x, this.destination.x, 800 * dt * .5) && Phaser.Math.fuzzyEqual(this.y, this.destination.y, 800 * dt * .5)) {
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
            this.next.update(dt);
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