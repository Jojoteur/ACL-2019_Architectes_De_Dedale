package start;

import java.io.IOException;
import engine.GameEngine;
import model.LabyrinthController;
import model.LabyrinthGame;
import model.LabyrinthPainter;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		new Application();
		//launchGame();
	}
	
	public static void launchGame() throws IOException, InterruptedException {
		LabyrinthGame game = new LabyrinthGame(15,10);
		game.initFortTest();
		//game.loadGame("pouuuuuuu");
		//game.saveGame("pouuuuuuu");
		
		LabyrinthPainter painter = new LabyrinthPainter(game, 50);
		LabyrinthController controller = new LabyrinthController();
		
		// classe qui lance le moteur de jeu generique
		GameEngine engine = new GameEngine(game, controller, painter);
		engine.run();
	}
}