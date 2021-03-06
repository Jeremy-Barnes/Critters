package com.critters.breakout.entities.powerup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.math.Rectangle;
import com.critters.breakout.math.Vector2f;

import static com.critters.breakout.graphics.Render.sr;
import static com.critters.breakout.level.Level.level;

import java.util.ArrayList;

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
	 * Method called from paddle when it intersects the powerup. Move the object into the active powerups group.
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
			time+= Gdx.graphics.getDeltaTime();

			if (time >= LENGTH)
				level.removePowerup(this);
		}

		rect.update(pos.add(-7f), pos.add(7f));
	}

	@Override
	public void render(SpriteBatch render) {
		// Don't render if it has been picked up
		if (pickedUp)
			return;

		sr.setColor(color);
		sr.triangle(pos.x - 7.5f, pos.y - +6.5f, pos.x + 7.5f, pos.y - +6.5f, pos.x, pos.y + 6.5f);
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
