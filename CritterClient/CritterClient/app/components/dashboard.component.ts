import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/dashboard.template.htm"
})

export class DashboardComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    ngOnInit() { this.user = this.app.user; }
    x: WebSocket;

    client: string;

    getID() {
        var self = this;
        Application.test().done(() => {
        });
    }

    createGame() {
        var self = this;
        Application.openGameServer(0, this.client, "2v2 NR 30 Min").fail(s => {
            //SystemJS.import('../../games/phaser.js').then(
            //    game =>
            //        alert(game));

            self.x = new WebSocket("ws://localhost:8080/api/session/" + self.app.secureID);

            self.x.onmessage = function (evt) {
                alert(evt.data);
                self.x.send(JSON.stringify({ acceptedUsers: [evt.data] }));
             //   self.x.send("true");
            };

            SystemJS.import('../../Libraries/phaser.min.js').then(Phaser => {
                SystemJS.import('../../games/pong/gameLauncher.js').then(

                    game => {
                   //     self.x.send(JSON.stringify({ startGame: true }));
                        game.GameLauncher(self.x)
                    });
            });

        });
    }

    getOtherGame() {

        var self = this;
        Application.getUsernameGames(0, "battlebarnes").done(s => {
            alert(s);
            Application.connectToGameServer(s, self.client).done(s2 => alert(s2));
        });
    }



}
