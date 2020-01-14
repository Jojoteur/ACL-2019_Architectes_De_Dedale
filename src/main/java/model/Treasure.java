package model;

public class Treasure extends GroundItem{
	public Treasure(int x, int y) {
		super("treasure", x, y);
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public void applyEffects(Hero hero) {
		hero.setVictory(true);
	}
	
	@Override
	public boolean removeWhenPicked() {
		return true;
	}
}
