package start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import engine.GameEngine;
import model.LabyrinthController;
import model.LabyrinthGame;
import model.LabyrinthPainter;
import model.Texture;

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

	public Application() throws IOException, InterruptedException {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		contentPane = new JPanel();
		frame.setContentPane(contentPane);
		
		roomWidth = 15;
		roomHeight = 10;
		fieldSize = 50;
		windowWidth = (int) (fieldSize * roomWidth);
		windowHeight = (int) (fieldSize * roomHeight + 50);
		
		Texture.initTextures(fieldSize);
		game = new LabyrinthGame(roomWidth, roomHeight);

		//game.loadGame("pouuuuuuu");
		//game.saveGame("pouuuuuuu");		
		LabyrinthPainter painter = new LabyrinthPainter(game, fieldSize);
		
		controller = new LabyrinthController();
		engine = new GameEngine(game, controller, painter);		
		
		timer = new Timer(100, null);
		timer.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		        engine.run();
		        healthLabel.setText("Health points : "+game.getHero().getHealthPoints());
				if (game.isFinishedVictory() || game.isFinishedDead()) {
					timer.stop();
					JPanel contentPane = (JPanel) frame.getContentPane();
					contentPane.remove(panelGame);
					contentPane.add(panelMenu);
					contentPane.removeKeyListener(controller);
					
					if(game.isFinishedVictory()) {
						endText.setText("VICTORY");
						frame.pack();
						frame.revalidate();
						frame.repaint();
					}
					else {
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
		panelMenu.setLayout(new BorderLayout(0,0));
		
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
				
				if(!gameSet) {
					try {
						game.initFortTest();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

		        timer.start();
		    }
		});

		loadButton.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
				
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
		panelGame.setLayout(new BorderLayout(0,0));
		panelGame.setPreferredSize(new Dimension(windowWidth, windowHeight));

		JPanel panelCenter = new JPanel();
		panelCenter.add(engine.getGui().getPanel());
		
		JPanel panelNorth = new JPanel();
		panelNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		healthLabel = new JLabel("Health points : "+game.getHero().getHealthPoints());		
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
		JButton exitButton = new JButton("Quitter la partie");
		JButton saveButton = new JButton("Sauvegarder la partie");
		designMenuButton(saveButton);
		designMenuButton(resumeButton);
		designMenuButton(exitButton);

		panelPause = new JPanel();
		panelPause.setLayout(new BorderLayout(0,0));
		
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

		saveButton.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
				
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


/*
 1
maFenetre.setContentPane(tonJPanel);
maFenetre.repaint();
maFenetre.revalidate();
*/
