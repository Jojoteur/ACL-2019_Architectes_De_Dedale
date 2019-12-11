package model;


import java.awt.Image;
import java.io.IOException;
import java.util.Hashtable;
import org.json.simple.JSONObject;

import engine.Command;
import engine.Game;
import tools.Img;
import tools.JSON;

public class LabyrinthGame implements Game{
	private int roomWidth, roomHeight;
	private Hero hero;
	public Room activeRoom;
	public Hashtable<Integer, Room> rooms;
	public Hashtable<Integer, Door> doors;
	
	public LabyrinthGame(int roomWidth, int roomHeight) {
		this.roomWidth = roomWidth;
		this.roomHeight = roomHeight;
		rooms = new Hashtable<Integer, Room>();
		doors = new Hashtable<Integer, Door>();
	}
	
	public Hero getHero() {
		return hero;
	}
	
	public Room getActiveRoom() {
		return activeRoom;
	}
	
	public int getRoomWidth() {
		return roomWidth;
	}
	
	public int getRoomHeight() {
		return roomHeight;
	}
		
	@Override
	public void update(Command cmd) {
		int x = hero.getX();
		int y = hero.getY();
		
		if(activeRoom.checkDoor(x, y)) {
			Door door = activeRoom.getDoor(x, y);
			if(cmd == door.getCmd1() && door.getRoom1().getId() == activeRoom.getId() 
				|| cmd == door.getCmd2() && door.getRoom2().getId() == activeRoom.getId() ) {
				changeRoom(door);
				return;
			}
		}
		
		if(cmd == Command.DOWN) {
			y = (y < roomHeight - 1) ? y+1 : y;
		}
		else if(cmd == Command.UP) {
			y = (y == 0) ? 0 : y-1;
		}
		else if(cmd == Command.LEFT) {
			x = (x == 0) ? 0 : x-1;
		}
		else if(cmd == Command.RIGHT) {
			x = (x < roomWidth - 1) ? x+1 : x;
		}
		
		boolean canMove = true;
		
		if(activeRoom.checkGroundItem(x, y)) {
			GroundItem groundItem = activeRoom.getGroundItem(x, y);
			if(!groundItem.canPassThrough()) {
				canMove = false;
			}
			else {
				groundItem.applyEffects(hero);
				activeRoom.removeGroundItems(x, y);
			}
			
			System.out.println(hero.getHealthPoints());
		}


		if(canMove) {
			hero.setX(x);
			hero.setY(y);
		}
	}

	@Override
	public boolean isFinished() {
		return hero.getVictory();
	}
		
	@SuppressWarnings("unchecked")
	public void saveGame(String name) {
		JSONObject save = new JSONObject();
		
		save.put("hero", hero.save());
		JSONObject rooms = new JSONObject();
        this.rooms.forEach((k, v) -> { 
        	rooms.put(k, v.save());
        });
        save.put("rooms", rooms);
        
		JSONObject doors = new JSONObject();
        this.doors.forEach((k, v) -> { 
        	doors.put(k, v.save());
        });
        save.put("doors", doors);
        
        save.put("activeRoom", activeRoom.getId());
        save.put("roomWidth", roomWidth);
        save.put("roomHeight", roomHeight);
        JSONObject saves = JSON.openJSON("saves.json");
        saves.put(name, save);
        
		JSON.saveJSON(saves, "saves.json");
	}
	
	@SuppressWarnings("unchecked")
	public void loadGame(String name) throws IOException {
        JSONObject savesJSON = JSON.openJSON("saves.json");
        JSONObject saveJSON = (JSONObject) savesJSON.get(name);        
        JSONObject roomsJSON = (JSONObject) saveJSON.get("rooms");
        JSONObject doorsJSON = (JSONObject) saveJSON.get("doors");
        JSONObject heroJSON = (JSONObject) saveJSON.get("hero");
        
		Image heroTexture = Img.resize("hero.jpg", 50, 50);
		Image wallTexture = Img.resize("wall.jpg", 50, 50);
		Image trapTexture = Img.resize("trap.jpg", 50, 50);
		Image healObjectTexture = Img.resize("healObject.jpg", 50, 50);
		Image treasureTexture = Img.resize("treasure.jpg", 50, 50);
        hero = new Hero(
        		Math.toIntExact((Long) heroJSON.get("x")), 
        		Math.toIntExact((Long) heroJSON.get("y")), 
        		Math.toIntExact((Long) heroJSON.get("healthPoints")),
        		heroTexture
        );
        
        roomsJSON.forEach((k, v) -> { 
        	JSONObject j = (JSONObject) v;
        	int id = Math.toIntExact((Long) j.get("id"));
        	Room room = new Room(id, Math.toIntExact((Long) j.get("width")), Math.toIntExact((Long) j.get("height")));
        	rooms.put(id, room);
        	
        	JSONObject groundItems = (JSONObject) j.get("groundItems");
        	
        	groundItems.forEach((k2, v2) -> { 
            	JSONObject j2 = (JSONObject) v2;
            	String type = (String) j2.get("type");
            	int x = Math.toIntExact((Long) j2.get("x"));
            	int y = Math.toIntExact((Long) j2.get("y"));
            	
            	switch(type) {
            		case "wall":
            			room.addGroundItem(new Wall(x,y, wallTexture));
            			break;
            		case "trap":
            			room.addGroundItem(new Trap(x,y, trapTexture));
            			break;
            		case "healthObject":
            			room.addGroundItem(new HealObject(x,y, healObjectTexture));
            			break;
            		case "treasure":
            			room.addGroundItem(new Treasure(x,y, treasureTexture));
            			break;
            		default:
            			break;
            	}
            });
        });
        
        
        doorsJSON.forEach((k, v) -> { 
        	JSONObject j = (JSONObject) v;
        	int id = Math.toIntExact((Long) j.get("id"));
        	int idRoom1 = Math.toIntExact((Long) j.get("idRoom1"));
        	int idRoom2 = Math.toIntExact((Long) j.get("idRoom2"));
        	Door door = new Door(
        			id,
        			Math.toIntExact((Long) j.get("x1")),
        			Math.toIntExact((Long) j.get("y1")),
        			Command.valueOf((String) j.get("cmd1")),
        			rooms.get(idRoom1),
        			Math.toIntExact((Long) j.get("x2")),
        			Math.toIntExact((Long) j.get("y2")),
        			Command.valueOf((String) j.get("cmd2")),
        			rooms.get(idRoom2)
        	);
        	doors.put(id, door);
        	rooms.get(idRoom1).addDoor(door);
        	rooms.get(idRoom2).addDoor(door);
        });
        
        
		roomWidth = Math.toIntExact((Long) saveJSON.get("roomWidth"));
		roomHeight = Math.toIntExact((Long) saveJSON.get("roomHeight"));
		activeRoom = this.rooms.get(Math.toIntExact((Long) saveJSON.get("activeRoom")));
	}
	
	public void initFortTest() throws IOException {
        this.hero = new Hero(1, 1, 10, Img.resize("hero.jpg", 50, 50));
        Image wallTexture = Img.resize("wall.jpg", 50, 50);
        
        int nbRooms = 4;
		Room[] rooms = new Room[nbRooms];
		
		for(int i = 0; i < nbRooms; i++) {
			rooms[i] = new Room(i, roomWidth, roomHeight);
			
			for(int x = 0; x < roomWidth; x++) {
				for(int y = 0; y < roomHeight; y++) {
					if(x == 0 || x == roomWidth - 1 || y == 0 || y == roomHeight - 1) {
						rooms[i].addGroundItem(new Wall(x,y, wallTexture));
					}
				}
			}
			
			this.rooms.put(i, rooms[i]);
			
		}
		
		Door door;
		door = new Door(0, 0, 5, Command.LEFT, rooms[0], roomWidth - 1, 5, Command.RIGHT, rooms[1]);
		rooms[0].addDoor(door).removeGroundItems(door.getX1(), door.getY1());
		rooms[1].addDoor(door).removeGroundItems(door.getX2(), door.getY2());
		doors.put(0, door);
		door = new Door(1, 7, 0, Command.UP, rooms[1], 7, roomHeight - 1, Command.DOWN, rooms[2]);
		rooms[1].addDoor(door).removeGroundItems(door.getX1(), door.getY1());
		rooms[2].addDoor(door).removeGroundItems(door.getX2(), door.getY2());
		doors.put(1, door);
		door = new Door(2, roomWidth - 1, 5, Command.RIGHT, rooms[2], 0, 5, Command.LEFT, rooms[3]);
		rooms[2].addDoor(door).removeGroundItems(door.getX1(), door.getY1());
		rooms[3].addDoor(door).removeGroundItems(door.getX2(), door.getY2());
		doors.put(2, door);
		
		
		Image trapTexture = Img.resize("trap.jpg", 50, 50);
		Image healObjectTexture = Img.resize("healObject.jpg", 50, 50);
		Image treasureTexture = Img.resize("treasure.jpg", 50, 50);
		rooms[0].addGroundItem(new Trap(5,5,trapTexture));
		rooms[0].addGroundItem(new HealObject(5,6,healObjectTexture));
		
		rooms[3].addGroundItem(new Treasure(5,5,treasureTexture));
		activeRoom = rooms[0];
	}
	
	private void changeRoom(Door door) {
		if(activeRoom.getId() == door.getRoom1().getId()) {
			activeRoom = door.getRoom2();
			hero.setX(door.getX2());
			hero.setY(door.getY2());
		}
		else {
			activeRoom = door.getRoom1();
			hero.setX(door.getX1());
			hero.setY(door.getY1());
		}
	}
}