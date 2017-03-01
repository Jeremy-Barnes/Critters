import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { User, GameThumbnail, GamesInfo } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/games.template.htm"
})

export class GamesComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    activeGame: GameThumbnail = new GameThumbnail();
    games: GameThumbnail[];
    ngOnInit() { this.user = this.app.user; }
    isFull = false;


    constructor(private route: ActivatedRoute) {
        Application.getGames();
        this.games = this.app.games;
        var gth = new GameThumbnail();
        gth.gameDescription = "sdfsd";
        gth.gameName = "sdfsdf";
        this.games.push(gth);

    }

    goFull() {
        this.isFull = true;
    }

    selectGame(game: GameThumbnail) {
        this.activeGame = game;
    }

}
