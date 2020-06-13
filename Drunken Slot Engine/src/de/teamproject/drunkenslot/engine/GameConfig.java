package de.teamproject.drunkenslot.engine;

import java.awt.Image;
import java.util.ArrayList;

/**
 * GameConfig class that will provide the data to the engine from the game lobby.
 * @author Dominik Haacke
 *
 */

public class GameConfig//TODO Der Engine übergeben sobald das spiel startet mit playerdaten, schwierigekeit
{

	private ArrayList<Player> playerList = new ArrayList<Player>();
	
	public void createPlayer(int id, String name, Image image) 
	{
		playerList.add(new Player(id, name, image));
	}
	
	public ArrayList<Player> getPlayerList()
	{
		return playerList;
	}
}
