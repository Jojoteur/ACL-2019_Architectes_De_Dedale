package model;

import java.util.Hashtable;

import org.json.simple.JSONObject;

public class Room {
	private int height, width, id;
	private Hashtable<Integer,GroundItem> groundItems;
	private Hashtable<Integer,Door> doors;
	private Hashtable<Integer,Monster> monsters;
	
	public Room(int id, int width, int height) {
		this.height = height;
		this.width = width;
		this.id = id;
		groundItems = new Hashtable<Integer,GroundItem>();
		doors = new Hashtable<Integer,Door>();
		monsters = new Hashtable<Integer,Monster>();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public int getId() {
		return this.id;
	}
	
	private static int hashXY(int x, int y) {
		return x+y*100;
	}
	
	//Methodes pour les monstres
	public Room addMonster(Monster monster) {
		monsters.put(hashXY(monster.getX(),monster.getY()), monster);
		return this;
	}
	
	public boolean checkMonster(int x, int y) {
		return monsters.containsKey(hashXY(x,y));
	}
	
	public Monster getMonster(int x, int y) {
		return monsters.get(hashXY(x,y));
	}

	public Room removeMonster(int x, int y) {
		monsters.remove(hashXY(x,y));
		return this;
	}

	public Hashtable<Integer,Monster> getMonsters() {
		return monsters;
	}

	public void moveAndAttackMonsters(Hero hero) {
		Hashtable<Integer,Monster> newMonsters = new Hashtable<Integer,Monster>();

		this.monsters.forEach((k, monster) -> {
			monster.move(this, hero);
			newMonsters.put(hashXY(monster.getX(),monster.getY()), monster);
			monster.attack(null, this, hero);
		});
		
		this.monsters = newMonsters;
	}
	
	//Methodes pour les doors
	public Room addDoor(Door door) {
		int x,y;
		if(door.getRoom1().getId() == id) {
			x = door.getX1();
			y = door.getY1();
		}
		else {
			x = door.getX2();
			y = door.getY2();
		}
		
		doors.put(hashXY(x, y), door);
		return this;
	}
	
	public boolean checkDoor(int x, int y) {
		return doors.containsKey(hashXY(x,y));
	}
	
	public Door getDoor(int x, int y) {
		return doors.get(hashXY(x,y));
	}
	
	//Methodes pour les items
	public Room addGroundItem(GroundItem groundItem) {
		groundItems.put(hashXY(groundItem.getX(), groundItem.getY()), groundItem);
		return this;
	}
	
	public boolean checkGroundItem(int x, int y) {
		return groundItems.containsKey(hashXY(x,y));
	}
	
	public GroundItem getGroundItem(int x, int y) {
		return groundItems.get(hashXY(x,y));
	}
	
	public Room removeGroundItems(int x, int y){
		groundItems.remove(hashXY(x,y));
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject j = new JSONObject();
		j.put("id", id);
		j.put("height", height);
		j.put("width", width);
		        
		JSONObject groundItems = new JSONObject();
        this.groundItems.forEach((k, v) -> { 
        	groundItems.put(k, v.save());
        });
        
        j.put("groundItems", groundItems);
        
		return j;
	}
	
	
}
