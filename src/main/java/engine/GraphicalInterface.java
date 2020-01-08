package engine;
public class GraphicalInterface {
	private DrawingPanel panel;
	
	public GraphicalInterface(GamePainter gamePainter, GameController gameController) {	
		this.panel=new DrawingPanel(gamePainter);
		this.panel.addKeyListener(gameController);	
	}
	
	public void paint()	{
		this.panel.drawGame();	
	}
	
	public DrawingPanel getPanel() {
		return panel;
	}
}
