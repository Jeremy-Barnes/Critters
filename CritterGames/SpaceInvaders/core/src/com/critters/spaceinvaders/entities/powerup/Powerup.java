package com.critters.spaceinvaders.entities.powerup;

import static com.critters.spaceinvaders.graphics.Render.sr;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Rectangle;
import com.critters.spaceinvaders.math.Vector2f;

public class Powerup extends Entity {

	// Probably a temp. color variable. Should get some sprites some time.
	protected Color color;

	protected final int LENGTH; // In seconds
	protected int time = 0;

	public boolean pickedUp = false;
	public boolean ended = false;
	private final float yVel = 120;// Per second

	public Rectangle rect;

	public Powerup(Level level, Vector2f pos) {
		this(level, pos, 10000);
	}

	public Powerup(Level level, Vector2f pos, int length) {
		super(level, pos);
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
			pos.y -= yVel * Gdx.graphics.getDeltaTime();

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
	public void render(SpriteBatch render) {
		// Don't render if it has been picked up
		if (pickedUp)
			return;

		sr.setColor(color);
		sr.triangle(pos.x - 7.5f, pos.y - +6.5f, pos.x + 7.5f, pos.y - +6.5f, pos.x, pos.y + 6.5f);
	}

	/**
	 * Deactivate the powerup, useful mostly for powerups that don't have a time limit, but have, say, a number of uses.
	 */
	public void deactivate() {
		level.removePowerup(this);
	}

	/**
	 * Check if the given powerup is currently active
	 */
	public static boolean exists(Level level, Class<? extends Powerup> someClass) {
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
	public static int count(Level level, Class<? extends Powerup> someClass) {
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
