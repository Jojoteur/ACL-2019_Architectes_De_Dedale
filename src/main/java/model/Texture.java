package model;

import java.awt.Image;
import java.io.IOException;
import java.util.Hashtable;

import engine.Command;
import tools.Utility;

public class Texture{
    private static Image defaultTexture;
    private static Hashtable<String,Image> textures;

    public static void initTextures(int imgSize)
    {
        textures = new Hashtable<String, Image>();

        try {
			defaultTexture = Utility.resizeImage("default.jpg", imgSize, imgSize);
		} catch (IOException e) {
            System.out.println("The default texture is missing.");
			e.printStackTrace();
        }
        
        checkAndAddTexture("wall", "wall.jpg", imgSize);
        checkAndAddTexture("treasure", "treasure.jpg", imgSize);
        checkAndAddTexture("trap", "trap.jpg", imgSize);
        checkAndAddTexture("hero_LEFT", "hero_LEFT.jpg", imgSize);
        checkAndAddTexture("hero_RIGHT", "hero_RIGHT.jpg", imgSize);
        checkAndAddTexture("hero_UP", "hero_UP.jpg", imgSize);
        checkAndAddTexture("hero_DOWN", "hero_DOWN.jpg", imgSize);
        checkAndAddTexture("goblin_LEFT", "goblin_LEFT.jpg", imgSize);
        checkAndAddTexture("goblin_RIGHT", "goblin_RIGHT.jpg", imgSize);
        checkAndAddTexture("goblin_UP", "goblin_UP.jpg", imgSize);
        checkAndAddTexture("goblin_DOWN", "goblin_DOWN.jpg", imgSize);
        checkAndAddTexture("skeleton_LEFT", "squeleton_LEFT.jpg", imgSize);
        checkAndAddTexture("skeleton_RIGHT", "squeleton_RIGHT.jpg", imgSize);
        checkAndAddTexture("skeleton_UP", "squeleton_UP.jpg", imgSize);
        checkAndAddTexture("skeleton_DOWN", "squeleton_DOWN.jpg", imgSize);
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
        return textures.get(category +"_"+ cmd.name());
    }

    private static void checkAndAddTexture(String key, String filename, int imgSize)
    {
        try {
			textures.put(key, Utility.resizeImage(filename, imgSize, imgSize));
		} catch (IOException|java.lang.NullPointerException e) {
            System.out.println("The "+key+" texture is missing. \""+filename+"\" given.");
		}
    }
}
