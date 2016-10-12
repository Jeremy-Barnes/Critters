package com.critters.snake.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.snake.math.Vector2f;

public class Render {

	// The ratio should be kept 16/9
	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;

	public ShapeRenderer shapeRenderer;
	public SpriteBatch spriteBatch;

	private OrthographicCamera camera;

	public Render() {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.translate(WIDTH / 2, HEIGHT / 2, 0);
		camera.update();
	}

	/**
	 * @param x
	 *            the x offset. X will be the center of the polygon
	 * @param y
	 *            the y offset. Y will be the center of the polygon
	 * 
	 * @param rotation
	 *            rotation of the polygon in radians
	 * 
	 */
	public void drawRect(Vector2f v0, Vector2f size, Color color) {
		drawRect(v0.x, v0.y, size.x, size.y, color);
	}

	public void drawRect(float x, float y, float w, float h, Color color) {
		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.setColor(color);
		shapeRenderer.translate(x, y, 0);
		shapeRenderer.rect(0, 0, w, h);
		shapeRenderer.translate(-x, -y, 0);

		shapeRenderer.end();

	}

	public void drawLine(float x0, float y0, float x1, float y1) {
		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.setColor(0, 0, 0, 1);
		shapeRenderer.line(x0, y0, x1, y1);

		shapeRenderer.end();
	}

	public void drawText(BitmapFont font, String text, float x, float y) {
		spriteBatch.begin();

		font.draw(spriteBatch, text, x, y);

		spriteBatch.end();
	}

	public void drawTexture(Texture texture, float x, float y, float w, float h) {
		spriteBatch.begin();

		spriteBatch.draw(SpriteLoader.getTest(), x, y, w, h);

		spriteBatch.end();
	}

	public void begin() {
		// shapeRenderer.begin(ShapeType.Filled);
		// spriteBatch.begin();

		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		// Draw the background
		float r = 220 / 255f;
		Gdx.gl.glClearColor(r, r, r, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_ARRAY_BUFFER_BINDING);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

	}

	public void end() {
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public void dispose() {
		shapeRenderer.dispose();
		spriteBatch.disableBlending();
	}

	public void flush() {
		spriteBatch.flush();
		shapeRenderer.flush();
	}

}
