package com.critters.snake.entities.ui;

import com.critters.snake.entities.Entity;
import com.critters.snake.graphics.Render;
import com.critters.snake.level.Level;

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

	public abstract void renderIfVisible(Render render);

	@Override
	public void render(Render render) {
		if (visible)
			renderIfVisible(render);
	}

}
