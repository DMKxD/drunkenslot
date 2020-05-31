package de.teamproject.drunkenslot.engine;

public class Win 
{
	private int playerID, amount;
	private boolean isAllPlayer, isShots, isRule, isDistribute;
	
	public Win()
	{
		this.setPlayerID(-1);
		this.setAllPlayer(false);
		this.setShots(false);
		this.setDistribute(false);
		this.setRule(false);
		this.setAmount(-1);
	}
	
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
