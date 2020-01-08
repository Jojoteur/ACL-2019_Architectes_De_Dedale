package model;

import java.awt.Image;

import org.json.simple.JSONObject;

import engine.Command;

public abstract class Character {
	protected int x, y, healthPoints, attackPoints, cooldownMoveInit, cooldownMove, cooldownAttack, cooldownAttackInit;
	protected String category;
	protected Command direction;
	
	public Character(String category, int x, int y, int healthPoints, int attackPoints, int cooldownMoveInit, int cooldownAttackInit) {
		this.x = x;
		this.y = y;
		this.healthPoints = healthPoints;
		this.attackPoints = attackPoints;
		this.category = category;
		this.direction = Command.DOWN;
		this.cooldownMoveInit = cooldownMoveInit;
		this.cooldownMove = cooldownMoveInit;
		this.cooldownAttackInit = cooldownAttackInit;
		this.cooldownAttack = cooldownAttackInit;	
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

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	public void setCooldownMove(int cooldownMove) {
		this.cooldownMove = cooldownMove;
	}

	public void setCooldownAttack(int cooldownAttack) {
		this.cooldownAttack = cooldownAttack;
	}

	public void setDirection(Command direction) {
		this.direction = direction;
	}

	public Image getTexture() {
		return Texture.get(category, direction);
	}

	public String getCategory() {
		return category;
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
		if(cooldownMove <= 0) {
			cooldownMove = cooldownMoveInit;

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
		else {
			cooldownMove--;
			return false;
		}		
	}

	abstract public void attack(Command cmd, Room activeRoom, Hero hero);	

	
	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject j = new JSONObject();
		j.put("x", x);
		j.put("y", y);
		j.put("healthPoints", healthPoints);
		j.put("attackPoints", attackPoints);
		j.put("cooldownMove", cooldownMove);
		j.put("cooldownAttack", cooldownAttack);
		j.put("category", category);
		j.put("direction", direction.toString());
		return j;
	}
}
