package com.critters.spaceinvaders.entities.powerup;

import static com.critters.spaceinvaders.graphics.Render.sr;
import static com.critters.spaceinvaders.level.Level.level;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.math.Rectangle;
import com.critters.spaceinvaders.math.Vector2f;



public class Powerup extends Entity {

	// Probably a temp. color variable. Should get some sprites some time.
	protected Color color;

	protected final int LENGTH;
	protected int time = 0;

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
			pos.y -= 2;

		if (pos.y < -20)
			level.removeEntity(this);

		if (pickedUp) {
			time++;

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
