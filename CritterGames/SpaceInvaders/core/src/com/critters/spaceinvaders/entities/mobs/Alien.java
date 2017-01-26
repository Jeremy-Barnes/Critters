package com.critters.spaceinvaders.entities.mobs;

import static com.critters.spaceinvaders.graphics.Render.sr;

import java.util.ArrayList;
import java.util.Random;

<<<<<<< HEAD
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.projectiles.Projectile;
=======
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
import com.critters.spaceinvaders.entities.projectiles.ProjectileEnemy;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Alien extends Enemy {

	private static Random random = new Random();
	private int cooldown;

	private float originX;
	private float maxOffset = 100;
	private final int STEPS = 10;
<<<<<<< HEAD
	private final int STEPS_TIME = 10;
	private int currentStepsTime = 0;
=======
	private final float STEPS_TIME = 0.10f; // In seconds
	private float currentStepsTime = 0;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	private boolean right = true;

	public int row;
	public int column;

	private boolean canShoot;

	public Alien(Level level, Vector2f pos, Vector2f size, int row, int column) {
		this(level, pos, size, row, column, 1);
		this.row = row;
		this.column = column;

		originX = pos.x;
	}

	public Alien(Level level, Vector2f pos, Vector2f size, int row, int column, int hp) {
		super(level, pos, size, hp);
	}

<<<<<<< HEAD
	@Override
	public void hit(Projectile projectile) {
		super.hit(projectile);
		level.score++;
	}

=======
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	private void updateShoot() {
		// Only the lowest alien it it's column can shoot
		ArrayList<Alien> aliens = level.getAliensInColumn(column);
		boolean lowest = true;
		for (Alien a : aliens) {
			if (a.row < this.row && a != this)
				lowest = false;
		}
		canShoot = lowest;

		if (cooldown == 0 && canShoot) {

			if (random.nextInt(200) == 0) {
				level.addEntity(new ProjectileEnemy(level, this, getRectangle().getCenter(), new Vector2f(5, 15),
<<<<<<< HEAD
						new Vector2f(0, -3)));
=======
						new Vector2f(0, -180)));
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
				cooldown = 50;
			}

		} else {
			cooldown--;
		}
	}

	private void updatePosition() {
<<<<<<< HEAD
		currentStepsTime++;
=======
		currentStepsTime += Gdx.graphics.getDeltaTime();
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
		if (currentStepsTime >= STEPS_TIME) {
			currentStepsTime = 0;

			float step = maxOffset / STEPS;
			if (!right)
				step = -step;

			pos.x += step;

			if ((right && pos.x >= originX + maxOffset) || (!right && pos.x <= originX)) {
				right = !right;
<<<<<<< HEAD

=======
				
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
				// Switch direction and move down
				pos.y -= 10;
			}

		}
	}

	@Override
	public void update() {
		rectangle.update(pos, pos.add(size));

		updateShoot();

		updatePosition();
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.25f, 0.75f, 0.25f, 1);
		if (canShoot)
			sr.setColor(0.75f, 0.25f, 0.25f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
