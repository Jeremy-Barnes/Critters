package com.critters.flappingbird.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Render {

	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;

	public ShapeRenderer shapeRenderer;
	public SpriteBatch spriteBatch;

	public BitmapFont font;

	private OrthographicCamera camera;

	public Render() {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));

		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.translate(WIDTH / 2, HEIGHT / 2, 0);
		camera.update();
	}

	public void translate(float x, float y) {
		camera.translate(x - camera.position.x + Render.WIDTH / 2, y - camera.position.y + Render.HEIGHT / 2);
		camera.update();
	}

	/**
	 * Only works for 1 size for the moment.
	 * 
	 * TODO: Expand into multiple needed font sizes.
	 */
	public void drawText(String text, Color color, float x, float y) {
		beginRenderer(spriteBatch);

		font.setColor(color);
		font.draw(spriteBatch, text, x, y);
	}

	public void drawPolygon(Polygon p) {
		drawPolygon(p, Color.WHITE, 0, 0, 0);
	}

	public void drawPolygon(Polygon p, float x, float y) {
		drawPolygon(p, Color.WHITE, x, y, 0);
	}

	public void drawPolygon(Polygon p, Color c, float x, float y) {
		drawPolygon(p, c, x, y, 0);
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
	public void drawPolygon(Polygon p, Color color, float x, float y, float rotation) {
		beginRenderer(shapeRenderer);

		rotation = rotation * 180 / 3.14f; // Convert to degrees

		shapeRenderer.setColor(color);
		// shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.5f);

		// Draw the individual vertices triangles with vertices

		shapeRenderer.translate(x, y, 0);
		shapeRenderer.rotate(0, 0, 1, rotation);

		float[] v = p.VERTICES;
		for (int i = 0; i < p.VERTICES_COUNT - 2; i++) {
			int t = 2 * (i + 1);
			shapeRenderer.triangle(v[0], v[1], v[t], v[t + 1], v[t + 2], v[t + 3]);
		}

		shapeRenderer.rotate(0, 0, 1, -rotation);
		shapeRenderer.translate(-x, -y, 0);
	}

	public void drawRectangle(Color color, float x, float y, float w, float h) {
		beginRenderer(shapeRenderer);

		shapeRenderer.setColor(color);

		float rotation = 0;
		shapeRenderer.translate(x + w / 2, y + h / 2, 0);
		shapeRenderer.rotate(0, 0, 1, rotation);

		shapeRenderer.rect(-w / 2, -h / 2, w, h);

		shapeRenderer.rotate(0, 0, 1, -rotation);
		shapeRenderer.translate(-(x + w / 2), -(y + h / 2), 0);
	}

	public void drawCirlce(Color color, float x, float y, float radius) {
		beginRenderer(shapeRenderer);

		shapeRenderer.setColor(color);
		shapeRenderer.circle(x, y, radius);
	}

	public void drawLine(float x0, float y0, float x1, float y1) {
		beginRenderer(shapeRenderer);

		shapeRenderer.setColor(0, 0, 0, 0.3f);
		shapeRenderer.line(x0, y0, x1, y1);
	}

	public void drawTexture(Texture texture, float x, float y, float w, float h) {
		drawTexture(texture, x, y, w, h, 0);
	}

	public void drawTexture(Texture texture, float x, float y, float w, float h, float rotation) {
		beginRenderer(spriteBatch);

		// The draw method requires rotation in degrees.
		rotation = rotation * 180f / 3.14f;
		spriteBatch.draw(texture, x, y, w / 2, h / 2, w, h, 1, 1, rotation, 0, 0, texture.getWidth(),
				texture.getHeight(), false, false);
	}

	public void beginRenderer(Object renderer) {
		// Start the renderer that was passed if isn't started.
		// End others
		if (renderer instanceof SpriteBatch) {
			if (!((SpriteBatch) renderer).isDrawing()) {
				endAllRenderers();
				((SpriteBatch) renderer).begin();
			}
		}

		if (renderer instanceof ShapeRenderer) {
			if (!((ShapeRenderer) renderer).isDrawing()) {
				endAllRenderers();
				((ShapeRenderer) renderer).begin(ShapeType.Filled);
			}
		}
	}

	public void begin() {
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.identity(); // This stops the rotation problem

		// Draw the background
		float r = 220 / 255f;
		Gdx.gl.glClearColor(r, r, r, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void endAllRenderers() {
		if (shapeRenderer.isDrawing())
			shapeRenderer.end();

		if (spriteBatch.isDrawing())
			spriteBatch.end();
	}

	public void end() {
		endAllRenderers();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public void dispose() {
		shapeRenderer.dispose();
		spriteBatch.disableBlending();
	}

}