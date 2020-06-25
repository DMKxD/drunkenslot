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
		GameConfig config = new GameConfig(0, true);
		config.createPlayer(Engine.getID(), "Dominik", null);
		config.createPlayer(Engine.getID(), "Jonas", null);
		
		System.out.println(config.getPlayerList().get(0).getId());
		System.out.println(config.getPlayerList().get(1).getId());
		
		Engine engine = new Engine(config);
		engine.createGame();
		//engine.testWinLine();
		engine.gameLoop();
		//SlotImage si = engine.roll();
		//engine.printSlot(si);
		//engine.scanWinLines(si);
		//engine.testWinLine();//TODO Debug
	}
}
