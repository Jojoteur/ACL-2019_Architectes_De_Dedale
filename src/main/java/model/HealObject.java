package model;

public class HealObject extends GroundItem{
	public HealObject(int x, int y) {
		super("healObject", x, y);
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public void applyEffects(Hero hero) {
		hero.addHealthPoints(1);
	}

	@Override
	public boolean removeWhenPicked() {
		return true;
	}	
}
