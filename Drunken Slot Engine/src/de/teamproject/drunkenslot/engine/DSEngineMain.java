package de.teamproject.drunkenslot.engine;

public class DSEngineMain 
{
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
