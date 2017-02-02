package com.critters.breakout.entities.ui;

import com.critters.breakout.entities.Entity;
import com.critters.breakout.graphics.Render;

/**
 * Abstract class for all ui_elements. Might get more code later.
 */
public abstract class UIElement extends Entity {

	protected boolean visible = true;

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
