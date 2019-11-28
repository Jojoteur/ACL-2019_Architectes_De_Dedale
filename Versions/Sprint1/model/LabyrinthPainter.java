package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import engine.GamePainter;

public class LabyrinthPainter implements GamePainter{

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double screenHeight = screenSize.getHeight();
	private double screenWidth = screenSize.getWidth();
	
	private double rate = 0.7;
	
	private int windowHeight = (int) (rate * screenHeight);
	private int windowWidth = (int) (rate * screenWidth);
	
	private int fieldSize = 50;
	
	private LabyrinthGame labyrinthGame; 
	
	public LabyrinthPainter(LabyrinthGame labyrinthGame) {
		this.labyrinthGame = labyrinthGame;
	}
	
	@Override
	public void draw(BufferedImage image) {
		Graphics2D crayon = (Graphics2D) image.getGraphics();
		crayon.setColor(Color.blue);
		Hero hero = labyrinthGame.getHero();
		crayon.fillRect(hero.getX()*fieldSize, hero.getY()*fieldSize, fieldSize, fieldSize);
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return windowWidth;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return windowHeight;
	}

}
