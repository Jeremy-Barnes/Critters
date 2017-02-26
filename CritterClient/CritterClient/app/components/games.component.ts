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
        //Application.getView(game.gameURL).done((html: any) => {
        //    jQuery(\"#gameBind\").html(html);
        //});
        this.activeGame = game;
     //   var ifrm = document.createElement('iframe');
        document.getElementById("gameBind").insertBefore
        jQuery('#gameBind').append("butts");
       // jQuery('#gameBind').append("<iframe><script src=\"dist/html/html.nocache.js\"></script> <script src= \"Libraries/soundmanager2-setup.js\" ></script> <script type= \"text/javascript\" src= \"Libraries/soundmanager2-jsmin.js\" > </script><script> function handleMouseDown(evt) { evt.preventDefault(); evt.stopPropagation(); evt.target.style.cursor = 'default';} function handleMouseUp(evt) {evt.preventDefault();        evt.stopPropagation();       evt.target.style.cursor = '';   } document.getElementById('embed-html').addEventListener('mousedown', handleMouseDown, false);document.getElementById('embed-html').addEventListener('mouseup', handleMouseUp, false);</script>");
    }

}
