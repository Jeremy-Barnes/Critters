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
   // myGID: string;


    getID() {
        var self = this;
        Application.getGameSecureID().done(() => {
            self.client = self.app.secureID;
           // self.x = new WebSocket("ws://localhost:8080/api/session/"+self.app.secureID);
        });
    }

    createGame() {
        var self = this;
        Application.openGameServer(0, this.client, "2v2 NR 30 Min").done(s => {
          //  self.myGID = s;
            self.x = new WebSocket("ws://localhost:8080/api/session/" + self.app.secureID);
            self.x.onmessage = function (evt) {
                alert(evt);
                self.x.send("true");
            };

        });
    }

    getOtherGame() {
        var self = this;
        Application.getUsernameGames(0, "battlebarnes").done(s => {
            alert(s);
            Application.connectToGameServer(s, self.client).done(s2 => alert(s2));
        });
        //this.x.send("1:0:2v2NR30Mins Lets Go");
    }



}
