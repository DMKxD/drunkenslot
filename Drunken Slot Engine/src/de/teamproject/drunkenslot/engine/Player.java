package de.teamproject.drunkenslot.engine;

import java.awt.Image;

/**
 * Player class that stores the Player data.
 * @author Dominik Haacke
 *
 */
public class Player 
{
	private int id;
	private String name;
	private Image image;
	private boolean active = true;
	private int shots = 0;
	private int drinks = 0;
	private int playerSymbol;
	
	/**
	 * Constructor for the Player class
	 * @param id Player ID
	 * @param name Player name
	 * @param image Player image
	 */
	public Player(int id, String name, Image image)
	{
		this.id = id;
		this.name = name;
		this.image = image;
		playerSymbol = id + 11;
	}

	public int getId() 
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id  = id;
	}
	
	public int getPlayerSymbol()
	{
		return playerSymbol;
	}

	public String getName() 
	{
		return name;
	}

	public Image getImage() 
	{
		return image;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public String getIDString()
	{
		return "P"+id;
	}

	public int getShots() 
	{
		return shots;
	}

	public void setShots(int shots) 
	{
		this.shots = shots;
	}

	public int getDrinks() 
	{
		return drinks;
	}

	public void setDrinks(int drinks) 
	{
		this.drinks = drinks;
	}
}
