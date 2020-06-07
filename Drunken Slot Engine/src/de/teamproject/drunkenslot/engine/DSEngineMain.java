package de.teamproject.drunkenslot.engine;

/**
 * Test Main Class to run the CMD Engine and demo Game.
 * @author Dominik Haacke
 *
 */
public class DSEngineMain 
{
	/**
	 * Main Method to test the engine
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Engine engine = new Engine();
		engine.createPlayer(Engine.getID(), "Dominik", null);
		engine.createPlayer(Engine.getID(), "Jonas", null);
		engine.createGame();
		engine.gameLoop();
		//SlotImage si = engine.roll();
		//engine.printSlot(si);
		//engine.scanWinLines(si);
		//engine.testWinLine();//TODO Debug
	}
}
