package engine;

import java.awt.image.BufferedImage;

public interface GamePainter {

	public abstract void draw(BufferedImage image);
	public abstract int getWidth();
	public abstract int getHeight();
}
 