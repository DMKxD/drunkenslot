package de.teamproject.drunkenslot.engine;

import java.awt.Image;

/** 
 * GameModel interface, that gets Implemented by the Engine
 * provides the methodes createPlayer and createGame.
 * 
 * @author Dominik Haacke
 *
 */
public interface GameModel 
{
	public abstract void createPlayer(int id, String name, Image image);
	public abstract void createGame();
}