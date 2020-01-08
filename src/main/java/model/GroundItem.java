package model;

import java.awt.Image;

import org.json.simple.JSONObject;

public abstract class GroundItem {
	
	private String type;
	private int x,y;
	private Image texture;
	
	public GroundItem(String type, int x, int y, Image texture) {
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Image getTexture() {
		return this.texture;
	}
	
	public abstract boolean canPassThrough();
	public abstract void applyEffects(Hero hero);
	
	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject j = new JSONObject();
		j.put("type", type);
		j.put("x", x);
		j.put("y", y);
		return j;
	}
}
