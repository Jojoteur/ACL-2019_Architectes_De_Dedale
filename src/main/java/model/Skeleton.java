package model;

import engine.Command;

public class Skeleton extends Monster {
	public Skeleton(int x, int y) {
		super("skeleton", x, y, 5, 2, 500, 1000);
	}

	@Override
	protected Command determineNextDirection(Room room, Hero hero) {
		int dX = hero.getX() - x;
		int dY = hero.getY() - y;

		// S'il faut bouger sur l'axe horizontal.
		if(Math.abs(dX) >= Math.abs(dY)) {
			return (dX > 0) ? Command.RIGHT : Command.LEFT;
		}
		// S'il faut bouger sur l'axe vertical
		else {
			return (dY > 0) ? Command.DOWN : Command.UP;
		}
		
	}
}
