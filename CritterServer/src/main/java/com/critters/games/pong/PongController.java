package com.critters.games.pong;

import com.critters.games.GameController;
import com.critters.games.GameObject;
import com.critters.games.sockets.Player;
import com.critters.games.sockets.SocketGameRequest;
import com.critters.games.sockets.SocketGameResponse;

import java.awt.*;
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

	private int tickCount = 0;

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
			r.assignedInstanceId = p.physicsComponent.getInstanceID();
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
			((PongPaddle)p.physicsComponent).boundingBox = new Rectangle();
			((PongPaddle)p.physicsComponent).boundingBox.setRect(p.physicsComponent.x,
																 p.physicsComponent.y - ((PongPaddle)p.physicsComponent).PADDLE_HEIGHT/2, 1,
																 ((PongPaddle)p.physicsComponent).PADDLE_HEIGHT);
			p.physicsComponent.setInstanceID(physicsplayers + 1); //could be rand int???
			physicsplayers++;
		}

		PongBall lead = new PongBall();
		lead.x = (RIGHT_X - LEFT_X)/2;
		lead.y = (TOP_Y - BOTTOM_Y)/2;
		lead.xVector = (int)Math.signum(Math.random() - 0.5);
		lead.setInstanceID(3); //could be rand int???
		world.add(lead);
	}

	public void tick(int dT){
		tickCount++;
		movePaddles(dT);
		moveBall(dT);
		if(tickCount % 10 == 0) {
			sendPlayersGameState();
		}
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

	private void moveBall(int dT) {
		PongBall lead = (PongBall)world.get(0);
		lead.x += dT/1000 * lead.xVector * lead.BALL_VELOCITY;
		lead.y += dT/1000 * lead.yVector * lead.BALL_VELOCITY ;

		if(lead.x < LEFT_X) {
			scoreP2();
		} else
		if(lead.x > RIGHT_X) {
			scoreP1();
		} else { //collision detect!
			for(Player p : super.clientIDToPlayer.values()) {
				if(((PongPaddle)p.physicsComponent).boundingBox.intersects(lead.x - lead.BALL_DIAMETER/2, lead.y - lead.BALL_DIAMETER/2, lead.BALL_DIAMETER, lead.BALL_DIAMETER )) {
					lead.xVector *= -1;
				}
			}

			if (lead.y > TOP_Y || lead.y < BOTTOM_Y) {
				lead.yVector *= -1;
			}
		}
	}

	private void movePaddles(int dT) {
		for(Player p : super.clientIDToPlayer.values()) {
			PongPaddle paddle = (PongPaddle) p.physicsComponent;
			if(paddle.yVector != 0) {
				paddle.y += paddle.yVector * paddle.PADDLE_VELOCITY * dT/1000;
				paddle.boundingBox.setRect(paddle.x, paddle.y - paddle.PADDLE_HEIGHT / 2, 1, paddle.PADDLE_HEIGHT);
				paddle.needsUpdate = true;
			}
		}
	}

	private void sendPlayersGameState(){
		SocketGameResponse r = new SocketGameResponse();
		r.tickNumber = this.tickCount;
		r.deltaObjects.add(this.world.get(0));//da ball
		for(Player p : super.clientIDToPlayer.values()) {
			if (p.physicsComponent.needsUpdate) {
				r.deltaPlayers.add(p);
				p.physicsComponent.needsUpdate = false;
			}
		}
		for(Player p : super.clientIDToPlayer.values()) {
			p.sendMessage(r);
		}
	}

	private void scoreP1(){

	}

	private void scoreP2(){

	}

}
