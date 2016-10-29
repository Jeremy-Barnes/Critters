package com.critters.spaceinvaders.entities.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.level.Level;

/**
 * Abstract class for all ui_elements. Might get more code later.
 */
public abstract class UIElement extends Entity {

	protected boolean visible = true;

	public UIElement(Level level) {
		super(level);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public abstract void renderIfVisible(SpriteBatch render);

	@Override
	public void render(SpriteBatch render) {
		if (visible)
			renderIfVisible(render);
	}

}
