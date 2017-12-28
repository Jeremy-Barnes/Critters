import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser'
import { ActivatedRoute, Params, Router } from '@angular/router';
import { User, GameThumbnail, GamesInfo, GameSocketResponse, UINotification } from '../dtos'
import {Application} from "../appservice"
import '../../node_modules/rxjs/add/operator/switchMap';

@Component({
    templateUrl: "../../templates/games.template.htm"
})

export class GamesComponent implements OnInit {
    /*********** Controller View Variables **************/
    user: User;
    app: Application = Application.getApp();
    activeGame: GameThumbnail = new GameThumbnail();
    visibleLobbies: Array<any> = [];
    games: GameThumbnail[];
    categories = ["chance", "adventure", "simple"];
    displayPlayPage: boolean = false;

    specialGame: GameThumbnail = {
        gameDescription: "Halo: Combat Evolved takes place in a science fiction universe created by Bungie Studios. According to the story, the realization of faster-than-light travel has allowed the human race to colonize other planets after the overpopulation of Earth. A keystone of these efforts is the planet Reach, an interstellar naval yard and a hub of scientific and military activity.",
        gameIconPath: "http://k22.kn3.net/0CE906C3D.png",
        gameName: "Halo: Combat Evolved",
        gameThumbnailConfigID: 1,
        gameURL: "google.com",
        bannerImagePath: "http://interactive.wttw.com/sites/default/files/images/2017/03/31/Chicago-muni-flag.png",
        thumbnailImagePath1: "http://pcmedia.ign.com/pc/image/article/794/794508/halo-2-20070605063546992-000.jpg",
        thumbnailImagePath2: "https://static.giantbomb.com/uploads/original/0/5911/1141263-h2_mp_01.jpg",
        isMultiplayer: true
    };


    /*********** Game Lobby Variables **************/
    gameLobbyName: string = "";
    clientID: string;
    isFull = false;
    creating: boolean = false;
    /* Notification Information */
    notification: UINotification;
    playersInTheLobby: Array<any> = [];


    /*********** Other Game Technical Variables **************/
    gameWebsocketConnection: WebSocket;

    ngOnInit() {

        this.user = this.app.user;
        var self = this;
        //cast only so VS will quit complaining about not being able to find switchMap EVEN THOUGH IT COMPILES JUST FINE
        (<any>this.route.params).switchMap((params: Params) => {
                self.resolveGames();
            //todo remove this test data
                var gth = new GameThumbnail();
                gth.gameDescription = "sdfsd";
                gth.gameName = "sdfsdf";
                gth.gameIconPath = "http://thatgamecompany.com/wp-content/themes/thatgamecompany/_include/img/journey/journey-game-screenshot-13.jpg"
                gth.gameThumbnailConfigID = 2;
                gth.gameURL = "http://www.homestarrunner.com/intro.html";
                self.games.push(gth);

                self.games.push({
                    gameDescription: "Halo: Combat Evolved takes place in a science fiction universe created by Bungie Studios. According to the story, the realization of faster-than-light travel has allowed the human race to colonize other planets after the overpopulation of Earth. A keystone of these efforts is the planet Reach, an interstellar naval yard and a hub of scientific and military activity.",
                    gameIconPath: "http://k22.kn3.net/0CE906C3D.png",
                    gameName: "Halo: Combat Evolved",
                    gameThumbnailConfigID: 1,
                    gameURL: "http://www.homestarrunner.com",
                    bannerImagePath: "http://interactive.wttw.com/sites/default/files/images/2017/03/31/Chicago-muni-flag.png",
                    thumbnailImagePath1: "http://pcmedia.ign.com/pc/image/article/794/794508/halo-2-20070605063546992-000.jpg",
                    thumbnailImagePath2: "https://static.giantbomb.com/uploads/original/0/5911/1141263-h2_mp_01.jpg",
                    isMultiplayer: true
                });
            //todo remove test data above
                for (var i = 0; i < self.games.length; i++) {
                    if (self.games[i].gameThumbnailConfigID == params['id']) return Promise.resolve(self.games[i]);
                }
                return Promise.resolve(self.activeGame);

            }).subscribe(
            (game: GameThumbnail) => {
                self.activeGame = game;
            }
        );
    }

    constructor(private route: ActivatedRoute, private router: Router, private domSanitizer: DomSanitizer) {
        var self = this;
        self.games = self.app.games;
        Application.getGames().done(() => {
            self.games = self.app.games;
        });
       // this.games = this.app.games;
        var gth = new GameThumbnail();
        gth.gameDescription = "sdfsd";
        gth.gameName = "sdfsdf";
        gth.gameThumbnailConfigID = 2;

        this.games.push(gth);
        this.games.push({
            gameDescription: "Halo: Combat Evolved takes place in a science fiction universe created by Bungie Studios. According to the story, the realization of faster-than-light travel has allowed the human race to colonize other planets after the overpopulation of Earth. A keystone of these efforts is the planet Reach, an interstellar naval yard and a hub of scientific and military activity.",
            gameIconPath: "http://k22.kn3.net/0CE906C3D.png",
            gameName: "Halo: Combat Evolved",
            gameThumbnailConfigID: 1,
            gameURL: "http://www.homestarrunner.com/",
            isMultiplayer: true,
            bannerImagePath: "http://interactive.wttw.com/sites/default/files/images/2017/03/31/Chicago-muni-flag.png",
            thumbnailImagePath1: "http://pcmedia.ign.com/pc/image/article/794/794508/halo-2-20070605063546992-000.jpg",
            thumbnailImagePath2: "https://static.giantbomb.com/uploads/original/0/5911/1141263-h2_mp_01.jpg"
        });
    }

    toggleFull() {
        this.isFull = !this.isFull;
    }

    selectGame(game: GameThumbnail) {
        let link = ['games/' + game.gameThumbnailConfigID];
        this.router.navigate(link);
    }

    togglePlayGame() {
        this.displayPlayPage = !this.displayPlayPage;

        if (this.activeGame.isMultiplayer) {
            var self = this;
            Application.getGameSecureID().done(() => {
                self.clientID = self.app.secureID;
            });
        }      
    }

    fetchGameLobbies() {
        var self = this;
        Application.getActiveGames(this.activeGame.gameThumbnailConfigID).done((lobbies: any) => {
            self.visibleLobbies.push(...lobbies.runningGames);
        });
    }

    startCreatingLobby() {
        this.creating = true;
    }

    createGame() {
        var self = this;
        Application.openGameServer(this.activeGame.gameThumbnailConfigID, this.clientID, this.gameLobbyName).then(s => {
            self.gameWebsocketConnection = new WebSocket("ws://localhost:8080/api/session/" + self.app.secureID);
           // self.gameWebsocketConnection.send(JSON.stringify({ ping: true }));
            self.gameWebsocketConnection.onclose = function (evt) {
                alert(evt);
            }

            this.createSocketOnMessage(self.gameWebsocketConnection);
        });
    }

    respond(response: boolean) {
        this.gameWebsocketConnection.send(JSON.stringify({ notificationResponse: true, notificationID: this.notification.notificationID }));
        this.notification = null;
    }

    joinLobby(gameID: string) {
        var self = this;
        Application.connectToGameServer(gameID, this.clientID).done((o: any) => {
            alert("welcome to the party");
            self.gameWebsocketConnection = new WebSocket("ws://localhost:8080/api/session/" + self.app.secureID)
            this.createSocketOnMessage(self.gameWebsocketConnection);
        }).fail(() => { alert("you were rejected"); });
    }

    createSocketOnMessage(socket: WebSocket) {
        var self = this;
        socket.onmessage = (evt: any) => {
            //   self.gameWebsocketConnection.send(JSON.stringify({ acceptedUsers: [evt.data] }));
            var req: GameSocketResponse = JSON.parse(evt.data);
            if (req.notificationBody) {
                self.notification = new UINotification();
                self.notification.body = req.notificationBody;
                self.notification.title = req.notificationTitle;
                self.notification.dangerButtonText = req.dangerButtonText;
                self.notification.noButtonText = req.noButtonText;
                self.notification.notificationID = req.notificationID;
            }
            if (req.deltaPlayers) {
                self.playersInTheLobby.push(req.broadCastMessage);
            }
        };
    }

    startGame() {
        var gamesAngularComponent = this;
        SystemJS.import('../../Libraries/phaser.min.js').then(Phaser => {
            SystemJS.import('../../games/pong/gameLauncher.js').then(
                game => {
                    gamesAngularComponent.gameWebsocketConnection.send(JSON.stringify({ startGame: true }));
                    game.GameLauncher(gamesAngularComponent.gameWebsocketConnection);
                });
        });
    }

    resolveGames(): JQueryPromise<GamesInfo> {
        var self = this;
        return Application.getGames().done(() => { self.games = self.app.games; });
    }
}
