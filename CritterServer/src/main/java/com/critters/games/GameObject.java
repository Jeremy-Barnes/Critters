package com.critters.games;

/**
 * Created by Jeremy on 7/22/2017.
 */
public abstract class GameObject {
	public int x;
	public int y;
	public int z;
	public int xVector;
	public int yVector;
	public int zVector;
	public boolean needsUpdate;

	public int GAME_INSTANCE_ID = -1;


	public abstract int getEntityID();

	public void setInstanceID(int id){
		GAME_INSTANCE_ID = id;
	}

	public int getInstanceID() {
		return GAME_INSTANCE_ID;
	}

}
