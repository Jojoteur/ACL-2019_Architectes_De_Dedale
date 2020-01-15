package model;

import java.awt.Color;
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
					drawCharacter(crayon, room.getMonster(x, y));
				}
			}
		}
		drawCharacter(crayon, hero);
	}

	private void drawCharacter(Graphics2D crayon, Character character) {
		int x = character.getX();
		int y = character.getY();
		float healthPoints = character.getHealthPoints();
		float maxHealthPoints = character.getMaxHealthPoints();

		float percentLife = healthPoints/maxHealthPoints;
		int lengthRedBar = (int) (percentLife*fieldSize);
		int lengthBlackBar = (int) (fieldSize - lengthRedBar);

		crayon.setColor(Color.RED);
		crayon.fillRect(x*fieldSize, y*fieldSize-10, lengthRedBar, 5);
		crayon.setColor(Color.BLACK);
		crayon.fillRect(x*fieldSize + lengthRedBar, y*fieldSize-10, lengthBlackBar, 5);
		crayon.drawRect(x*fieldSize - 1, y*fieldSize-11, fieldSize+1, 6);
		crayon.drawImage(character.getTexture(), x*fieldSize, y*fieldSize, null);
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
