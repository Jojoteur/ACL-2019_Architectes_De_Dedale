package model;

import java.awt.Image;

import org.json.simple.JSONObject;

import engine.Command;

public abstract class Character {
	
	protected int x, y, healthPoints;
	protected String category;
	
	public Character(String category, int x, int y, int healthPoints) {
		this.x = x;
		this.y = y;
		this.healthPoints = healthPoints;
		this.category = category;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void addHealthPoints(int value) {
		healthPoints += value;
	}
	
	public int getHealthPoints() {
		return healthPoints;
	}

	public Image getTexture() {
		return Texture.get(category);
	}

	public String getCategory() {
		return category;
	}

	public void move(Command cmd, Room activeRoom)
	{
		int newX = this.x, newY = this.y;

		if(cmd == Command.DOWN) {
			newY += (this.y < activeRoom.getHeight() - 1) ? 1 : 0;
		}
		else if(cmd == Command.UP) {
			newY += (this.y == 0) ? 0 : -1;
		}
		else if(cmd == Command.LEFT) {
			newX += (this.x == 0) ? 0 : -1;
		}
		else if(cmd == Command.RIGHT) {
			newX += (this.x < activeRoom.getWidth() - 1) ? 1 : 0;
		}
		
		if(!activeRoom.checkGroundItem(newX, newY)
		|| activeRoom.getGroundItem(newX, newY).canPassThrough()) {
			this.x = newX;
			this.y = newY;
		}
	}
	
	public abstract boolean canPassThrough();
	
	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject j = new JSONObject();
		j.put("x", x);
		j.put("y", y);
		j.put("healthPoints", healthPoints);
		
		return j;
	}
}
