package com.critters.breakout.entities.powerup;

import static com.critters.breakout.level.Level.level;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.graphics.Polygon;
import com.critters.breakout.graphics.Render;
import com.critters.breakout.math.Rectangle;
import com.critters.breakout.math.Vector2f;

public class Powerup extends Entity {

	// Probably a temp. color variable. Should get some sprites some time.
	protected Color color;

	protected final float LENGTH;
	protected float time = 0;
	protected float fallSpeed = 150; // Per second

	public boolean pickedUp = false;
	public boolean ended = false;

	public Rectangle rect;

	public Powerup(Vector2f pos, int length) {
		super(pos);
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
		if (!pickedUp)
			pos.y -= fallSpeed * Gdx.graphics.getDeltaTime();

		if (pos.y < -20)
			level.removeEntity(this);

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
	public static boolean exists(Class<? extends Powerup> someClass) {
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
	public static int count(Class<? extends Powerup> someClass) {
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
