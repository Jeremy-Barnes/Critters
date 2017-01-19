package com.critters.spaceinvaders.entities.mobs;

import static com.critters.spaceinvaders.graphics.Render.sr;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.projectiles.ProjectileEnemy;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Alien extends Enemy {

	private static Random random = new Random();
	private int cooldown;

	private float originX;
	private float maxOffset = 100;
	private final int STEPS = 10;
	private final float STEPS_TIME = 0.050f; // In seconds
	private float currentStepsTime = 0;
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
						new Vector2f(0, -180)));
				cooldown = 50;
			}

		} else {
			cooldown--;
		}
	}

	private void updatePosition() {
		currentStepsTime += Gdx.graphics.getDeltaTime();
		if (currentStepsTime >= STEPS_TIME) {
			currentStepsTime = 0;

			float step = maxOffset / STEPS;
			if (!right)
				step = -step;

			pos.x += step;

			if ((right && pos.x >= originX + maxOffset) || (!right && pos.x <= originX)) {
				right = !right;
				
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