package model;

public class Trap extends GroundItem{

	public Trap(int x, int y) {
		super("trap", x, y);
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
