package de.teamproject.drunkenslot.engine;

import java.awt.Image;
import java.util.ArrayList;

/**
 * GameConfig class that will provide the data to the engine from the game lobby.
 * @author Dominik Haacke
 *
 */

public class GameConfig
{

	private ArrayList<Player> playerList = new ArrayList<Player>();
	private int difficulty;
	private boolean logging;
	
	public GameConfig(int difficulty, boolean logging)
	{
		this.difficulty = difficulty;
		this.logging = logging;
	}
	
	public int getDifficulty()
	{
		return difficulty;
	}
	
	public boolean isLogging()
	{
		return logging;
	}

	public void createPlayer(int id, String name, Image image) 
	{
		playerList.add(new Player(id, name, image));
	}
	
	public ArrayList<Player> getPlayerList()
	{
		return playerList;
	}
}
