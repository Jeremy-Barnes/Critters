package com.critters.breakout.entities.blocks;

import static com.critters.breakout.graphics.Render.sr;
import static com.critters.breakout.level.Level.random;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.entities.Collidable;
import com.critters.breakout.entities.powerup.Powerup;
import com.critters.breakout.entities.powerup.PowerupBigPaddle;
import com.critters.breakout.entities.powerup.PowerupFireBall;
import com.critters.breakout.entities.powerup.PowerupSlowBall;
import com.critters.breakout.math.Rectangle;
import com.critters.breakout.math.Vector2f;

public abstract class Block extends Collidable {

	protected ArrayList<Powerup> powerups = new ArrayList<Powerup>();

	protected Color color;
	protected boolean destructible = true;

	public Block(Vector2f pos, Vector2f size) {
		super(pos, size);
		this.size = size;

		rectangle = new Rectangle(pos, pos.add(size));

		color = new Color(1, 0, 0, 1);

		if (random.nextInt(6) == 0)
			addRandomPowerup();
	}

	private void addRandomPowerup() {
		int type = random.nextInt(3);
		switch (type) {
		case 0:
			powerups.add(new PowerupBigPaddle(rectangle.getCenter()));
			break;
		case 1:
			powerups.add(new PowerupFireBall(rectangle.getCenter()));
			break;
		case 2:
			powerups.add(new PowerupSlowBall(rectangle.getCenter()));
			break;
		default:
			break;
		}
	}

	@Override
	public abstract void update();

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(color);
		/*
		 * Debug code for ball proximity if (checked) { sr.setColor(0, 0, 1, 1); checked = false; }
		 */
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

	@Override
	public abstract void hit();

	@Override
	public abstract void destroy();

	public Class<? extends Block> getType() {
		if (this.getClass() == BlockMulti.class)
			return BlockMulti.class;
		if (this.getClass() == BlockVoid.class)
			return BlockVoid.class;
		if (this.getClass() == BlockIndestructible.class)
			return BlockIndestructible.class;
		return null;
	}
}
