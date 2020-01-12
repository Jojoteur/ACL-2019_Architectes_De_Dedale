package model;

import java.util.Hashtable;
import org.json.simple.JSONObject;

import engine.Command;
import engine.Game;

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
		hero = new Hero(1, 1);
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
		Door door = hero.getDoor(cmd, activeRoom);

		if(door != null)
		{
			changeRoom(door);
			return;
		}
		
		hero.move(cmd, activeRoom, hero);
		hero.pickGroundItem(activeRoom);

		activeRoom.moveAndAttackMonsters(hero);
		hero.attack(cmd, activeRoom, hero);
	}

	@Override
	public boolean isFinishedVictory() {
		return hero.getVictory();
	}
	
	@Override
	public boolean isFinishedDead() {
		return (getHero().getHealthPoints() <= 0);
	}
		
	@SuppressWarnings("unchecked")
	public JSONObject getJSON(String name) {
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

		return save;
	}
	
	@SuppressWarnings("unchecked")
	public void loadJSON(JSONObject saveJSON) {  
        JSONObject roomsJSON = (JSONObject) saveJSON.get("rooms");
        JSONObject doorsJSON = (JSONObject) saveJSON.get("doors");
        JSONObject heroJSON = (JSONObject) saveJSON.get("hero");
        
        hero = new Hero(
        		Math.toIntExact((Long) heroJSON.get("x")), 
        		Math.toIntExact((Long) heroJSON.get("y"))
		);

		hero.setHealthPoints(Math.toIntExact((Long) heroJSON.get("healthPoints")));
		hero.setLastAttack((Long) heroJSON.get("lastAttack"));
		hero.setLastMove((Long) heroJSON.get("lastMove"));
		hero.setDirection(Command.valueOf((String) heroJSON.get("direction")));

        roomsJSON.forEach((k, v) -> { 
        	JSONObject j = (JSONObject) v;
        	int id = Math.toIntExact((Long) j.get("id"));
        	Room room = new Room(id, Math.toIntExact((Long) j.get("width")), Math.toIntExact((Long) j.get("height")));
			rooms.put(id, room);
			
			JSONObject monsters = (JSONObject) j.get("monsters");
			
			monsters.forEach((k2, v2) -> { 
            	JSONObject j2 = (JSONObject) v2;
            	String category = (String) j2.get("category");
            	int x = Math.toIntExact((Long) j2.get("x"));
            	int y = Math.toIntExact((Long) j2.get("y"));
            	Monster monster = null;
            	switch(category) {
            		case "goblin":
						monster = new Goblin(x,y);
            			break;
            		case "skeleton":
            			monster = new Skeleton(x,y);
            			break;
            		default:
            			break;
				}
				
				monster.setHealthPoints(Math.toIntExact((Long) j2.get("healthPoints")));
				monster.setLastAttack((Long) j2.get("lastAttack"));
				monster.setLastMove((Long) j2.get("lastMove"));
				monster.setDirection(Command.valueOf((String) j2.get("direction")));

				room.addMonster(monster);
            });

        	JSONObject groundItems = (JSONObject) j.get("groundItems");
        	
        	groundItems.forEach((k2, v2) -> { 
            	JSONObject j2 = (JSONObject) v2;
            	String category = (String) j2.get("category");
            	int x = Math.toIntExact((Long) j2.get("x"));
            	int y = Math.toIntExact((Long) j2.get("y"));
            	
            	switch(category) {
            		case "wall":
            			room.addGroundItem(new Wall(x,y));
            			break;
            		case "trap":
            			room.addGroundItem(new Trap(x,y));
            			break;
            		case "healthObject":
            			room.addGroundItem(new HealObject(x,y));
            			break;
            		case "treasure":
            			room.addGroundItem(new Treasure(x,y));
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
	
	public void initMap() {
        this.hero = new Hero(1, 1);
        
        int nbRooms = 25;
		Room[] rooms = new Room[nbRooms];
		
		for(int i = 0; i < nbRooms; i++) {
			rooms[i] = new Room(i, roomWidth, roomHeight);
			
			for(int x = 0; x < roomWidth; x++) {
				for(int y = 0; y < roomHeight; y++) {
					if(x == 0 || x == roomWidth - 1 || y == 0 || y == roomHeight - 1) {
						rooms[i].addGroundItem(new Wall(x,y));
					}
				}
			}			
			this.rooms.put(i, rooms[i]);			
		}
		
		int idDoor = 0;
		// Portes sur axe X
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[22], roomWidth - 1, 5, Command.RIGHT, rooms[23]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[21], roomWidth - 1, 5, Command.RIGHT, rooms[22]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[3], roomWidth - 1, 5, Command.RIGHT, rooms[2]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[0], roomWidth - 1, 5, Command.RIGHT, rooms[1]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[1], roomWidth - 1, 5, Command.RIGHT, rooms[4]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[4], roomWidth - 1, 5, Command.RIGHT, rooms[15]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[14], roomWidth - 1, 5, Command.RIGHT, rooms[13]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[9], roomWidth - 1, 5, Command.RIGHT, rooms[8]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[8], roomWidth - 1, 5, Command.RIGHT, rooms[7]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[7], roomWidth - 1, 5, Command.RIGHT, rooms[10]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[10], roomWidth - 1, 5, Command.RIGHT, rooms[11]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[11], roomWidth - 1, 5, Command.RIGHT, rooms[12]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[12], roomWidth - 1, 5, Command.RIGHT, rooms[18]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[18], roomWidth - 1, 5, Command.RIGHT, rooms[19]);

		// Portes sur axe Y
		addDoor(idDoor++, 7, 0, Command.UP, rooms[12], 7, roomHeight - 1, Command.DOWN, rooms[13]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[14], 7, roomHeight - 1, Command.DOWN, rooms[15]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[17], 7, roomHeight - 1, Command.DOWN, rooms[16]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[16], 7, roomHeight - 1, Command.DOWN, rooms[10]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[4], 7, roomHeight - 1, Command.DOWN, rooms[5]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[24], 7, roomHeight - 1, Command.DOWN, rooms[23]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[7], 7, roomHeight - 1, Command.DOWN, rooms[6]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[6], 7, roomHeight - 1, Command.DOWN, rooms[1]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[1], 7, roomHeight - 1, Command.DOWN, rooms[2]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[3], 7, roomHeight - 1, Command.DOWN, rooms[20]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[20], 7, roomHeight - 1, Command.DOWN, rooms[21]);

		rooms[0].addGroundItem(new Trap(5,5));
		rooms[0].addGroundItem(new HealObject(5,6));
		
		rooms[3].addGroundItem(new Treasure(5,5));
		activeRoom = rooms[0];
		//activeRoom.addMonster(new Goblin(7,1));
		activeRoom.addMonster(new Skeleton(6,1));
		activeRoom.addMonster(new Skeleton(8,1));
		activeRoom.addMonster(new Skeleton(10,1));
		activeRoom.addMonster(new Skeleton(12,1));
	}

	private void addDoor(int idDoor, int x1, int y1, Command cmd1, Room room1, int x2, int y2, Command cmd2, Room room2) {
		Door door = new Door(idDoor, x1, y1, cmd1, room1, x2, y2, cmd2, room2);
		room1.addDoor(door).removeGroundItems(door.getX1(), door.getY1());
		room2.addDoor(door).removeGroundItems(door.getX2(), door.getY2());
		doors.put(idDoor++, door);
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
