package model;

public class Hero extends Character{
	
	private boolean victory;
	
	public Hero(int x, int y, int healthPoint) {
		super("hero", x, y, healthPoint);
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
