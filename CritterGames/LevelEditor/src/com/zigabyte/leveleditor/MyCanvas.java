package com.zigabyte.leveleditor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class MyCanvas extends Canvas implements MouseListener, MouseMotionListener {

	private int size_x = 0;
	private int size_y = 0;
	private int index = 0;

	public int tiles[][];

	public MyCanvas() {
		addMouseListener(this);
		addMouseMotionListener(this);
		updateSize(16, 9);
	}

	public void updateSize(int x, int y) {
		this.size_x = x;
		this.size_y = y;

		tiles = new int[x][y];
	}

	public void updateIndex(int index) {
		this.index = index;
	}

	@Override
	public void paint(Graphics g) {
		int tw = getWidth() / size_x;
		int th = getHeight() / size_y;
		Random r = new Random();

		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				Color c = Color.white;
				switch (tiles[x][y]) {
				case 0:
					c = Color.GRAY;
					break;
				case 1:
					c = Color.blue;
					break;
				case 2:
					c = Color.RED;
					break;
				case 3:
					c = Color.ORANGE;
					break;
				case 4:
					c = Color.BLACK;
					break;
				case 5:
					c = Color.GREEN;
					break;
				case 6:
					c = Color.YELLOW;
					break;
				case 7:
					c = Color.PINK;
					break;
				}
				g.setColor(c);
				g.fillRect(x * tw, y * th, tw, th);
			}
		}
	}

	private void addTile(int x, int y) {
		int tx = x / (getWidth() / size_x);
		int ty = y / (getHeight() / size_y);

		if (tx >= 0 && ty >= 0 && tx < size_x && ty < size_y) {
			if (tiles[tx][ty] != index)
				repaint();

			tiles[tx][ty] = index;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		addTile(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		addTile(e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		addTile(e.getX(), e.getY());
	}

	// Unused below
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
