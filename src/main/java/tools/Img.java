package tools;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Img {
	public static Image resize(String filename, int newWidth, int newHeight) throws IOException {
		Image img = ImageIO.read(Img.class.getResourceAsStream("/"+filename)).getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
		return img;
	}
}
