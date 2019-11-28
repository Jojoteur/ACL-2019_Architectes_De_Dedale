package model;

import java.awt.Color;

import engine.Command;
import engine.Game;

public class LabyrinthGame implements Game{
	private int width=17, height=9;
	private Hero hero;
	private Floor floor;
	private Room room;
	public LabyrinthGame() {
		hero = new Hero(1, 1, Color.blue);
	}
	
	public Hero getHero() {
		return hero;
	}
		
	@Override
	public void update(Command cmd) {
		
		int x = hero.getX();
		int y = hero.getY();
		if(cmd == Command.DOWN) {
			y = (y < height) ? y+1 : y;
		}
		else if(cmd == Command.UP) {
			y = (y == 0) ? 0 : y-1;
		}
		else if(cmd == Command.LEFT) {
			x = (x == 0) ? 0 : x-1;
		}
		else if(cmd == Command.RIGHT) {
			x = (x < width) ? x+1 : x;
		}
		hero.setX(x);
		hero.setY(y);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
	
	

}
