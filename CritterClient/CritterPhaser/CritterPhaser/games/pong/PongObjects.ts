import {Player, User, GameObject, SocketGameResponse} from '../SocketGameResponse'

export class PongBall extends GameObject {
    BALL_DIAMETER: number;
    BALL_VELOCITY: number;
}

export class PongPaddle extends GameObject {
    PADDLE_HEIGHT: number;
    PADDLE_VELOCITY: number;
}