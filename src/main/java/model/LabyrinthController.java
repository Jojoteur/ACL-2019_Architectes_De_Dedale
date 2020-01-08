package model;


import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import engine.Command;
import engine.GameController;

public class LabyrinthController implements GameController{
	private Command actualCommand;
	
	private List<Command> pressedKeys = new ArrayList<Command>();
	
	private boolean UP = false;
	private boolean DOWN = false;
	private boolean RIGHT = false;
	private boolean LEFT = false;
	
	public LabyrinthController() {
		this.actualCommand = Command.IDLE;
	}
	
	@Override
	public Command getCommand() {
		return this.actualCommand;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case 81: // Lettre q
		case 37: // Flèche vers la gauche
			if(!LEFT)
			{
				LEFT = true;
				pressedKeys.add(Command.LEFT);
			}
			
			this.actualCommand = Command.LEFT;
			break;
		case 90: // Lettre z
		case 38: // Flèche vers le haut
			if(!UP)
			{
				UP = true;
				pressedKeys.add(Command.UP);
			}
			
			this.actualCommand = Command.UP;
			break;
		case 83: // Lettre s
		case 40: // Flèche vers le bas
			if(!DOWN)
			{
				DOWN = true;
				pressedKeys.add(Command.DOWN);
			}
			
			this.actualCommand = Command.DOWN;
			break;
		case 68: // Lettre d
		case 39: // Flèche vers la droite
			if(!RIGHT)
			{
				RIGHT = true;
				pressedKeys.add(Command.RIGHT);
			}
			this.actualCommand = Command.RIGHT;
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case 81: // Lettre q
		case 37: // Flèche vers la gauche
			LEFT = false;
			pressedKeys.remove(Command.LEFT);
			this.actualCommand = Command.LEFT;
			break;
		case 90: // Lettre z
		case 38: // Flèche vers le haut
			UP = false;
			pressedKeys.remove(Command.UP);
			this.actualCommand = Command.UP;
			break;
		case 83: // Lettre s
		case 40: // Flèche vers le bas
			DOWN = false;
			pressedKeys.remove(Command.DOWN);
			this.actualCommand = Command.DOWN;
			break;
		case 68: // Lettre d
		case 39: // Flèche vers la droite
			RIGHT = false;
			pressedKeys.remove(Command.RIGHT);
			this.actualCommand = Command.RIGHT;
			break;
		}

		if(!UP && !DOWN && !LEFT && !RIGHT)
		{
			this.actualCommand = Command.IDLE;
		}
		else
		{
			this.actualCommand = pressedKeys.get(pressedKeys.size()-1);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public void reset() {
		actualCommand = Command.IDLE;
		pressedKeys = new ArrayList<Command>();
		UP = false;
		DOWN = false;
		RIGHT = false;
		LEFT = false;
	}
}
