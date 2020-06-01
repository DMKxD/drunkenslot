package de.teamproject.drunkenslot.engine;

public class DSEngineMain 
{
	public static void main(String[] args) 
	{
		Engine engine = new Engine();
		engine.createPlayer(engine.getID(), "Dominik", null);
		engine.createPlayer(engine.getID(), "Jonas", null);
		engine.createGame();
		engine.gameLoop();
		//SlotImage si = engine.roll();
		//engine.printSlot(si);
		//engine.scanWinLines(si);
		//engine.testWinLine();//TODO Debug
	}
}
