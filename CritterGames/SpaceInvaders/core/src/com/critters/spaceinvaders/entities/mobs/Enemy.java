package com.critters.spaceinvaders.entities.mobs;

import static com.critters.spaceinvaders.level.Level.random;

import java.util.ArrayList;

import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.entities.powerup.Powerup;
import com.critters.spaceinvaders.entities.powerup.PowerupLaser;
import com.critters.spaceinvaders.entities.powerup.PowerupLife;
import com.critters.spaceinvaders.entities.projectiles.Projectile;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public abstract class Enemy extends Collidable {

	protected ArrayList<Powerup> powerups = new ArrayList<Powerup>();

	protected int hitPoints;

	public Enemy(Level level, Vector2f pos, Vector2f size) {
		this(level, pos, size, 1);
	}

	public Enemy(Level level, Vector2f pos, Vector2f size, int hp) {
		super(level, pos, size);
		hitPoints = hp;

		if (random.nextInt(6) == 0)
			addRandomPowerup();
	}

	private void addRandomPowerup() {
		int type = random.nextInt(3);
		switch (type) {
		case 0:
			powerups.add(new PowerupLaser(level, rectangle.getCenter()));
			break;
		case 1:
			powerups.add(new PowerupLife(level, rectangle.getCenter()));
			break;

		default:
			break;
		}
	}

	@Override
	public void hit(Projectile projectile) {
		super.hit(projectile);

		if (hitCount >= hitPoints) {
			level.removeEntity(this);

			for (Powerup p : powerups)
				level.addEntity(p);
		}
	}

}
