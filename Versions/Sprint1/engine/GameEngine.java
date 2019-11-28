package engine;

public class GameEngine {
	private Game game;
	private GameController gameController;
	private GamePainter gamePainter;
	private GraphicalInterface gui;
	
	public GameEngine(Game game, GameController gameController, GamePainter gamePainter) {
		this.game = game;
		this.gameController = gameController;
		this.gamePainter = gamePainter;
	}
	
	public void run() throws InterruptedException {
		this.gui = new GraphicalInterface(this.gamePainter, this.gameController);
		
		while(!this.game.isFinished()) {
			Command cmd = this.gameController.getCommand();
			this.game.update(cmd);
			this.gui.paint();
			Thread.sleep(100);
		}
	}
}
