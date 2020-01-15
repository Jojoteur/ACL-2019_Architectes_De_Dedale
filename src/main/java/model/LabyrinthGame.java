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
        this.hero = new Hero(roomWidth - 2, 5);
        
        int nbRooms = 10;
		Room[] rooms = new Room[nbRooms];
		
		for(int i = 0; i < nbRooms; i++) {
			rooms[i] = new Room(i, roomWidth, roomHeight);
			this.rooms.put(i, rooms[i]);
			for(int x = 0; x < roomWidth; x++) {
				for(int y = 0; y < roomHeight; y++) {
					if(x == 0 || x == roomWidth - 1 || y == 0 || y == roomHeight - 1) {
						rooms[i].addGroundItem(new Wall(x,y));
					}
				}
			}			
		}
		
		int idDoor = 0;
		// Portes sur axe X
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[8], roomWidth - 1, 8, Command.RIGHT, rooms[9]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[3], roomWidth - 1, 5, Command.RIGHT, rooms[2]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[0], roomWidth - 1, 5, Command.RIGHT, rooms[1]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[1], roomWidth - 1, 5, Command.RIGHT, rooms[5]);
		addDoor(idDoor++, 0, 5, Command.LEFT, rooms[6], roomWidth - 1, 5, Command.RIGHT, rooms[7]);

		// Portes sur axe Y
		addDoor(idDoor++, 7, 0, Command.UP, rooms[4], 7, roomHeight - 1, Command.DOWN, rooms[1]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[5], 7, roomHeight - 1, Command.DOWN, rooms[6]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[1], 7, roomHeight - 1, Command.DOWN, rooms[2]);
		addDoor(idDoor++, 7, 0, Command.UP, rooms[3], 7, roomHeight - 1, Command.DOWN, rooms[8]);

		activeRoom = rooms[0];

		int[] arrayX0 = {1, 2, 2, 2, 2, 2, 2, 3, 4, 6, 7, 8, 8, 8, 8, 8, 8, 9, 10};
		int[] arrayY0 = {6, 5, 6, 7, 4, 3, 1, 3, 3, 3, 3, 3, 2, 1, 5, 6, 7, 5, 5};
		addWalls(rooms[0], arrayX0, arrayY0);
		rooms[0].addGroundItem(new HealObject(1, 7));
		rooms[0].addGroundItem(new Trap(5, 8));
		rooms[0].addGroundItem(new Trap(10, 4));
		rooms[0].addGroundItem(new Trap(4, 2));
		rooms[0].addGroundItem(new Trap(6, 1));
		rooms[0].addMonster(new Goblin(5, 6));

		int[] arrayX1 = {2, 2, 3, 3, 4, 4,10,11,12,10,11,12, 6, 8, 6, 7, 8};
		int[] arrayY1 = {6, 7, 6, 7, 6, 7, 6, 6, 6, 7, 7, 7, 1, 1, 4, 4, 4};
		addWalls(rooms[1], arrayX1, arrayY1);
		rooms[1].addGroundItem(new Trap(3, 3));
		rooms[1].addGroundItem(new Trap(11, 3));
		rooms[1].addGroundItem(new Trap(7, 6));
		rooms[1].addGroundItem(new Trap(7, 7));
		rooms[1].addMonster(new Goblin(3, 4));
		rooms[1].addMonster(new Goblin(11, 4));

		int[] arrayX2 = {13,12,11,11,11,11,10, 9, 8, 7, 6, 5, 4, 3, 6, 6, 5};
		int[] arrayY2 = { 6, 6, 6, 5, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 6, 5, 6};
		addWalls(rooms[2], arrayX2, arrayY2);
		rooms[2].addGroundItem(new Trap(13, 4));
		rooms[2].addGroundItem(new Trap(12, 2));
		rooms[2].addGroundItem(new Trap(2, 2));
		rooms[2].addGroundItem(new Trap(4, 1));
		rooms[2].addGroundItem(new Trap(5, 1));
		rooms[2].addGroundItem(new Trap(7, 2));
		rooms[2].addGroundItem(new Trap(8, 2));
		rooms[2].addGroundItem(new Trap(10, 1));
		rooms[2].addGroundItem(new Trap(5, 7));
		rooms[2].addGroundItem(new Trap(5, 8));
		rooms[2].addGroundItem(new Trap(12, 8));
		rooms[2].addGroundItem(new Trap(11, 8));
		rooms[2].addGroundItem(new HealObject(13, 8));
		rooms[2].addMonster(new Skeleton(10, 4));
		rooms[2].addMonster(new Goblin(5, 5));

		int[] arrayX3 = {1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12, 7, 7, 7, 6, 6};
		int[] arrayY3 = {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 6, 7, 3, 2};
		addWalls(rooms[3], arrayX3, arrayY3);
		rooms[3].addGroundItem(new HealObject(5, 3));
		rooms[3].addGroundItem(new Trap(2, 2));
		rooms[3].addGroundItem(new Trap(3, 2));
		rooms[3].addGroundItem(new Trap(4, 2));
		rooms[3].addGroundItem(new Trap(5, 2));
		rooms[3].addGroundItem(new Trap(10, 1));
		rooms[3].addGroundItem(new Trap(12, 7));
		rooms[3].addGroundItem(new Trap(8, 5));
		rooms[3].addGroundItem(new Trap(4, 6));
		rooms[3].addGroundItem(new Trap(5, 6));
		rooms[3].addMonster(new Goblin(5, 7));
		rooms[3].addMonster(new Goblin(12, 6));
		rooms[3].addMonster(new Goblin(10, 2));
		int[] arrayX4 = {2, 2, 2, 2, 2, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,12,12,12,12,12};
		int[] arrayY4 = {2, 3, 4, 5, 6, 7, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 4, 5, 6, 7};
		addWalls(rooms[4], arrayX4, arrayY4);
		rooms[4].addGroundItem(new Trap(7, 4));
		rooms[4].addGroundItem(new Trap(6, 4));
		rooms[4].addGroundItem(new Trap(8, 4));
		rooms[4].addGroundItem(new HealObject(7, 3));
		rooms[4].addMonster(new Skeleton(4, 5));
		rooms[4].addMonster(new Skeleton(10, 5));

		int[] arrayX5 = {2, 2, 2, 2, 2, 2, 3, 4, 5, 6, 8, 9,10,11,12,12,12,12,12,12, 3, 4, 5, 6, 8, 9,10,11};
		int[] arrayY5 = {2, 3, 4, 5, 6, 7, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 4, 5, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6};
		addWalls(rooms[5], arrayX5, arrayY5);
		rooms[5].addGroundItem(new Trap(12, 1));
		rooms[5].addGroundItem(new Trap(13, 1));
		rooms[5].addGroundItem(new Trap(13, 2));
		rooms[5].addGroundItem(new Trap(13, 3));
		rooms[5].addGroundItem(new Trap(13, 4));
		rooms[5].addGroundItem(new Trap(7, 3));
		rooms[5].addGroundItem(new Trap(9, 8));
		rooms[5].addMonster(new Goblin(7, 4));
		rooms[5].addMonster(new Goblin(1, 5));
		int[] arrayX6 = {2, 2, 2, 2, 2, 1, 3, 4, 5, 6, 8, 9,10,11,12,12,12,12,12,12, 3, 4, 5, 6, 8, 9,10,11, 4, 6, 8, 8};
		int[] arrayY6 = {2, 3, 4, 5, 6, 6, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 4, 5, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 1, 2, 7, 8};
		addWalls(rooms[6], arrayX6, arrayY6);
		rooms[6].addGroundItem(new Trap(10, 8));
		rooms[6].addGroundItem(new Trap(6, 7));
		rooms[6].addGroundItem(new Trap(8, 2));
		rooms[6].addGroundItem(new Trap(10, 1));
		rooms[6].addGroundItem(new HealObject(9, 8));
		rooms[6].addMonster(new Goblin(3, 5));
		rooms[6].addMonster(new Goblin(11, 4));
		rooms[6].addMonster(new Goblin(1, 8));

		int[] arrayX7 = {2, 2, 2, 2, 2, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,12,12,12,12,12,12, 4, 4, 4, 4, 4, 5, 6, 7, 8, 9,10,10,10,10};
		int[] arrayY7 = {2, 3, 4, 5, 6, 7, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 4, 5, 6, 7, 8, 8, 7, 6, 5, 4, 4, 4, 4, 4, 4, 4, 5, 6, 7};
		addWalls(rooms[7], arrayX7, arrayY7);
		rooms[7].addGroundItem(new Trap(9, 7));
		rooms[7].addGroundItem(new Trap(7, 8));
		rooms[7].addGroundItem(new HealObject(9, 5));
		rooms[7].addGroundItem(new HealObject(5, 8));
		rooms[7].addMonster(new Goblin(6, 6));

		int[] arrayX8 = {6, 6, 6, 6, 8, 8, 8, 8, 5, 4, 5, 4, 3, 2, 2, 2, 1, 8, 8, 7, 6, 5, 5, 9,10,11,12};
		int[] arrayY8 = {8, 7, 6, 4, 8, 7, 6, 4, 6, 6, 4, 4, 4, 4, 5, 6, 6, 3, 2, 2, 2, 2, 3, 2, 2, 2, 2};
		addWalls(rooms[8], arrayX8, arrayY8);
		rooms[8].addGroundItem(new Trap(4, 7));
		rooms[8].addGroundItem(new Trap(2, 8));
		rooms[8].addGroundItem(new Trap(10, 7));
		rooms[8].addGroundItem(new Trap(10, 4));
		rooms[8].addGroundItem(new Trap(12, 7));
		rooms[8].addGroundItem(new Trap(12, 4));
		rooms[8].addGroundItem(new HealObject(5, 7));
		rooms[8].addGroundItem(new HealObject(1, 8));
		rooms[8].addMonster(new Goblin(2, 2));
		rooms[8].addMonster(new Skeleton(13, 2));

		int[] arrayX9 = {8, 8, 8, 8, 8, 8, 8, 9,11,13,10,12, 9,11,13, 7, 6, 5, 4, 3, 2, 2, 2, 3, 4, 5, 6};
		int[] arrayY9 = {8, 7, 6, 5, 4, 3, 2, 7, 7, 7, 5, 5, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 6, 6, 6, 6, 6};
		addWalls(rooms[9], arrayX9, arrayY9);
		rooms[9].addGroundItem(new Trap(6, 7));
		rooms[9].addGroundItem(new Trap(4, 8));
		rooms[9].addGroundItem(new Trap(2, 7));
		rooms[9].addGroundItem(new Trap(3, 2));
		rooms[9].addGroundItem(new Trap(6, 2));
		rooms[9].addGroundItem(new Trap(10, 2));
		rooms[9].addGroundItem(new Trap(12, 1));
		rooms[9].addGroundItem(new Trap(11, 6));
		rooms[9].addGroundItem(new Trap(13, 4));
		rooms[9].addGroundItem(new HealObject(9, 8));
		rooms[9].addGroundItem(new HealObject(13, 1));
		rooms[9].addGroundItem(new Treasure(3,5));
		rooms[9].addMonster(new Goblin(11, 5));
		rooms[9].addMonster(new Skeleton(3, 3));
		rooms[9].addMonster(new Skeleton(6, 3));
	}

	private void addWalls(Room room, int[] arrayX, int[] arrayY) {
		for(int i = 0; i < arrayX.length; i++) {
			room.addGroundItem(new Wall(arrayX[i],arrayY[i]));
		}
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
