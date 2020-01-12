package model;

import java.util.Date;

import engine.Command;

public abstract class Monster extends Character{
	public Monster (String category, int x, int y, int healthPoints, int attackPoints, int cooldownMove, int cooldownAttack) {
		super(category, x, y, healthPoints, attackPoints, cooldownMove, cooldownAttack);
	}

	public void move(Room activeRoom, Hero hero) {
		super.move(determineNextDirection(activeRoom, hero), activeRoom, hero);
	}

	@Override
	public void attack(Command cmd, Room activeRoom, Hero hero) {
		int targetX = x;
		int targetY = y;
		
		switch(direction) {
			case LEFT:
				targetX--;
				break;
			case RIGHT:
				targetX++;
				break;
			case UP:
				targetY--;
				break;
			case DOWN:
				targetY++;
				break;
			default:
				break;
		}

		Date date = new Date();
		long milliseconds = date.getTime();

		// Si le goblin est à côté du joueur.
		if(hero.getX() == targetX && hero.getY() == targetY) {

			// S'il est à côté depuis assez de coups de clock pour taper.
			if(milliseconds < lastAttack + cooldownAttack) {
				return;
			}

			hero.addHealthPoints(-attackPoints);			
		}

		lastAttack = milliseconds;

	}
	
	abstract protected Command determineNextDirection(Room room, Hero hero);
}
