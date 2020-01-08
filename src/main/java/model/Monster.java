package model;

import engine.Command;


public abstract class Monster extends Character{
	
	private String category;
	private Command cmd;
	
	public Monster (String category, int x, int y, int healthPoints) {
		super(category, x, y, healthPoints);		
	}
	
	public String getcategory() {
		return this.category;
	}

	@Override
	public boolean canPassThrough() {
		return false;
	}
	
	public Command determineNextDirection(Room room, Hero hero) {
		
		return cmd;
	}
	
	
	
	
	
	
}
