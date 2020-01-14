package model;

import java.awt.Image;

import org.json.simple.JSONObject;

public abstract class GroundItem {
	
	private String category;
	private int x,y;
	
	public GroundItem(String category, int x, int y) {
		this.x = x;
		this.y = y;
		this.category = category;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Image getTexture() {
		return Texture.get(category);
	}
	
	public abstract boolean canPassThrough();
	public abstract void applyEffects(Hero hero);
	public abstract boolean removeWhenPicked();
	
	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject j = new JSONObject();
		j.put("category", category);
		j.put("x", x);
		j.put("y", y);
		return j;
	}
}
