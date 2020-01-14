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
        checkAndAddTexture("treasure", "treasure.png", imgSize);
        checkAndAddTexture("trap", "trap.png", imgSize);
        checkAndAddTexture("hero_LEFT", "hero_LEFT.png", imgSize);
        checkAndAddTexture("hero_RIGHT", "hero_RIGHT.png", imgSize);
        checkAndAddTexture("hero_UP", "hero_UP.png", imgSize);
        checkAndAddTexture("hero_DOWN", "hero_DOWN.png", imgSize);
        checkAndAddTexture("goblin_LEFT", "goblin_LEFT.png", imgSize);
        checkAndAddTexture("goblin_RIGHT", "goblin_RIGHT.png", imgSize);
        checkAndAddTexture("goblin_UP", "goblin_UP.png", imgSize);
        checkAndAddTexture("goblin_DOWN", "goblin_DOWN.png", imgSize);
        checkAndAddTexture("skeleton_LEFT", "skeleton_LEFT.png", imgSize);
        checkAndAddTexture("skeleton_RIGHT", "skeleton_RIGHT.png", imgSize);
        checkAndAddTexture("skeleton_UP", "skeleton_UP.png", imgSize);
        checkAndAddTexture("skeleton_DOWN", "skeleton_DOWN.png", imgSize);
        checkAndAddTexture("healObject", "healObject.png", imgSize);
        checkAndAddTexture("blood", "blood.png", imgSize);
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
