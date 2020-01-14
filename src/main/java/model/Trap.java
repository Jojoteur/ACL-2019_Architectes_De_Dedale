package model;

import java.util.Date;

public class Trap extends GroundItem {
	private int cooldownEffect;
	private long lastEffect;
	public Trap(int x, int y) {
		super("trap", x, y);
		this.cooldownEffect = 1000;
		Date date = new Date();
		this.lastEffect = date.getTime() - this.cooldownEffect;
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public void applyEffects(Hero hero) {
		Date date = new Date();
		long milliseconds = date.getTime();
		if(milliseconds >= lastEffect + cooldownEffect){
			hero.addHealthPoints(-1);
			lastEffect = milliseconds;
		}
	}

	@Override
	public boolean removeWhenPicked() {
		return false;
	}
}