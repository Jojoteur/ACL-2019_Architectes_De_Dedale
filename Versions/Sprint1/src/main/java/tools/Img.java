package tools;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Img {
	public static Image resize(String src, int newHeight, int newWidth) throws IOException {
		Image img = ImageIO.read(Img.class.getResourceAsStream("/"+src)).getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
		return img;
	}
}
