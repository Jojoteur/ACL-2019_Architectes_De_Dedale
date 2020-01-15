package model;

import java.awt.Image;
import java.util.Date;
import java.util.Random;

import org.json.simple.JSONObject;

import engine.Command;

public abstract class Character {
	protected int x, y, healthPoints, attackPoints, cooldownMove, cooldownAttack, maxHealthPoints;
	protected String category;
	protected Command direction;
	protected long lastMove, lastAttack;
	
	public Character(String category, int x, int y, int healthPoints, int attackPoints, int cooldownMove, int cooldownAttack) {
		this.x = x;
		this.y = y;
		this.healthPoints = healthPoints;
		this.maxHealthPoints = healthPoints;
		this.attackPoints = attackPoints;
		this.category = category;
		this.direction = Command.DOWN;
		Date date = new Date();
		Random r = new Random();
		this.lastMove = date.getTime() - r.nextInt(cooldownMove + 1);
		this.lastAttack = date.getTime() - r.nextInt(cooldownAttack + 1);
		this.cooldownMove = cooldownMove;
		this.cooldownAttack = cooldownAttack;	
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void addHealthPoints(int value) {
		healthPoints += value;
	}
	
	public int getHealthPoints() {
		return healthPoints;
	}

	public int getMaxHealthPoints() {
		return maxHealthPoints;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	public void setLastAttack(long lastAttack) {
		this.lastAttack = lastAttack;
	}

	public void setLastMove(long lastAttack) {
		this.lastAttack = lastAttack;
	}

	public void setDirection(Command direction) {
		this.direction = direction;
	}

	public Image getTexture() {
		return Texture.get(category, direction);
	}

	public Command getDirection() {
		return direction;
	}

	public void move(Command cmd, Room activeRoom, Hero hero)
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
		
		if(canMove(cmd, newX, newY, activeRoom, hero)) {
			this.x = newX;
			this.y = newY;
		}
	}

	private boolean canMove(Command cmd, int x, int y, Room activeRoom, Hero hero) {
		Date date = new Date();
		long milliseconds = date.getTime();

		if(milliseconds < lastMove + cooldownMove){
			return false;
		}

		lastMove = milliseconds;

		if(cmd.equals(Command.DOWN) 
		|| cmd.equals(Command.UP) 
		|| cmd.equals(Command.RIGHT)
		|| cmd.equals(Command.LEFT)) {
			this.direction = cmd;				
		}

		return !activeRoom.checkMonster(x, y)
			&& (x != hero.getX() || y != hero.getY())
			&& (!activeRoom.checkGroundItem(x, y) 
				|| activeRoom.getGroundItem(x, y).canPassThrough());
	
	}

	abstract public void attack(Command cmd, Room activeRoom, Hero hero);	

	
	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject j = new JSONObject();
		j.put("x", x);
		j.put("y", y);
		j.put("healthPoints", healthPoints);
		j.put("attackPoints", attackPoints);
		j.put("lastAttack", lastAttack);
		j.put("lastMove", lastMove);
		j.put("category", category);
		j.put("direction", direction.toString());
		return j;
	}
}
