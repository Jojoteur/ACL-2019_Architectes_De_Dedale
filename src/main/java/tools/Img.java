package tools;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Img {
	public static Image resize(String filename, int newWidth, int newHeight) throws IOException {
		InputStream resource = Img.class.getResourceAsStream("/"+filename);
		
		if(resource == null)
		{
			return null;
		}

		Image img = ImageIO.read(resource).getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
		return img;
	}
}
