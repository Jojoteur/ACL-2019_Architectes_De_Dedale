package tools;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import engine.Command;

public class Utility {
    public static Command determineDirection(int xStart, int yStart, int xEnd, int yEnd) {
        int dX = xEnd - xStart;
        int dY = yEnd - yStart;

        // S'il faut bouger sur l'axe horizontal.
        if (Math.abs(dX) >= Math.abs(dY)) {
            return (dX > 0) ? Command.RIGHT : Command.LEFT;
        }
        // S'il faut bouger sur l'axe vertical
        else {
            return (dY > 0) ? Command.DOWN : Command.UP;
        }
    }

    public static Image resizeImage(String filename, int newWidth, int newHeight) throws IOException {
        InputStream resource = Utility.class.getResourceAsStream("/textures/" + filename);

        if (resource == null) {
            return null;
        }

        Image img = ImageIO.read(resource).getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        return img;
    }

    public static void saveJSON(JSONObject json, String path) {
        File file = new File(path);
        FileWriter fw = null;
        String data = json.toJSONString();
        try {
            fw = new FileWriter(file);

            if(!file.exists()) {
                file.createNewFile();
                fw.write("{}");    
            }
            fw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject openJSON(String path) {
        JSONParser jsonParser = new JSONParser();
        File file = new File(path);
        try {
            if(!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write("{}"); 
                fw.close();   
            }

            //Read JSON file
            FileReader reader = new FileReader(file);
            Object obj = jsonParser.parse(reader);
            
            JSONObject json = (JSONObject) obj;
            reader.close();
            return json;
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
        	e.printStackTrace();
        }
        return new JSONObject();
    }
}
