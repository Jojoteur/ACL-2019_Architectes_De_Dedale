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
        checkAndAddTexture("hero_LEFT", "hero_left.jpg", imgSize);
        checkAndAddTexture("hero_RIGHT", "hero_right.jpg", imgSize);
        checkAndAddTexture("hero_UP", "hero_up.jpg", imgSize);
        checkAndAddTexture("hero_DOWN", "hero_down.jpg", imgSize);
        checkAndAddTexture("goblin_LEFT", "goblin_left.jpg", imgSize);
        checkAndAddTexture("goblin_RIGHT", "goblin_right.jpg", imgSize);
        checkAndAddTexture("goblin_UP", "goblin_up.jpg", imgSize);
        checkAndAddTexture("goblin_DOWN", "goblin_down.jpg", imgSize);
        checkAndAddTexture("skeleton_LEFT", "skeleton_left.jpg", imgSize);
        checkAndAddTexture("skeleton_RIGHT", "skeleton_right.jpg", imgSize);
        checkAndAddTexture("skeleton_UP", "skeleton_up.jpg", imgSize);
        checkAndAddTexture("skeleton_DOWN", "skeleton_down.jpg", imgSize);
        checkAndAddTexture("healObject", "heal_object.jpg", imgSize);
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