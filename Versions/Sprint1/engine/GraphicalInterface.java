package engine;

import javax.swing.JFrame;

public class GraphicalInterface {
	private DrawingPanel panel;
	private JFrame frame;
	public GraphicalInterface(GamePainter gamePainter, GameController gameController) {	
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

		this.panel=new DrawingPanel(gamePainter);
		frame.setContentPane(this.panel);
		
		this.panel.addKeyListener(gameController);	
		
		frame.pack();
		frame.setVisible(true);
		frame.getContentPane().setFocusable(true);
		frame.getContentPane().requestFocus();
	}
	
	public void paint()	{
		this.panel.drawGame();	
	}
}
