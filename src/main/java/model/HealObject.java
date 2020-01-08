package model;

import java.awt.Image;

public class HealObject extends GroundItem{
	public HealObject(int x, int y, Image texture) {
		super("healObject", x, y, texture);
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public void applyEffects(Hero hero) {
		hero.addHealthPoints(1);
	}
	
	
}
