package model;

import engine.Command;

public class Hero extends Character {
	
	private boolean victory;
	
	public Hero(int x, int y) {
		super("hero", x, y, 10, 1, 1, 2);
		victory = false;
	}
	
	public void setVictory(boolean victory) {
		this.victory = victory;
	}
	
	public boolean getVictory() {
		return victory;
	}

	public void pickGroundItem(Room activeRoom) {
		if(activeRoom.checkGroundItem(x, y)) {
			GroundItem groundItem = activeRoom.getGroundItem(x, y);
			groundItem.applyEffects(this);
			activeRoom.removeGroundItems(x, y);			
		}
	}

	public Door getDoor(Command cmd, Room activeRoom) {
		if(activeRoom.checkDoor(x, y)) {
			Door door = activeRoom.getDoor(x, y);
			if(cmd == door.getCmd1() && door.getRoom1().getId() == activeRoom.getId() 
				|| cmd == door.getCmd2() && door.getRoom2().getId() == activeRoom.getId() ) {
				return door;
			}
		}

		return null;
	}

	@Override
	public void attack(Command cmd, Room activeRoom, Hero hero) {
		if(!cmd.equals(Command.SPACE)) {
			return;
		}
		else if(cooldownAttack > 0) {
			cooldownAttack--;
			return;
		}
		
		cooldownAttack = cooldownAttackInit;

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

		if(activeRoom.checkMonster(targetX, targetY)) {
			Monster monster = activeRoom.getMonster(targetX, targetY);
			monster.addHealthPoints(-attackPoints);
			if(monster.getHealthPoints() <= 0) {
				activeRoom.removeMonster(targetX, targetY);
			}
		}
	}
}
