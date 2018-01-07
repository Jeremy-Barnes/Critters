export class SocketGameResponse {
    friendshipID: number;
    requester: User;
    requested: User;
    accepted: boolean;
    dateSent: Date;


    connectToGameRequesters : Array<Player>;

    /*** Initialization variables ***/
    startTickingNow: boolean;
    tickingPaused: boolean;
    gameOver: boolean;
    
    assignedInstanceId: number;
    
    notificationBody: string;

    notificationTitle: string;
    notificationHTML: string;
    dangerButtonText: string;
    noButtonText: string;
    notificationID: string;

    /*** Game state variables ***/
    tickNumber: number;
    startTime: number;
    currentTime: number;

    deltaObjects: Array<GameObject>;
    deltaPlayers: Array<Player>;

    broadCastMessage: string;
    broadCaster: string;

    ping: boolean;
}

export class Player {
    user: User;
    physicsComponent: GameObject;
    score: number;
}

export class GameObject {
    x: number;
    y: number;
    z: number;
    xVector: number;
    yVector: number;
    zVector: number;
    needsUpdate: boolean;
    GAME_INSTANCE_ID: number;
    ENTITY_TYPE_ID: number;
}

export class User {
    userID: number = 0;
    userName: string = "";
    firstName: string = "";
    lastName: string = "";
    userImagePath: string = "";
}