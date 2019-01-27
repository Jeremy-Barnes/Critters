import {Player, User, GameObject, SocketGameResponse} from '../SocketGameResponse'


export class SnakeFood extends GameObject {
    sprite: Phaser.Sprite;
    game: Phaser.Game; 
    pointsVal = 1;
    collectedPoints = 0;
    constructor(game: Phaser.Game, x, y) {
        super();
        this.game = game;
        this.x = x; this.y = y;
        this.sprite = game.add.sprite(0, 0, 'food');
        this.generateNewForm();
        this.sprite.anchor.set(0.5, 0.5);
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        game.world.add(this.sprite);
    }

    public update(snake: SnakeHead, score: Phaser.Text) {
        if (Phaser.Math.fuzzyEqual(snake.x, this.x, snake.diameter) && Phaser.Math.fuzzyEqual(snake.y, this.y, snake.diameter)) {
            this.collectedPoints += this.pointsVal;
            score.setText("Score: " + this.collectedPoints);
            this.generateNewForm();
            this.respawnInNewLocation(snake);
            snake.addBody();
        }
    }

    public generateNewForm() {
        var factor = (500 * Math.random()) + 1; //this really ought to be done with weighted selector and some cleverness, but fuck it.
        if (factor >= 500) {
            this.sprite.loadTexture('8');
            this.pointsVal = 100;

        } else if (factor >= 480) {
            this.sprite.loadTexture('7');
            this.pointsVal = 50;

        } else if (factor >= 450) {
            this.sprite.loadTexture('6');
            this.pointsVal = 30;

        } else if (factor >= 440) {
            this.sprite.loadTexture('5');
            this.pointsVal = 25;

        } else if (factor >= 420) {
            this.sprite.loadTexture('4');
            this.pointsVal = 10;

        } else if (factor >= 400) {
            this.sprite.loadTexture('3');
            this.pointsVal = 5;

        } else if (factor >= 350) {
            this.sprite.loadTexture('2');
            this.pointsVal = 2;

        } else {
            this.sprite.loadTexture('1');
            this.pointsVal = 1;
        }
    }

    public respawnInNewLocation(snake: SnakeHead) {
        var foodPlaced = false
        while (!foodPlaced) {
            var newx = Math.random() * this.game.world.bounds.right;
            var newy = Math.random() * this.game.world.bounds.bottom;
            if (!snake.seeIfDotCollidesWithTail(new Phaser.Point(newx, newy))) {
                foodPlaced = true;
                this.x = newx;
                this.y = newy;
                this.sprite.x = this.x;
                this.sprite.y = this.y;
            }
        }
    }

}


export class SnakeTail extends GameObject {
    game: Phaser.Game;

    sprite: Phaser.Sprite;
    diameter: number = 20;
    velocity: number;

    next: SnakeTail;
    prev: SnakeTail;
    destination: Phaser.Point;
    inflects = [];

    constructor(game: Phaser.Game, x, y, sprite: string, velocity) {
        super();
        this.game = game;
        this.velocity = velocity;
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

        this.x = this.x + this.velocity * dt * this.xVector;
        this.y = this.y + this.velocity * dt * this.yVector;
        this.sprite.x = this.x;
        this.sprite.y = this.y;
        if (this.destination == null || Phaser.Math.fuzzyEqual(this.x, this.destination.x, this.velocity * dt * .5) && Phaser.Math.fuzzyEqual(this.y, this.destination.y, this.velocity * dt * .5)) {
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
            var newtail = new SnakeTail(this.game, this.x - this.xVector * 50, this.y - this.yVector * 50, null, this.velocity);
            newtail.xVector = this.xVector;
            newtail.yVector = this.yVector;
            this.next = newtail;
            newtail.prev = this;
            this.inflects.length = 0;
        }
    }

    public getNextSameX() {
        if (this.next != null && this.next.x == this.x) {
            return this.next.getNextSameX();
        } else {
            return this;
        }
    }


    public getNextSameY() {
        if (this.next != null && this.next.y == this.y) {
            return this.next.getNextSameY();
        } else {
            return this
        }
    }

}


export class SnakeHead extends SnakeTail {
    constructor(game: Phaser.Game, x, y, velocity) {
        super(game,x,y, 'head', velocity);
        this.x = x; this.y = y; this.xVector = -1; this.yVector = 0;
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
        var nx = this.x + this.velocity * dt * this.xVector;
        var ny = this.y + this.velocity * dt * this.yVector;

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

    public seeIfDotCollidesWithTail(dot: Phaser.Point) {
        var startPt: SnakeTail = this;
        var nextPt = startPt.next;
        while (startPt != nextPt && nextPt != null && startPt != null) { //traverse line segments to see if there is a collision
            if (startPt.x == startPt.next.x) {
                nextPt = startPt.next.getNextSameX();
            } else if (startPt.y == startPt.next.y) {
                nextPt = startPt.next.getNextSameY();
            }
            if ((dot.x == nextPt.x && dot.x == startPt.x && ((dot.y < startPt.y && dot.y > nextPt.y) || (dot.y > startPt.y && dot.y < nextPt.y))) ||
                (dot.y == nextPt.y && dot.y == startPt.y && ((dot.x < startPt.x && dot.x > nextPt.x) || (dot.x > startPt.x && dot.x < nextPt.x)))) {
                return true;
            }
            startPt = nextPt;
            nextPt = startPt.next;
        }

        return false;
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