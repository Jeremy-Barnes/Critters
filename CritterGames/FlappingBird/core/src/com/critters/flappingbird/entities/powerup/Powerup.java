package com.critters.flappingbird.entities.powerup;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.flappingbird.entities.Entity;
import com.critters.flappingbird.graphics.Polygon;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Rectangle;
import com.critters.flappingbird.math.Vector2f;

public class Powerup extends Entity {

	// Probably a temp. color variable. Should get some sprites some time.
	protected Color color;

	protected final float LENGTH;
	protected float time = 0;
	protected float fallSpeed = 150; // Per second

	public boolean pickedUp = false;
	public boolean ended = false;

	public Rectangle rect;

	public Powerup(Level level, Vector2f pos, int length) {
		super(level, pos);
		color = new Color(0xff00ffff);

		LENGTH = length;

		rect = new Rectangle(pos.add(-7f), pos.add(7f));
	}

	/**
	 * Method called from paddle when it intersects the powerup. Move the object
	 * into the active powerups group.
	 */
	public void pickUp() {
		pickedUp = true;
		level.removeEntity(this);
		level.addPowerup(this);
	}

	@Override
	public void update() {

		// Remove the powerup
		if (pos.y < -20)
			level.removeEntity(this);

		// Pickedup logic
		if (pickedUp) {
			time += Gdx.graphics.getDeltaTime();
			if (time >= LENGTH)
				level.removePowerup(this);
		}

		rect.update(pos.add(-7f), pos.add(7f));
	}

	@Override
	public void render(Render render) {
		// Don't render if it has been picked up
		if (pickedUp)
			return;

		float[] verts = { pos.x - 15f, pos.y - +13f, pos.x + 15f, pos.y - +13f, pos.x, pos.y + 13f };
		Polygon p = new Polygon(verts);
		render.drawPolygon(p, color, 0, 0);
	}

	/**
	 * Check if the given powerup is currently active
	 */
	public static void removeAll(Class<? extends Powerup> someClass, Level level) {
		ArrayList<Powerup> powerups = level.getActivePowerups();
		for (int i = 0; i < powerups.size(); i++) {
			Powerup p = powerups.get(i);
			if (p.getClass() == someClass) {
				level.removePowerup(p);
				i--;
			}
		}
	}

	/**
	 * Check if the given powerup is currently active
	 */
	public static boolean exists(Class<? extends Powerup> someClass, Level level) {
		ArrayList<Powerup> powerups = level.getActivePowerups();
		for (Powerup p : powerups) {
			if (p.getClass() == someClass) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check how many of the given powerup are active
	 */
	public static int count(Class<? extends Powerup> someClass, Level level) {
		ArrayList<Powerup> powerups = level.getActivePowerups();
		int count = 0;
		for (Powerup p : powerups) {
			if (p.getClass() == someClass) {
				count++;
			}
		}
		return count;
	}
}
