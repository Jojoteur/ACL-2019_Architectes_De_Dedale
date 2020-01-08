package model;

import java.awt.Image;
import java.io.IOException;
import java.util.Hashtable;

import engine.Command;
import tools.Img;

public class Texture{
    private static Image defaultTexture;
    private static Hashtable<String,Image> textures;

    public static void initTextures(int imgSize)
    {
        textures = new Hashtable<String, Image>();

        try {
			defaultTexture = Img.resize("default.jpg", imgSize, imgSize);
		} catch (IOException e) {
            System.out.println("The default texture is missing.");
			e.printStackTrace();
        }
        
        checkAndAddTexture("wall", "wall.jpg", imgSize);
        checkAndAddTexture("treasure", "treasure.jpg", imgSize);
        checkAndAddTexture("trap", "trap.jpg", imgSize);
        checkAndAddTexture("hero", "hero.jpg", imgSize);
        checkAndAddTexture("healObject", "healObject.jpg", imgSize);
    }

    public static Image getDefault()
    {
        return defaultTexture;
    }

    public static Image get(String category)
    {
        return textures.get(category);
    }

    public static Image get(String category, Command cmd)
    {
        return textures.get(category + cmd.name());
    }

    private static void checkAndAddTexture(String key, String filename, int imgSize)
    {
        try {
			textures.put(key, Img.resize(filename, imgSize, imgSize));
		} catch (IOException|java.lang.NullPointerException e) {
            System.out.println("The "+key+" texture is missing. \""+filename+"\" given.");
		}
    }
}