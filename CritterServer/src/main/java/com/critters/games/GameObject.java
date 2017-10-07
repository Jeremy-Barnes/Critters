package com.critters.games;

import com.critters.games.pong.PongBall;
import com.critters.games.pong.PongPaddle;

import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Created by Jeremy on 7/22/2017.
 */
@XmlSeeAlso({PongPaddle.class, PongBall.class})
public abstract class GameObject {
	public float x;
	public float y;
	public float z;
	public float xVector;
	public float yVector;
	public float zVector;
	public boolean needsUpdate;

	public int GAME_INSTANCE_ID = -1;
	public int ENTITY_TYPE_ID;

	public void setEntityID(int id) {
	}


	public int getEntityID() {
		return ENTITY_TYPE_ID;
	}

	public void setInstanceID(int id){
		GAME_INSTANCE_ID = id;
	}

	public int getInstanceID() {
		return GAME_INSTANCE_ID;
	}

}
