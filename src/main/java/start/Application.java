package start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.simple.JSONObject;

import engine.GameEngine;
import model.LabyrinthController;
import model.LabyrinthGame;
import model.LabyrinthPainter;
import model.Texture;
import tools.Utility;

public class Application {
	private JFrame frame;
	private JPanel contentPane, panelMenu, panelGame, panelPause;
	private GameEngine engine;
	private Timer timer;
	private LabyrinthGame game;
	private LabyrinthController controller;
	private JLabel endText, healthLabel;
	private int roomWidth, roomHeight, fieldSize, windowHeight, windowWidth;
	private boolean gameSet;

	public Application() throws InterruptedException {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		frame.setContentPane(contentPane);

		roomWidth = 15;
		roomHeight = 10;
		fieldSize = 50;
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
				healthLabel.setText("Health points : " + game.getHero().getHealthPoints());
				if (game.isFinishedVictory() || game.isFinishedDead()) {
					timer.stop();
					JPanel contentPane = (JPanel) frame.getContentPane();
					contentPane.remove(panelGame);
					contentPane.add(panelMenu);
					contentPane.removeKeyListener(controller);

					if (game.isFinishedVictory()) {
						endText.setText("VICTORY");
						frame.pack();
						frame.revalidate();
						frame.repaint();
					} else {
						endText.setText("GAME OVER");
						frame.pack();
						frame.revalidate();
						frame.repaint();
					}

					resetGame();
				}
			}
		});

		initPanelGame();
		initPanelMenu();
		initPauseMenu();
		contentPane.add(panelMenu);

		frame.pack();
		frame.setVisible(true);
		frame.getContentPane().setFocusable(true);
		frame.getContentPane().requestFocus();
	}

	private void initPanelMenu() {
		JButton startButton = new JButton("Lancer une partie");
		JButton exitButton = new JButton("Quitter le jeu");
		JButton loadButton = new JButton("Charger une partie");
		designMenuButton(loadButton);
		designMenuButton(startButton);
		designMenuButton(exitButton);

		panelMenu = new JPanel();
		panelMenu.setLayout(new BorderLayout(0, 0));

		JPanel panelNorth = new JPanel();
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.PAGE_AXIS));

		panelNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		panelMenu.setPreferredSize(new Dimension(windowWidth, windowHeight));

		endText = new JLabel("");

		panelNorth.setPreferredSize(new Dimension(windowWidth, 50));
		panelNorth.add(endText);

		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(startButton);
		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(loadButton);
		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(exitButton);
		panelMenu.add(panelNorth, BorderLayout.NORTH);
		panelMenu.add(panelCenter, BorderLayout.CENTER);

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel contentPane = (JPanel) frame.getContentPane();
				contentPane.remove(panelMenu);
				contentPane.add(panelGame);
				contentPane.addKeyListener(controller);
				frame.pack();
				frame.revalidate();
				frame.repaint();

				if (!gameSet) {
					game.initMap();
				}

				timer.start();
			}
		});

		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JSONObject saves = Utility.openJSON("./saves.json");
				ArrayList<String> choices = new ArrayList<String>();

				saves.forEach((k,v) -> {
					choices.add(k.toString());
				});

				if(choices.size() == 0) {
					choices.add("Aucune sauvegarde");
				}
				
				String input = (String) JOptionPane.showInputDialog(null, "Choose now...",
				"The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null, // Use
																				// default
																				// icon
				choices.toArray(), // Array of choices
				choices.get(0)); // Initial choice

				if(input == null || input == "Aucune sauvegarde"){
					return;
				}

				game.loadJSON((JSONObject) saves.get(input));
				gameSet = true;

				JPanel contentPane = (JPanel) frame.getContentPane();
				contentPane.remove(panelMenu);
				contentPane.add(panelGame);
				contentPane.addKeyListener(controller);
				frame.pack();
				frame.revalidate();
				frame.repaint();

				timer.start();
			}
		});

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}

	private void initPanelGame() {
		panelGame = new JPanel();
		panelGame.setLayout(new BorderLayout(0, 0));
		panelGame.setPreferredSize(new Dimension(windowWidth, windowHeight));

		JPanel panelCenter = new JPanel();
		panelCenter.add(engine.getGui().getPanel());

		JPanel panelNorth = new JPanel();
		panelNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		healthLabel = new JLabel("Health points : " + game.getHero().getHealthPoints());
		panelNorth.setPreferredSize(new Dimension(windowWidth, 50));
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.LINE_AXIS));

		JButton pauseButton = new JButton("Pause");

		panelNorth.add(healthLabel);
		panelNorth.add(Box.createHorizontalGlue());
		panelNorth.add(pauseButton);

		panelGame.add(panelNorth, BorderLayout.NORTH);
		panelGame.add(panelCenter, BorderLayout.CENTER);

		pauseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				JPanel contentPane = (JPanel) frame.getContentPane();
				contentPane.remove(panelGame);
				contentPane.add(panelPause);
				contentPane.removeKeyListener(controller);
				controller.reset();
				frame.pack();
				frame.revalidate();
				frame.repaint();
			}
		});
	}

	private void initPauseMenu() {
		JButton resumeButton = new JButton("Continuer la partie");
		JButton restartButton = new JButton("Relancer une nouvelle partie");		
		JButton saveButton = new JButton("Sauvegarder la partie");
		JButton exitButton = new JButton("Quitter la partie");
		designMenuButton(saveButton);
		designMenuButton(resumeButton);
		designMenuButton(restartButton);
		designMenuButton(exitButton);

		panelPause = new JPanel();
		panelPause.setLayout(new BorderLayout(0, 0));

		JPanel panelNorth = new JPanel();
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.PAGE_AXIS));

		panelNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		panelPause.setPreferredSize(new Dimension(windowWidth, windowHeight));

		panelNorth.setPreferredSize(new Dimension(windowWidth, 50));
		panelNorth.add(new JLabel("La partie est en pause !"));

		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(resumeButton);
		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(restartButton);
		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(saveButton);
		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(exitButton);
		panelPause.add(panelNorth, BorderLayout.NORTH);
		panelPause.add(panelCenter, BorderLayout.CENTER);

		resumeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel contentPane = (JPanel) frame.getContentPane();
				contentPane.remove(panelPause);
				contentPane.add(panelGame);
				contentPane.addKeyListener(controller);
				frame.pack();
				frame.revalidate();
				frame.repaint();
				timer.start();
			}
		});

		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel contentPane = (JPanel) frame.getContentPane();
				contentPane.remove(panelPause);
				contentPane.add(panelGame);
				contentPane.addKeyListener(controller);
				frame.pack();
				frame.revalidate();
				frame.repaint();
				game.initMap();
				timer.start();
			}
		});

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(null, "Sauvegarde", "Nom de la sauvegarde :", JOptionPane.OK_CANCEL_OPTION);
				JSONObject save = game.getJSON(name);
				if (name == null || name == "") {
					return;
				}

				JSONObject saves = Utility.openJSON("./saves.json");
				saves.put(name, save);				
				Utility.saveJSON(saves, "./saves.json");
		    }
		});

		exitButton.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
				timer.stop();
				JPanel contentPane = (JPanel) frame.getContentPane();
				contentPane.remove(panelPause);
				contentPane.add(panelMenu);
				contentPane.removeKeyListener(controller);
				resetGame();
				frame.pack();
		        frame.revalidate();
		        frame.repaint();
		    }
		});
	}

	private void resetGame() {
		gameSet = false;
		controller.reset();
		engine.getGui().getPanel().reset();
	}

	private void designMenuButton(JButton button) {
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setMinimumSize(new Dimension(300, 35));
		button.setPreferredSize(new Dimension(300, 35));
		button.setMaximumSize(new Dimension(300, 35));
	}
}