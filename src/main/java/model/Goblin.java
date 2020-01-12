package model;

import engine.Command;
import tools.Utility;

public class Goblin extends Monster {	
	public Goblin(int x, int y) {
		super("goblin", x, y, 2, 1, 400, 800);
	}

	@Override
	protected Command determineNextDirection(Room room, Hero hero) {
		int dX = hero.getX() - x;
		int dY = hero.getY() - y;

		// Si héros à côté, le gobelin ne bouge pas.
		if(Math.abs(dX) + Math.abs(dY) <= 1) {
			direction = Utility.determineDirection(x, y, hero.getX(), hero.getY());
			return Command.IDLE;
		}
		else {
			// Mouvement sur l'axe horizontal
			if(Math.random() > 0.5) {
				return (Math.random() > 0.5) ? Command.LEFT : Command.RIGHT;
			}
			// Mouvement sur l'axe vertical
			else {
				return (Math.random() > 0.5) ? Command.UP : Command.DOWN;
			}
		}
	}
}
