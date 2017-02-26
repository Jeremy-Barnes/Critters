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
    activeGame: GameThumbnail;
    games: GameThumbnail[];
    ngOnInit() { this.user = this.app.user; }

    constructor(private route: ActivatedRoute) {
        Application.getGames();
        this.games = this.app.games;

    }

    selectGame(game: GameThumbnail) {
        Application.getView(game.gameURL).done((html: any) => {
            jQuery("#gameBind").html(html);
        });
        this.activeGame = game;
    }

}
