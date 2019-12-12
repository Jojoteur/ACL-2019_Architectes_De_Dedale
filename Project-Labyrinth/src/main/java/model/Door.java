package model;

import java.awt.image.BufferedImage;

import org.json.simple.JSONObject;

import engine.Command;

public class Door{
	private int id,x1,y1,x2,y2;
	private Room room1, room2;
	private Command cmd1, cmd2;
	private BufferedImage texture;
	public Door(int id, int x1, int y1, Command cmd1, Room room1, int x2, int y2, Command cmd2, Room room2) {
		this.id = id;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.room1 = room1;
		this.room2 = room2;
		this.cmd1 = cmd1;
		this.cmd2 = cmd2;
	}
	
	public int getId() {
		return id;
	}
	
	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}
	
	public Command getCmd1() {
		return cmd1;
	}
	
	public Command getCmd2() {
		return cmd2;
	}
	
	public Room getRoom1() {
		return room1;
	}

	public Room getRoom2() {
		return room2;
	}
	
	public BufferedImage getTexture() {
		return texture;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject j = new JSONObject();
		j.put("id", id);
		j.put("x1", x1);
		j.put("x2", x2);
		j.put("y1", y1);
		j.put("y2", y2);
		j.put("idRoom1", room1.getId());
		j.put("idRoom2", room2.getId());
		j.put("cmd1", cmd1.toString());
		j.put("cmd2", cmd2.toString());
		return j;
	}
}
