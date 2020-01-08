package model;

import java.awt.Image;

import org.json.simple.JSONObject;

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
