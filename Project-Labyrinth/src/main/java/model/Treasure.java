package model;

import java.awt.Image;

public class Treasure extends GroundItem{
	public Treasure(int x, int y, Image texture) {
		super("treasure", x, y, texture);
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public void applyEffects(Hero hero) {
		hero.setVictory(true);
	}
	
	
}
