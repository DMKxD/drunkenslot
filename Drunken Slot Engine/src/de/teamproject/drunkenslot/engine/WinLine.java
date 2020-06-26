package de.teamproject.drunkenslot.engine;

import java.util.ArrayList;

/**
 * WinLine class to calculate if there is a Win in the selected Line.
 * @author Dominik Haacke
 *
 */
public class WinLine 
{
	private int length;
	private int currentPlayer;
	private int playerSymbol;
	private int symbol;
	private int line;
	private int playerOffset;
	private int difficulty;
	private boolean allPlayer;
	private boolean isBroken;
	private ArrayList<Player> playerList;
	
	/**
	 * Constructor for the WinLine class
	 * @param currentPlayer Symbol of the current player not the id
	 * @param line number of the WinLine
	 */
	public WinLine(int currentPlayerSymbol, int line, int playerOffset, int difficulty, ArrayList<Player> playerList)
	{
		this.currentPlayer = currentPlayerSymbol;
		this.line = line;
		this.difficulty = difficulty;
		this.playerList = playerList;
		allPlayer = false;
		isBroken = false;
		symbol = -1;
		playerSymbol = -1;
		length = 0;
		this.playerOffset = playerOffset;
	}
	
	/**
	 * This method gets called for every step we take through a WinLine
	 * if the symbol breaks the WinLine it is set to isBroken. Else we
	 * check what Symbol it is, if the current Symbol is Wild or a Player
	 * Symbol it gets overwritten by the symbol, if the symbol is not
	 * a Wild or a Player Symbol. If there are two different Player Symbols
	 * in the Line, the Line will break. As long as the WinLine isn't broken
	 * we will increase its length with each step.
	 * @param symbol next Symbol in this Line
	 */
	public void setSymbol(int symbol)
	{
		//System.out.println("This:"+this.symbol+" Input:"+symbol);//Debug
		if(!isBroken)
		{
			if(symbol > 10 || symbol == 7)//If PlayerSymbol then check if match, else broken, override currentsymbol, if its the first time
			{
				if(playerSymbol == -1)
				{
					playerSymbol = symbol;
					if(this.symbol == -1)
					{
						this.symbol = symbol;
					}
				}
				else if(playerSymbol != symbol)
				{
					isBroken = true;
				}
			}
			else if(this.symbol == 6 || this.symbol == 7 || this.symbol >= 11)//Override current Symbol, if it is a Wild or allPlayerWild or player Symbol 
			{
				this.symbol = symbol;
			}
			else if(this.symbol == -1)
			{
				this.symbol = symbol;
			}
			else if(this.symbol != symbol)
			{
				if(symbol != 6 && symbol != 7)
				{
					isBroken = true;
				}
			}
			else if(this.symbol >= 8 && this.symbol <= 10)//Niete, somit brechen
			{
				isBroken = true;
			}
			if(symbol == 7 && !isBroken)//Symbol = allPlayerWild
			{
				allPlayer = true;
			}
			if(!isBroken)
			{
				//System.out.println("length ++");//Debug
				length ++;
			}
		}
	}
	
	/**
	 * Check if there is a Win in this WinLine
	 * @return Win or No Win true or false
	 */
	public boolean isWin()
	{
		int player = getPlayer() - playerOffset;
		if(player < 0)
		{
			if(this.symbol >= 8 && this.symbol <= 10 || this.symbol == 5)//Niete oder Scatter
			{
				return false;
			}
			if(length >= 3 + difficulty)
			{
				if(symbol == 4 && length == 5) //IF Rule
				{
					return true;
				}
				else if(symbol == 4 && length < 5)
				{
					return false;
				}
				return true;
			}
		}
		else if(player > 0 && playerList.get(player).isActive())
		{
			if(this.symbol >= 8 && this.symbol <= 10 || this.symbol == 5)//Niete oder Scatter
			{
				return false;
			}
			if(length >= 3 + difficulty)
			{
				if(symbol == 4 && length == 5) //IF Rule
				{
					return true;
				}
				else if(symbol == 4 && length < 5)
				{
					return false;
				}
				return true;
			}
		}
		return false;
	}
	
	public int getSymbol()
	{
		return symbol;
	}
	
	public int getPlayer()
	{
		if(playerSymbol == -1)
		{
			return currentPlayer;
		}
		else return playerSymbol;
	}
	
	public boolean isAllPlayer()
	{
		return allPlayer;
	}
	
	public int getLine()
	{
		return line;
	}
	
	public int getLength()
	{
		return length;
	}
	
	/**
	 * Sets WinLine to All Player Win
	 */
	public void setAllPlayer()
	{
		allPlayer = true;
	}
	
	/**
	 * Generate the Win from the WinLine if it exists.
	 * @return Win if there is a Win, or null if there isn't.
	 */
	public Win getWin()
	{
		if(isWin())
		{
			Win win = new Win();
			if(isAllPlayer())
			{
				win.setAllPlayer(true);
			}
			switch(symbol)
			{
		        case 0:
		        	win.setPlayerID(getPlayer()-playerOffset);
		        	win.setAmount(length -2);
		        	win.setShots(true);
		        	win.setDistribute(true);
		            break;
		        case 1:
		        	win.setPlayerID(getPlayer()-playerOffset);
		        	win.setAmount(length -2);
		        	win.setShots(false);
		        	win.setDistribute(true);
		            break;
		        case 2:
		        	win.setPlayerID(getPlayer()-playerOffset);
		        	win.setAmount(length -2);
		        	win.setShots(true);
		        	win.setDistribute(false);
		            break;
		        case 3:
		        	win.setPlayerID(getPlayer()-playerOffset);
		        	win.setAmount(length -2);
		        	win.setShots(false);
		        	win.setDistribute(false);
		            break;
		        case 4:
		        	win.setPlayerID(getPlayer()-playerOffset);
		        	win.setRule(true);
		            break;
		        default://Bei AllPlayerSymbol oder nur Wilds
		        	win.setPlayerID(getPlayer()-playerOffset);
		        	win.setAmount(length -2);
		        	win.setShots(true);
		        	win.setDistribute(true);
		            break;
	        }
			return win;
		}
		return null;
	}
	
	public String getWinLineText(ArrayList<Player> spielerListe)
	{
		String returnText = "";
		if(isWin())
		{
			if(isAllPlayer())
			{
				returnText += "Alle Spieler dürfen ";
			}
			switch(symbol)
			{
		        case 0:
		        	if((length - 2) > 1)
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Shots verteilen.\n";
		        		}
		        		else
		        		{
			        		returnText += (length - 2)+" Shots verteilen.\n";
		        		}
		        	}
		        	else
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Shot verteilen.\n";
		        		}
		        		else
		        		{
			        		returnText += (length - 2)+" Shot verteilen.\n";
		        		}
		        	}
		            break;
		        case 1:
		        	if((length - 2) > 1)
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Schlücke verteilen.\n";
		        		}
		        		else
		        		{
		        			returnText += (length - 2)+" Schlücke verteilen.\n";
		        		}
		        	}
		        	else
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Schluck verteilen.\n";
		        		}
		        		else
		        		{
		        			returnText += (length - 2)+" Schluck verteilen.\n";
		        		}
		        	}
		            break;
		        case 2:
		        	if((length - 2) > 1)
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Shots trinken.\n";
		        		}
		        		else
		        		{
		        			returnText += (length - 2)+" Shots trinken.\n";
		        		}
		        	}
		        	else
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Shot trinken.\n";
		        		}
		        		else
		        		{
			        		returnText += (length - 2)+" Shot trinken.\n";
		        		}
		        	}
		            break;
		        case 3:
		        	if((length - 2) > 1)
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Schlücke trinken.\n";
		        		}
		        		else
		        		{
		        			returnText += (length - 2)+" Schlücke trinken.\n";
		        		}
		        	}
		        	else
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Schluck trinken.\n";
		        		}
		        		else
		        		{
		        			returnText += (length - 2)+" Schluck trinken.\n";
		        		}
		        	}
		            break;
		        case 4:
		        	if(returnText == "")
	        		{
	        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf sich eine Regel ausdenken.\n";
	        		}
		        	else
		        	{
		        		returnText += (length - 2)+" sich eine Regel ausdenken, einer wird zufällig ausgesucht.\n";
		        	}
		            break;
		        default://Bei AllPlayerSymbol oder nur Wilds
		        	if((length - 2) > 1)
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Shots verteilen.\n";
		        		}
		        		else
		        		{
		        			returnText += (length - 2)+" Shots verteilen.\n";
		        		}
		        	}
		        	else
		        	{
		        		if(returnText == "")
		        		{
		        			returnText = spielerListe.get(getPlayer()-playerOffset).getName()+" darf "+(length - 2)+" Shot verteilen.\n";
		        		}
		        		else
		        		{
		        			returnText += (length - 2)+" Shot verteilen.\n";
		        		}
		        	}
		            break;
	        }
		}
		return returnText;
	}
}
