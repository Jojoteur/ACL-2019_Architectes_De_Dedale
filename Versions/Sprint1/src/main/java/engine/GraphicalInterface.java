package engine;
public class GraphicalInterface {
	private DrawingPanel panel;
	public GraphicalInterface(GamePainter gamePainter, GameController gameController) {	
		/*frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setContentPane(this.panel);
		 */
		this.panel=new DrawingPanel(gamePainter);
		this.panel.addKeyListener(gameController);	
		/*
		frame.pack();
		frame.setVisible(true);
		frame.getContentPane().setFocusable(true);
		frame.getContentPane().requestFocus();
		*/
	}
	
	public void paint()	{
		this.panel.drawGame();	
	}
	
	public DrawingPanel getPanel() {
		return panel;
	}
}
