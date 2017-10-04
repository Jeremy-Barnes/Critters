package com.critters.games.pong;

import com.critters.games.GameController;
import com.critters.games.GameObject;
import com.critters.games.sockets.Player;
import com.critters.games.sockets.SocketGameRequest;
import com.critters.games.sockets.SocketGameResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 7/16/2017.
 */
public class PongController extends GameController {

	public enum PongEntityTypes {
		Paddle,
		Ball
	}

	private final int RIGHT_X = 150;
	private final int LEFT_X = 0;
	private final int TOP_Y = 100;
	private final int BOTTOM_Y = 0;

	public PongController(String gameID, String title, String hostClientID, int gameType) {
		super(gameID, title, hostClientID, gameType);
	}

	private List<GameObject> world = new ArrayList<GameObject>();

	public void initializeGame(){
		setUpPhysicsBodies();
		SocketGameResponse r = new SocketGameResponse();
		r.deltaPlayers = new ArrayList<Player>(super.clientIDToPlayer.values());
		r.deltaObjects = this.world;
		r.startTickingNow = true;
		r.tickNumber = 0;
		for(Player p : super.clientIDToPlayer.values()){
			p.sendMessage(r);
		}
	}

	private void setUpPhysicsBodies(){
		int physicsplayers = 0; //maybe spectators?
		for(Player p : super.clientIDToPlayer.values()) {
			if(physicsplayers >= 2) break;

			p.physicsComponent = new PongPaddle();
			if(p.clientID.equalsIgnoreCase(hostID)) {
				p.physicsComponent.y = TOP_Y/2;
				p.physicsComponent.x = LEFT_X;
			} else {
				p.physicsComponent.y = TOP_Y/2;
				p.physicsComponent.x = RIGHT_X;
			}
			p.physicsComponent.setInstanceID(physicsplayers + 1); //could be rand int???
			physicsplayers++;
		}

		PongBall lead = new PongBall();
		lead.x = (RIGHT_X - LEFT_X)/2;
		lead.y = (TOP_Y - BOTTOM_Y)/2;
		lead.setInstanceID(3); //could be rand int???
		world.add(lead);
	}

	public void tick(int dT){
		movePaddles(dT);
		moveBall(dT);
	}

	public void resolvePlayerCommand(SocketGameRequest request, Player player) {
		if(request.commands != null && request.commands.length > 0) {
			((PongPaddle)player.physicsComponent).yVector = 0;
			for(String cmd : request.commands) {
				if(cmd.equalsIgnoreCase("W")) {
					((PongPaddle)player.physicsComponent).yVector += 1;
				}
				if(cmd.equalsIgnoreCase("S")) {
					((PongPaddle)player.physicsComponent).yVector -= -1;
				}
			}
		}
	}

	void moveBall(int dT) {
		PongBall lead = (PongBall)world.get(0);
		lead.x += dT/1000 * lead.xVector * lead.BALL_VELOCITY;
		lead.y += dT/1000 * lead.yVector * lead.BALL_VELOCITY ;

		if(lead.x < LEFT_X) {
			scoreP2();
		}
		if(lead.x > RIGHT_X) {
			scoreP1();
		}

		if(lead.y > TOP_Y || lead.y  < BOTTOM_Y) {
			lead.yVector *= -1;
		}
	}

	private void movePaddles(int dT){



	}


	private void scoreP1(){

	}

	private void scoreP2(){

	}
	
}
