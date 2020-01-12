package model;

public class Wall extends GroundItem{
	public Wall(int x, int y) {
		super("wall", x, y);
	}
	
	@Override
	public boolean canPassThrough() {
		return false;
	}
	
	@Override
	public void applyEffects(Hero hero) {
		return;
	}

	@Override
	public boolean removeWhenPicked() {
		return false;
	}
}
