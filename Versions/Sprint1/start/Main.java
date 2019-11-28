package start;

import engine.GameEngine;
import model.LabyrinthController;
import model.LabyrinthGame;
import model.LabyrinthPainter;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		LabyrinthGame game = new LabyrinthGame();
		LabyrinthPainter painter = new LabyrinthPainter(game);
		LabyrinthController controller = new LabyrinthController();

		// classe qui lance le moteur de jeu generique
		GameEngine engine = new GameEngine(game, controller, painter);
		engine.run();
	}

}