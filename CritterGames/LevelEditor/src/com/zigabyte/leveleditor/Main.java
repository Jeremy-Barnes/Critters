package com.zigabyte.leveleditor;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Main {

	private JFrame frame;
	private MyCanvas canvas;

	private JPanel bottom_pannel;
	private JLabel textX;
	private JLabel textY;
	private JTextField inputX;
	private JTextField inputY;
	private Button confirm;
	private Button generate;
	private JList<String> list;

	public Main() {
		frame = new JFrame("Critters Level Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		{
			// Crate and add canvas
			canvas = new MyCanvas();
			canvas.setSize(800, 450);
			canvas.setBackground(Color.red);
		}
		{ // Create all other stuff
			int bW = 500, bH = 50;
			inputX = new JTextField(10);
			inputX.setMaximumSize(new Dimension(bW, bH));
			inputX.setText("16");
			inputY = new JTextField(10);
			inputY.setMaximumSize(new Dimension(bW, bH));
			inputY.setText("9");

			textX = new JLabel("Size X: ");
			textX.setMaximumSize(new Dimension(bW, bH));
			textY = new JLabel("Size Y: ");
			textY.setMaximumSize(new Dimension(bW, bH));

			confirm = new Button("Confirm");
			confirm.setMaximumSize(new Dimension(bW, bH));

			confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					canvas.updateSize(Integer.parseInt(inputX.getText()), Integer.parseInt(inputY.getText()));
				}
			});

			generate = new Button("Generate");
			generate.setMaximumSize(new Dimension(bW, bH));

			generate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Create a file and write to it
					PrintWriter writer;
					try {
						writer = new PrintWriter("output.txt", "UTF-8");

						int tiles[][] = canvas.tiles;
						for (int y = 0; y < tiles[0].length; y++) {
							for (int x = 0; x < tiles.length; x++) {
								writer.print("{" + tiles[x][y] + "}");
							}
							writer.println();
						}
						writer.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});

			DefaultListModel<String> listModel = new DefaultListModel<String>();
			listModel.addElement("0 - Empty Tile");
			listModel.addElement("1 - Empty Tile");
			listModel.addElement("2 - Empty Tile");
			listModel.addElement("3 - Empty Tile");
			listModel.addElement("4 - Empty Tile");
			listModel.addElement("5 - Empty Tile");
			listModel.addElement("6 - Empty Tile");
			listModel.addElement("7 - Empty Tile");

			list = new JList<String>(listModel);
			list.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					canvas.updateIndex(list.getSelectedIndex());
				}
			});
		}

		{ // Create the pannel and add its components
			Panel sub = new Panel();
			sub.setLayout(new BoxLayout(sub, BoxLayout.Y_AXIS));
			sub.add(textX);
			sub.add(inputX);
			sub.add(textY);
			sub.add(inputY);
			sub.add(confirm);
			sub.add(list);
			sub.add(generate);

			bottom_pannel = new JPanel();
			bottom_pannel.setLayout(new BorderLayout());

			bottom_pannel.add(canvas, BorderLayout.WEST);
			bottom_pannel.add(sub, BorderLayout.EAST);
		}

		// Finish the jframe
		frame.setContentPane(bottom_pannel);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new Main();
	}

}
