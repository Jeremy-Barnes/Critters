import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser'
import { ActivatedRoute, Params, Router } from '@angular/router';
import { User, GameThumbnail, GamesInfo } from '../dtos'
import {Application} from "../appservice"
import 'rxjs/add/operator/switchMap';

@Component({
    templateUrl: "../../templates/games.template.htm"
})

export class GamesComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    activeGame: GameThumbnail = new GameThumbnail();
    activeURL: any = null;
    specialGame: GameThumbnail = {
        gameDescription : "Halo: Combat Evolved takes place in a science fiction universe created by Bungie Studios. According to the story, the realization of faster-than-light travel has allowed the human race to colonize other planets after the overpopulation of Earth. A keystone of these efforts is the planet Reach, an interstellar naval yard and a hub of scientific and military activity.",
        gameIconPath: "http://k22.kn3.net/0CE906C3D.png",
        gameName : "Halo: Combat Evolved",
        gameThumbnailConfigID : 1,
        gameURL: "google.com",
        bannerImagePath: "http://interactive.wttw.com/sites/default/files/images/2017/03/31/Chicago-muni-flag.png",
        thumbnailImagePath1: "http://pcmedia.ign.com/pc/image/article/794/794508/halo-2-20070605063546992-000.jpg",
        thumbnailImagePath2: "https://static.giantbomb.com/uploads/original/0/5911/1141263-h2_mp_01.jpg"
    };
    games: GameThumbnail[];
    playGame: boolean = false;
    isFull = false;
    categories = ["chance", "adventure", "simple"];
    ngOnInit() {

        this.user = this.app.user;
        var self = this;
        this.route.params
            .switchMap((params: Params) => {
                self.resolveGames();
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
                    thumbnailImagePath2: "https://static.giantbomb.com/uploads/original/0/5911/1141263-h2_mp_01.jpg"
                });

                for (var i = 0; i < self.games.length; i++) {
                    if (self.games[i].gameThumbnailConfigID == params['id']) return Promise.resolve(self.games[i]);
                }
                return Promise.resolve(self.activeGame);

            }).subscribe(
            game => {
                self.activeGame = game
            }
        );
    }

    constructor(private route: ActivatedRoute, private router: Router, private domSanitizer: DomSanitizer) {
        var self = this;
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
        this.playGame = !this.playGame;
    }


    resolveGames(): JQueryPromise<GamesInfo> {
        var self = this;
        return Application.getGames().done(() => { self.games = self.app.games; });
    }


    //findGame(id: number): Promise<GameThumbnail> {
    //    var self = this;
    //    return new Promise(resolve => {
    //        resolve(() => {
    //            for (var i = 0; i < self.games.length; i++) {
    //                if (self.games[i].gameThumbnailConfigID == id) return self.games[i];
    //            }
    //            return null;
    //        });
    //    });
    //}

}
