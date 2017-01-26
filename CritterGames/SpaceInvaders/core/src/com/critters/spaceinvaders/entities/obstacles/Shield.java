package com.critters.spaceinvaders.entities.obstacles;

<<<<<<< HEAD
=======
import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.entities.mobs.Alien;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Shield extends Wall {

	public Shield(Level level, Vector2f pos, Vector2f size) {
		super(level, pos, size);
	}

	@Override
	public void update() {
<<<<<<< HEAD

		if (hitCount >= 1) {
			level.removeEntity(this);
		}
=======
		if (hitCount >= 1) {
			level.removeEntity(this);
		}

		for (int i = 0; i < level.getEntities().size(); i++) {
			Entity e = level.getEntities().get(i);
			if (e instanceof Alien && ((Alien) e).getRectangle().intersectsRect(rectangle))
				level.removeEntity(this);
		}
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	}

}
