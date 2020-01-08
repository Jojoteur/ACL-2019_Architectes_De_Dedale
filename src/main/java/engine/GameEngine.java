package engine;

public class GameEngine{
	private Game game;
	private GameController gameController;
	private GamePainter gamePainter;
	private GraphicalInterface gui;
	
	public GameEngine(Game game, GameController gameController, GamePainter gamePainter) {
		this.game = game;
		this.gameController = gameController;
		this.gamePainter = gamePainter;
		this.gui = new GraphicalInterface(this.gamePainter, this.gameController);
	}
	
	public void run() {	
		Command cmd = this.gameController.getCommand();
		this.game.update(cmd);
		this.gui.paint();
	}
	
	public GraphicalInterface getGui() {
		return gui;
	}
}
