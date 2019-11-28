package engine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private BufferedImage currentImage, nextImage;
	private GamePainter painter;
	private int width, height;
	
	public DrawingPanel(GamePainter painter) {
		super();
		this.width = painter.getWidth();
		this.height = painter.getHeight();
		this.setPreferredSize(new Dimension(this.width, this.height));
		this.painter=painter;

		// cree l'image buffer et son graphics
		this.nextImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		this.currentImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
	}
	
	public void drawGame() {
		// generer la nouvelle image
		this.painter.draw(this.nextImage);

		// inverses les images doublebuffereing
		BufferedImage temp = this.currentImage;
		// l'image a dessiner est celle qu'on a construite
		this.currentImage = this.nextImage;
		// l'ancienne image est videe
		this.nextImage = temp;
		this.nextImage.getGraphics()
				.fillRect(0, 0, this.width, this.height);
		// met a jour l'image a afficher sur le panel
		this.repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(this.currentImage, 0, 0, getWidth(), getHeight(), 0, 0,
				getWidth(), getHeight(), null);
	}
}
