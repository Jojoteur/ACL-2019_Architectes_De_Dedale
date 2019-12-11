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
		switch (e.getKeyChar()) {
		// si on appuie sur 'q',commande joueur est gauche
		case 'q':
		case 'Q':
			if(!LEFT)
			{
				LEFT = true;
				pressedKeys.add(Command.LEFT);
			}
			
			this.actualCommand = Command.LEFT;
			break;
		case 'z':
		case 'Z':
			if(!UP)
			{
				UP = true;
				pressedKeys.add(Command.UP);
			}
			
			this.actualCommand = Command.UP;
			break;
		case 's':
		case 'S':
			if(!DOWN)
			{
				DOWN = true;
				pressedKeys.add(Command.DOWN);
			}
			
			this.actualCommand = Command.DOWN;
			break;
		case 'd':
		case 'D':
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
		switch (e.getKeyChar()) {
		// si on appuie sur 'q',commande joueur est gauche
		case 'q':
		case 'Q':
			LEFT = false;
			pressedKeys.remove(Command.LEFT);
			this.actualCommand = Command.LEFT;
			break;
		case 'z':
		case 'Z':
			UP = false;
			pressedKeys.remove(Command.UP);
			this.actualCommand = Command.UP;
			break;
		case 's':
		case 'S':
			DOWN = false;
			pressedKeys.remove(Command.DOWN);
			this.actualCommand = Command.DOWN;
			break;
		case 'd':
		case 'D':
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
}