package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.GamePainter;
import tools.Img;

public class LabyrinthPainter implements GamePainter{

	private Image defaultTexture;
	private int windowHeight, windowWidth, fieldSize;
	
	private LabyrinthGame labyrinthGame; 
	
	public LabyrinthPainter(LabyrinthGame labyrinthGame, int fieldSize) throws IOException {
		this.labyrinthGame = labyrinthGame;
		this.fieldSize = fieldSize;
		
		windowHeight = labyrinthGame.getRoomHeight() * fieldSize;
		windowWidth = labyrinthGame.getRoomWidth() * fieldSize;
		
		defaultTexture = Img.resize("default.jpg", fieldSize, fieldSize);
	}
	
	@Override
	public void draw(BufferedImage image) {
		Graphics2D crayon = (Graphics2D) image.getGraphics();
		Hero hero = labyrinthGame.getHero();
		Room room = labyrinthGame.getActiveRoom();

		for(int x=0; x<labyrinthGame.getRoomWidth(); x++) {
			for(int y=0; y<labyrinthGame.getRoomHeight(); y++) {
				if(room.checkGroundItem(x, y)) {
					crayon.drawImage(room.getGroundItem(x, y).getTexture(), x*fieldSize, y*fieldSize, null);
				}
				else {
					crayon.drawImage(defaultTexture, x*fieldSize, y*fieldSize, null);
				}
			}
		}
		crayon.drawImage(hero.getTexture(), hero.getX()*fieldSize, hero.getY()*fieldSize, null);
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
