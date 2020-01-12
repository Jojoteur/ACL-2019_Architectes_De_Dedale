package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import engine.GamePainter;

public class LabyrinthPainter implements GamePainter{

	private Image defaultTexture;
	private int windowHeight, windowWidth, roomHeight, roomWidth, fieldSize;
	
	private LabyrinthGame labyrinthGame; 
	
	public LabyrinthPainter(LabyrinthGame labyrinthGame, int fieldSize) {
		this.labyrinthGame = labyrinthGame;
		roomHeight = labyrinthGame.getRoomHeight();
		roomWidth = labyrinthGame.getRoomWidth();
		this.fieldSize = fieldSize;
		
		windowHeight = roomHeight * fieldSize;
		windowWidth = roomWidth * fieldSize;
		
		defaultTexture = Texture.getDefault();
	}
	
	@Override
	public void draw(BufferedImage image) {
		Graphics2D crayon = (Graphics2D) image.getGraphics();
		Hero hero = labyrinthGame.getHero();
		Room room = labyrinthGame.getActiveRoom();
		
		for(int x=0; x<roomWidth; x++) {
			for(int y=0; y<roomHeight; y++) {
				crayon.drawImage(defaultTexture, x*fieldSize, y*fieldSize, null);

				if(room.checkTexture(x, y)) {
					crayon.drawImage(room.getTexture(x, y), x*fieldSize, y*fieldSize, null);
				}
				// Pas de else if
				if(room.checkGroundItem(x, y)) {
					crayon.drawImage(room.getGroundItem(x, y).getTexture(), x*fieldSize, y*fieldSize, null);
				}
				// Pas de else if
				if(room.checkMonster(x, y)){
					crayon.drawImage(room.getMonster(x, y).getTexture(), x*fieldSize, y*fieldSize, null);
				}
			}
		}

		crayon.drawImage(hero.getTexture(), hero.getX()*fieldSize, hero.getY()*fieldSize, null);
	}

	@Override
	public int getWidth() {
		return windowWidth;
	}

	@Override
	public int getHeight() {
		return windowHeight;
	}

}
