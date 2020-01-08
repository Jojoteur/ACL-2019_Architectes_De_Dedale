package model;

import java.awt.Image;

public class Hero extends Character{
	
	private boolean victory;
	
	public Hero(int x, int y, int healthPoint, Image texture) {
		super("hero", x, y, healthPoint, texture);
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


	
}
