package com.critters.spaceinvaders.entities.projectiles;

import static com.critters.spaceinvaders.graphics.Render.sr;

import java.util.ArrayList;

<<<<<<< HEAD
=======
import com.badlogic.gdx.Gdx;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Projectile extends Collidable {

	protected Entity owner;

<<<<<<< HEAD
	protected Vector2f vel;
=======
	protected Vector2f vel; // Per second in each direction
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

	public Projectile(Level level, Entity owner, Vector2f pos, Vector2f size, Vector2f vel) {
		super(level, pos, size);
		this.vel = vel;
		this.owner = owner;
	}

	protected void checkCollisions() {
		ArrayList<Collidable> collidables = level.getCollidables();

		for (Collidable c : collidables) {
			if (c.getRectangle().intersectsRect(this.rectangle) && c != owner && c != this) {
				c.hit(this);
				
				level.removeEntity(this);
				
				break;
			}
		}
	}

	@Override
	public void update() {
<<<<<<< HEAD
		pos = pos.add(vel);
=======
		pos = pos.add(vel.mul(Gdx.graphics.getDeltaTime()));
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

		rectangle.update(pos, pos.add(size));

		// Check if it hit anything
		checkCollisions();
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.75f, 0.75f, 0.75f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
