package model;

import java.awt.Image;

public class Trap extends GroundItem{

	public Trap(int x, int y, Image texture) {
		super("trap", x, y, texture);
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public void applyEffects(Hero hero) {
		hero.addHealthPoints(-1);
	}

}
