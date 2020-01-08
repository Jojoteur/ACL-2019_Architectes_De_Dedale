package model;

import java.awt.Image;

public class Wall extends GroundItem{
	public Wall(int x, int y, Image texture) {
		super("wall", x, y, texture);
	}
	
	@Override
	public boolean canPassThrough() {
		return false;
	}
	
	@Override
	public void applyEffects(Hero hero) {
		return;
	}
}
