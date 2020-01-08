package start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import engine.GameEngine;
import model.LabyrinthController;
import model.LabyrinthGame;
import model.LabyrinthPainter;

public class Application {
	private JFrame frame;
	private JPanel contentPane;
	private JPanel panelMenu, panelGame;
	private GameEngine engine;
	private Timer timer;
	private LabyrinthGame game;
	private LabyrinthController controller;	
	private JLabel labelVictory, labelHealthPoints;
	private int windowHeight, windowWidth;
	public Application() throws IOException, InterruptedException {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		contentPane = new JPanel();
		frame.setContentPane(contentPane);
		
		int roomWidth = 15;
		int roomHeight = 10;
		int fieldSize = 50;
		windowWidth = (int) (fieldSize * roomWidth);
		windowHeight = (int) (fieldSize * roomHeight + 50);
		
		
		game = new LabyrinthGame(roomWidth, roomHeight);
		game.initFortTest();
		//game.loadGame("pouuuuuuu");
		//game.saveGame("pouuuuuuu");		
		LabyrinthPainter painter = new LabyrinthPainter(game, fieldSize);
		
		controller = new LabyrinthController();
		engine = new GameEngine(game, controller, painter);		
		
		timer = new Timer(100, null);
		timer.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		        engine.run();
		        labelHealthPoints.setText("Health points : "+game.getHero().getHealthPoints());
		        if(game.isFinished()) {
		        	timer.stop();
			    	JPanel contentPane = (JPanel) frame.getContentPane();
			    	contentPane.remove(panelGame);
			    	contentPane.add(panelMenu);
			    	contentPane.removeKeyListener(controller);
			    	labelVictory.setText("Victory");
			        frame.pack();
			        frame.revalidate();
			        frame.repaint();
		        }
		    }
		});
		
		initPanelGame();
		initPanelMenu();
		contentPane.add(panelMenu);
		
		frame.pack();
		frame.setVisible(true);
		frame.getContentPane().setFocusable(true);
		frame.getContentPane().requestFocus();
	}
	
	private void initPanelMenu() {
		JButton start = new JButton("Launch Game");		
		panelMenu = new JPanel();
		panelMenu.setLayout(new BorderLayout(0,0));
		
		JPanel panelNorth = new JPanel();
		JPanel panelCenter = new JPanel();
		panelNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		panelMenu.setPreferredSize(new Dimension(windowWidth, windowHeight));
		
		labelVictory = new JLabel("");
		
		panelNorth.setPreferredSize(new Dimension(windowWidth, 50));
		panelNorth.add(labelVictory);
		panelCenter.add(start);
		panelMenu.add(panelNorth, BorderLayout.NORTH);
		panelMenu.add(panelCenter, BorderLayout.CENTER);
		
		start.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	JPanel contentPane = (JPanel) frame.getContentPane();
		    	contentPane.remove(panelMenu);
		    	contentPane.add(panelGame);
		    	contentPane.addKeyListener(controller);
		        frame.pack();
		        frame.revalidate();
		        frame.repaint();
		        timer.start();
		        try {
					resetGame();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    }
		});
	}
	
	
	private void initPanelGame() {	
		panelGame = new JPanel();
		panelGame.setLayout(new BorderLayout(0,0));
		
		JPanel panelNorth = new JPanel();
		JPanel panelCenter = new JPanel();

		panelGame.setPreferredSize(new Dimension(windowWidth, windowHeight));
		panelNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		labelHealthPoints = new JLabel("Health points : "+game.getHero().getHealthPoints());
		
		panelNorth.setPreferredSize(new Dimension(windowWidth, 50));
		panelNorth.add(labelHealthPoints);
		panelCenter.add(engine.getGui().getPanel());

		panelGame.add(panelNorth, BorderLayout.NORTH);
		panelGame.add(panelCenter, BorderLayout.CENTER);
	}
	
	private void resetGame() throws IOException {
		game.initFortTest();
		controller.reset();
		engine.getGui().getPanel().reset();
	}
}


/*
 1
maFenetre.setContentPane(tonJPanel);
maFenetre.repaint();
maFenetre.revalidate();
*/
