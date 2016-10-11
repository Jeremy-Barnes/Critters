package com.critters.spaceinvaders.entities.ui;

import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.level.Level;

/**
 * Abstract class for all ui_elements. Might get more code later.
 */
public abstract class UIElement extends Entity {

	public UIElement(Level level) {
		super(level);
	}

}
