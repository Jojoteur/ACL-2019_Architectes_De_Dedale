package engine;

public interface Game {
	
	public void update(Command cmd);
	public boolean isFinishedVictory();
	public boolean isFinishedDead();
}
