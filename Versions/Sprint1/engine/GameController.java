package engine;

import java.awt.event.KeyListener;

public interface GameController extends KeyListener{
	public Command getCommand();
}
