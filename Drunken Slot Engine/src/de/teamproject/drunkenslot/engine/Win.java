package de.teamproject.drunkenslot.engine;

/**
 * Class to store the WinData from a WinLine.
 * @author Dominik Haacke
 *
 */
public class Win 
{
	private int playerID, amount;
	private boolean isAllPlayer, isShots, isRule, isDistribute;
	
	/**
	 * Empty Constructor for the Win Class, set all values to negative or false
	 */
	public Win()
	{
		this.setPlayerID(-1);
		this.setAllPlayer(false);
		this.setShots(false);
		this.setDistribute(false);
		this.setRule(false);
		this.setAmount(-1);
	}
	
	/**
	 * Constructor for the Win Class
	 * @param playerID Player ID
	 * @param isAllPlayer boolean if this win affects all players
	 * @param isShots boolean if it is a shots win or a drink win
	 * @param isDistribute boolean if it is a distribute win
	 * @param isRule boolean if it is a rule win
	 * @param amount the amount of the win
	 */
	public Win(int playerID, boolean isAllPlayer, boolean isShots, boolean isDistribute, boolean isRule, int amount)
	{
		this.setPlayerID(playerID);
		this.setAllPlayer(isAllPlayer);
		this.setShots(isShots);
		this.setDistribute(isDistribute);
		this.setRule(isRule);
		this.setAmount(amount);
	}

	public int getAmount() 
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public int getPlayerID() 
	{
		return playerID;
	}

	public void setPlayerID(int playerID) 
	{
		this.playerID = playerID;
	}

	public boolean isShots() 
	{
		return isShots;
	}

	public void setShots(boolean isShots) 
	{
		this.isShots = isShots;
	}

	public boolean isAllPlayer()
	{
		return isAllPlayer;
	}

	public void setAllPlayer(boolean isAllPlayer) 
	{
		this.isAllPlayer = isAllPlayer;
	}

	public boolean isDistribute() 
	{
		return isDistribute;
	}

	public void setDistribute(boolean isDistribute) 
	{
		this.isDistribute = isDistribute;
	}

	public boolean isRule() 
	{
		return isRule;
	}

	public void setRule(boolean isRule) 
	{
		this.isRule = isRule;
	}
}
