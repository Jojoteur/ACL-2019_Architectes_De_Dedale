package start;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import engine.GameEngine;
import model.LabyrinthController;
import model.LabyrinthGame;
import model.LabyrinthPainter;
import model.Texture;

public class Application extends JFrame{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private PanelGame panelGame;
	private PanelMenu panelMenu;
	private PanelPause panelPause;
	private PanelEndGame panelEndGame;
	private GameEngine engine;
	private Timer timer;
	private LabyrinthGame game;
	private LabyrinthController controller;
	private int windowHeight, windowWidth;
	private boolean gameSet;

	public Application() throws InterruptedException {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		setTitle("Labyrinthe de Dédale");

		int roomWidth = 15;
		int roomHeight = 10;
		int fieldSize = 50;
		int fps = 60;
		windowWidth = (int) (fieldSize * roomWidth);
		windowHeight = (int) (fieldSize * roomHeight + 50);
		Texture.initTextures(fieldSize);

		game = new LabyrinthGame(roomWidth, roomHeight);
		LabyrinthPainter painter = new LabyrinthPainter(game, fieldSize);
		controller = new LabyrinthController();
		engine = new GameEngine(game, controller, painter);
		timer = new Timer((int) (1000/fps), null);

		timer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				engine.run();

				if (game.isFinishedVictory() || game.isFinishedDead()) {
					timer.stop();
					panelEndGame.refreshEndImage(game.isFinishedVictory());
					JPanel contentPane = (JPanel) getContentPane();
					contentPane.removeKeyListener(controller);
					contentPane.remove(panelGame);
					contentPane.add(panelEndGame);					

					resetGame();
					pack();
					revalidate();
					repaint();
				}
			}
		});

		panelMenu = new PanelMenu(this);
		panelGame = new PanelGame(this);
		panelPause = new PanelPause(this);
		panelEndGame = new PanelEndGame(this);

		contentPane.add(panelMenu);

		pack();
		setVisible(true);
		getContentPane().setFocusable(true);
		getContentPane().requestFocus();
	}

	public JFrame frame() {
		return this;
	}

	public Timer timer() {
		return timer;
	}

	public PanelMenu panelMenu() {
		return panelMenu;
	}

	public PanelGame panelGame() {
		return panelGame;
	}

	public PanelEndGame panelEndGame() {
		return panelEndGame;
	}

	public PanelPause panelPause() {
		return panelPause;
	}

	public LabyrinthGame game() {
		return game;
	}

	public LabyrinthController controller() {
		return controller;
	}

	public boolean isGameSet() {
		return gameSet;
	}

	public int windowHeight() {
		return windowHeight;
	}

	public int windowWidth() {
		return windowWidth;
	}

	public GameEngine engine() {
		return engine;
	}

	public void setGameSet(boolean gameSet) {
		this.gameSet = gameSet;
	}


	public void resetGame() {
		gameSet = false;
		controller.reset();
		engine.getGui().getPanel().reset();
	}

	public static void designMenuButton(JButton button) {
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setMinimumSize(new Dimension(300, 35));
		button.setPreferredSize(new Dimension(300, 35));
		button.setMaximumSize(new Dimension(300, 35));
	}
}