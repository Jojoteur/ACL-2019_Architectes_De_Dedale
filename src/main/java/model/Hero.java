package model;

import engine.Command;

public class Hero extends Character {
	
	private boolean victory;
	
	public Hero(int x, int y, int healthPoint) {
		super("hero", x, y, healthPoint);
		victory = false;
	}
	
	public void setVictory(boolean victory) {
		this.victory = victory;
	}
	
	public boolean getVictory() {
		return victory;
	}

	@Override
	public boolean canPassThrough() {
		return false;
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


	
}
