package com.critters.flappingbird.entities.ui;

import com.critters.flappingbird.entities.Entity;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

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
	public final void render(Render render) {
		if (visible) {
			renderIfVisible(render);
		}
	}

}
