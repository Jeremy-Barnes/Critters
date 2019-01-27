package com.critters.games.pong;

import com.critters.games.GameController;
import com.critters.games.GameObject;
import com.critters.games.sockets.Player;
import com.critters.games.sockets.SocketGameRequest;
import com.critters.games.sockets.SocketGameResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 7/16/2017.
 */
public class PongController extends GameController {

	static final Logger logger = LoggerFactory.getLogger("application");

	public enum PongEntityTypes {
		Paddle,
		Ball
	}

	private final int RIGHT_X = 1500;
	private final int LEFT_X = 0;
	private final int TOP_Y = 1000;
	private final int BOTTOM_Y = 0;

	private int tickCount = 0;
	public long startGameTimestamp;
	public int MAX_SCORE = 10;
	public long maxTime = 10*60*1000; //min*sec*milisec

	public PongController(String gameID, String title, String hostClientID, int gameType) {
		super(gameID, title, hostClientID, gameType);
	}

	private List<GameObject> world = new ArrayList<GameObject>();

	public void initializeGame(){
		initializePhysicsBodies();
		positionPhysicsBodies();
		this.startGameTimestamp = System.currentTimeMillis();
		SocketGameResponse setupMessage = new SocketGameResponse();

		setupMessage.deltaPlayers = new ArrayList<Player>(super.clientIDToPlayer.values());
		setupMessage.deltaObjects = this.world;
		setupMessage.startTickingNow = true;
		setupMessage.tickNumber = 0;
		setupMessage.startTime = startGameTimestamp;
		setupMessage.currentTime = startGameTimestamp;
		for(Player p : super.clientIDToPlayer.values()){
			setupMessage.assignedInstanceId = p.physicsComponent.getInstanceID();
			p.sendMessage(setupMessage);
		}
	}

	private void initializePhysicsBodies(){
		int physicsPlayers = 0; //maybe spectators?
		for(Player p : super.clientIDToPlayer.values()) {
			if(physicsPlayers >= 2) break;

			p.physicsComponent = new PongPaddle();
			((PongPaddle)p.physicsComponent).boundingBox = new Rectangle();
			p.physicsComponent.setInstanceID(physicsPlayers + 1); //could be rand int???
			physicsPlayers++;
			world.add(p.physicsComponent);
		}

		PongBall pongBall = new PongBall();
		pongBall.setInstanceID(3); //could be rand int???
		world.add(pongBall);
	}

	private void positionPhysicsBodies() {
		for (Player p : super.clientIDToPlayer.values()) {
			if (p.clientID.equalsIgnoreCase(hostID)) {
				p.physicsComponent.y = TOP_Y / 2;
				p.physicsComponent.x = LEFT_X  + 100;
			} else {
				p.physicsComponent.y = TOP_Y / 2;
				p.physicsComponent.x = RIGHT_X - 100;
			}
			p.physicsComponent.needsUpdate = true;

			((PongPaddle) p.physicsComponent).boundingBox.setRect(p.physicsComponent.x, p.physicsComponent.y, 1, ((PongPaddle) p.physicsComponent).PADDLE_HEIGHT);
		}
		PongBall ball = (PongBall)world.get(world.size()-1);
		ball.xVector = 0;
		ball.yVector = 0;
		ball.x = (RIGHT_X - LEFT_X)/2;
		ball.y = (TOP_Y - BOTTOM_Y)/2;
	}

	public void tick(int dT){
		tickCount++;
		movePaddles(dT);
		moveBall(dT);
		if(lastTime - startGameTimestamp > maxTime){
			gameIsOver();
		}
		if(tickCount % 5 == 0) {
			sendPlayersGameState();
		}
	}

	public void resolvePlayerCommand(SocketGameRequest request, Player player) {
		if(request.commands != null && request.commands.length > 0) {
			player.physicsComponent.yVector = 0;
			player.physicsComponent.needsUpdate = true;

			for(String cmd : request.commands) {
				if(cmd.equalsIgnoreCase("W")) {
					((PongPaddle)player.physicsComponent).yVector = -1;
				}
				if(cmd.equalsIgnoreCase("S")) {
					((PongPaddle)player.physicsComponent).yVector = 1;
				}
				if(cmd.equalsIgnoreCase("WS")) {
					((PongPaddle)player.physicsComponent).yVector = 0;
				}
				if(player.clientID.equalsIgnoreCase(this.hostID) && cmd.equalsIgnoreCase(" ")) {
					PongBall ball = (PongBall) this.world.get(world.size() - 1);
					if(ball.xVector == 0) {
						ball.xVector = (float)Math.signum(Math.random() - 0.5);
						ball.yVector = (float) (Math.signum(Math.random() - 0.5) * Math.random());
					}
				}
			}
			if(request.clientState != null) //todo lag compensation
				player.physicsComponent.clientNextY = request.clientState.y;
		}
	}

	private void moveBall(int dT) {
		PongBall ball = (PongBall)world.get(world.size()-1);
		ball.x += (dT * ball.xVector * ball.BALL_VELOCITY);
		ball.y += (dT * ball.yVector * ball.BALL_VELOCITY);

		if(ball.x < LEFT_X) {
			score(false);
		} else if(ball.x > RIGHT_X) {
			score(true);
		} else { //collision detect!

			for(Player p : super.clientIDToPlayer.values()) {
				PongPaddle paddle = (PongPaddle) p.physicsComponent;
				if(paddle.yVector != 0 && paddle.boundingBox.intersects(ball.x - ball.BALL_DIAMETER/2, ball.y - ball.BALL_DIAMETER/2, ball.BALL_DIAMETER, ball.BALL_DIAMETER )) {
					ball.xVector *= -1;
					ball.yVector = (float) (((ball.y - paddle.boundingBox.getCenterY()) / (paddle.PADDLE_HEIGHT / 6)) * ball.BALL_VELOCITY) + (paddle.yVector * paddle.PADDLE_VELOCITY);
					ball.x += (dT * ball.xVector * ball.BALL_VELOCITY);
					ball.y += (dT * ball.yVector * ball.BALL_VELOCITY);
					break;
				}
			}

			if (ball.y >= TOP_Y || ball.y <= BOTTOM_Y) {
				ball.yVector *= -1;
				ball.y += (dT * ball.yVector * ball.BALL_VELOCITY);
			}
		}
	}

	private void movePaddles(int dT) {
		for(Player p : super.clientIDToPlayer.values()) {
			PongPaddle paddle = (PongPaddle) p.physicsComponent;
			if(paddle.yVector != 0) {
				paddle.needsUpdate = true;
				paddle.y += paddle.yVector * paddle.PADDLE_VELOCITY * dT;
				paddle.boundingBox.setRect(paddle.x, paddle.y, 1, paddle.PADDLE_HEIGHT);

				if (paddle.boundingBox.getMinY() < this.BOTTOM_Y) { //colliding with top or bottom?
					paddle.y = this.BOTTOM_Y;
					paddle.boundingBox.setRect(paddle.x, paddle.y, 1, paddle.PADDLE_HEIGHT);
				} else if(paddle.boundingBox.getMaxY() > this.TOP_Y) {
					paddle.y = this.TOP_Y - paddle.PADDLE_HEIGHT;
					paddle.boundingBox.setRect(paddle.x, paddle.y, 1, paddle.PADDLE_HEIGHT);
				}
			}
		}
	}

	private void sendPlayersGameState(){
		SocketGameResponse r = new SocketGameResponse();
		r.tickNumber = this.tickCount;
		r.currentTime = System.currentTimeMillis();
		r.deltaObjects.add(this.world.get(world.size()-1));//da ball
		for(Player p : super.clientIDToPlayer.values()) {
			if (p.physicsComponent.needsUpdate) {
				p.physicsComponent.needsUpdate = false;
				r.deltaPlayers.add(p);
			}
		}
		for(Player p : super.clientIDToPlayer.values()) {
			p.sendMessage(r);
		}
	}

	private void score(boolean leftSidePlayerScored){
		for(Player p : super.clientIDToPlayer.values()) {
			if (leftSidePlayerScored && p.physicsComponent.getInstanceID() == 1) {
				p.score++;
				if(p.score == this.MAX_SCORE) {
					gameIsOver();
				}
				break;
			} else if (!leftSidePlayerScored && p.physicsComponent.getInstanceID() == 2) {
				p.score++;
				if(p.score == this.MAX_SCORE) {
					gameIsOver();
				}
				break;
			}
		}
		positionPhysicsBodies();
		sendPlayersGameState();
	}

	public void handlePostGameEvents() {
		Player winner = null;
		int scoreToBeat = 0; //only search list of players once, ties != wins

		for(Player p : super.clientIDToPlayer.values()) {
			if(p.score == this.MAX_SCORE) {
				winner = p;
			} else if((winner == null && scoreToBeat < p.score) || winner.score < p.score) {
				winner = p;
			} else if(winner.score == p.score) {
				winner = null;
				scoreToBeat = p.score; //tie detected, prevent future ties.
			}
		}
		SocketGameResponse r = new SocketGameResponse();
		if(winner != null) {
			//give the winner some coin! TODO
		}
		r.gameOver = true;
		r.tickNumber = this.tickCount;
		r.currentTime = System.currentTimeMillis();
		r.deltaPlayers = new ArrayList<Player>(super.clientIDToPlayer.values());
		for(Player p : super.clientIDToPlayer.values()) {
			p.sendMessage(r);
		}

	}
}
