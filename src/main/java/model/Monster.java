package model;

import java.awt.Image;
import engine.Command;


public abstract class Monster extends Character{
	
	private int categorie;
	private Command cmd;
	
	public Monster (String categorie, int x, int y, int healthPoints, Image texture) {
		super(categorie, x, y, healthPoints, texture);		
	}
	
	public int getCategorie() {
		return this.categorie;
	}

	@Override
	public boolean canPassThrough() {
		return false;
	}
	
	public Command determineNextDirection(Room room, Hero hero) {
		
		return cmd;
	}
	
	
	
	
	
	
}
