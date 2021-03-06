package de.teamproject.drunkenslot.engine;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Engine Class that runs the game and all background calculations.
 * @author Dominik Haacke
 *
 */
public class Engine implements GameModel
{
	private static int id = 0;
	private final int symbolOffset = 11;
	private int currentPlayerSymbol = 11;//11 is player 0;
	private int currentPlayerID = 0;
	private boolean isFreeGameEnabled = false;
	private int freeSpinsLeft = 0;
	private int freeSpinsAmount = 5;
	private int freeSpinsTotal = 0;
	private int difficulty = 0;
	private boolean logging = false;
	
	private String rule = "";
	private SlotImage currentSlotImage;
	private SlotMachine slotMachine;
	private WinLine[] currentWinLines;
	
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<Integer> alternativeSymbolList = new ArrayList<Integer>();
	
	private int[] roundShots, roundDrinks, roundShotsDistribute, roundDrinksDistribute, roundRules;

	static Scanner sc;
	
	/**
	 * Standard engine constructor, takes a config object to set all game values
	 * @param config
	 */
	public Engine(GameConfig config)
	{
		for(int i = 0; i < config.getPlayerList().size(); i++)
		{
			createPlayer(config.getPlayerList().get(i));
		}
		difficulty = config.getDifficulty();
		logging = config.isLogging();
		updateAlternativeSymbolList();
	}
	
	/*public static void main(String [] args)
	{
		GameConfig config = new GameConfig(0, true);
		config.createPlayer(Engine.getID(), "Dominik", null);
		config.createPlayer(Engine.getID(), "Jonas", null);
		/*config.createPlayer(Engine.getID(), "Peter", null);
		config.createPlayer(Engine.getID(), "Wilhelm", null);
		config.createPlayer(Engine.getID(), "Dagobert", null);
		config.createPlayer(Engine.getID(), "Alina", null);
		config.createPlayer(Engine.getID(), "Sophie", null);
		config.createPlayer(Engine.getID(), "Bruno", null);
		config.createPlayer(Engine.getID(), "Nike", null);
		config.createPlayer(Engine.getID(), "Nino", null);
		
		Engine engine = new Engine(config);
		engine.createGame();
		engine.testWinLine();
	}*/
	
	/**
	 * Set the alternative symbol list based on the player count.
	 * if the count is less or equal 2, add 2 more blank symbols,
	 * if the count is greater or equal 3 and less or equal 6
	 * add only 1 more blank symbol.
	 */
	private void updateAlternativeSymbolList() 
	{
		alternativeSymbolList = new ArrayList<Integer>();
		alternativeSymbolList.add(0);
		alternativeSymbolList.add(1);
		alternativeSymbolList.add(2);
		alternativeSymbolList.add(3);
		alternativeSymbolList.add(4);
		alternativeSymbolList.add(5);
		alternativeSymbolList.add(6);
		alternativeSymbolList.add(7);
		alternativeSymbolList.add(10);
		if(getActivePlayers().size() <= 2)
		{
			alternativeSymbolList.add(8);
			alternativeSymbolList.add(9);
		}
		else if(getActivePlayers().size() >= 3 && getActivePlayers().size() <= 6)
		{
			alternativeSymbolList.add(9);
		}
		for(int i = symbolOffset; i < (symbolOffset + playerList.size()); i ++)
		{
			alternativeSymbolList.add(i);
		}
	}

	@Override
	public void createPlayer(int id, String name, Image image) 
	{
		playerList.add(new Player(id, name, image));
	}
	
	/**
	 * Add a player object to the player List.
	 * @param player
	 */
	public void createPlayer(Player player)
	{
		playerList.add(player);
	}

	@Override
	public void createGame()
	{
		roundShots = new int[playerList.size()];
		roundDrinks = new int[playerList.size()];
		roundShotsDistribute = new int[playerList.size()];
		roundDrinksDistribute = new int[playerList.size()];
		roundRules = new int[playerList.size()];
		createSlotMachine();
	}
	
	/**
	 * Decrease FreeSpins Amount, if no FreeSpins remain, deactivate FreeGame Mode
	 */
	public void updateFreeGames()
	{
		freeSpinsLeft -= 1;
		if(freeSpinsLeft <= 0)
		{
			isFreeGameEnabled = false;
		}
	}
	
	/**
	 * Update the currentPlayer and currentPlayerSymbol
	 */
	public void updateCurrentPlayer()
	{
		currentPlayerID = getNextPlayerID();
		currentPlayerSymbol = getPlayerByID(currentPlayerID).getPlayerSymbol();
	}
	
	/**
	 * Add the roundShots and roundDrinks arrays in the player shots and drinks
	 * if the player is active
	 */
	public void finalizeRound()
	{
		for(int i = 0; i < roundShots.length; i ++)
		{
			if(playerList.get(i).isActive())
			{
				playerList.get(i).setShots(playerList.get(i).getShots() + roundShots[i]);
			}
		}
		
		for(int i = 0; i < roundDrinks.length; i ++)
		{
			if(playerList.get(i).isActive())
			{
				playerList.get(i).setDrinks(playerList.get(i).getDrinks() + roundDrinks[i]);
			}
		}
		
		clearRoundArrays();
	}
	
	/**
	 * Clear all round arrays to get them ready for the next round
	 */
	public void clearRoundArrays() 
	{
		for(int i = 0; i < playerList.size(); i++)
		{
			roundShots[i] = 0;
			roundDrinks[i] = 0;
			roundShotsDistribute[i] = 0;
			roundDrinksDistribute[i] = 0;
			roundRules[i] = 0;
		}
	}
	
	/**
	 * Create Slot Machine with the standard symbols 0 - n
	 * and the player symbols 11-n
	 * 0 = Distribute Shot
	 * 1 = Distribute Drink
	 * 2 = Drink Shot
	 * 3 = Drink Drink
	 * 4 = Rule
	 * 5 = Scatter
	 * 6 = Wild
	 * 7 = All Player Wild
	 * 8 = No Win Symbol for player count 2
	 * 9 = No Win Symbol for player count <= 4
	 * 10 = No Win Symbol always
	 * 11-n = Player Symbol (Only Wild if symbols from one player is in the line, 2 different player symbols break a win line)
	 */
	public void createSlotMachine()
	{
		ArrayList<Integer> symbols = new ArrayList<Integer>();
		
		for(int i = 0; i < (symbolOffset + playerList.size()); i++)
		{
			symbols.add(i);
		}
		slotMachine = new SlotMachine(symbols, symbolOffset);
	}
	
	/**
	 * If there are 3 Scatter Symbols in the current SlotImage active FreeGames, or if they are already active add the 
	 * freeSpinsAmount to freeSpinsLeft and freeSpinsTotal.
	 * @param si Current SlotImage returned from the SlotMachine
	 */
	public void checkFreeGames(SlotImage si)
	{
		if(isFreeGames(si))
		{
			if(isFreeGameEnabled)
			{
				freeSpinsLeft += freeSpinsAmount;
				freeSpinsTotal += freeSpinsAmount;
			}
			else
			{
				isFreeGameEnabled = true;
				freeSpinsLeft = freeSpinsAmount;
				freeSpinsTotal = freeSpinsAmount;
			}
		}
	}
	
	/**
	 * Generate a new random SlotImage, if free games are enabled replace all player symbols with normal wilds
	 * @return generated SlotImage
	 */
	public SlotImage roll()
	{
		SlotImage slotImage = slotMachine.generateRandom();
		
		for(int i = 0; i < slotImage.getLengthX(); i ++)
		{
			for (int j = 0; j < slotImage.getLengthY(); j++)
			{
				if(getActivePlayers().size() >= 3 && getActivePlayers().size() <= 6)//Niete 8 ersetzen
				{
					if(slotImage.get(i, j) == 8)
					{
						slotImage.set(i, j, alternativeSymbolList.get(ThreadLocalRandom.current().nextInt(0, alternativeSymbolList.size())));
					}
				}
				else if(getActivePlayers().size() >= 6) //Niete 8 + 9 ersetzen
				{
					if(slotImage.get(i, j) == 8)
					{
						slotImage.set(i, j, alternativeSymbolList.get(ThreadLocalRandom.current().nextInt(0, alternativeSymbolList.size())));
					}
					
					if(slotImage.get(i, j) == 9)
					{
						slotImage.set(i, j, alternativeSymbolList.get(ThreadLocalRandom.current().nextInt(0, alternativeSymbolList.size())));
					}
				}

				if(slotImage.get(i,j) == 5)//Reduce scatter occurrence
				{
					int keepScatter = ThreadLocalRandom.current().nextInt(0, 3);
					if(keepScatter > 0)
					{
						slotImage.set(i, j, alternativeSymbolList.get(ThreadLocalRandom.current().nextInt(0, alternativeSymbolList.size())));
					}
				}
				
				if(isFreeGameEnabled)//First Iteration, alle Spieler Wilds durch normale ersetzen
				{
					if(slotImage.get(i, j) >= slotMachine.getSymbolOffset())
					{
						slotImage.set(i, j, 6);
					}
				}
			}
		}
		currentSlotImage = slotImage;
		scanWinLines(slotImage);
		return currentSlotImage;
	}
	
	/**
	 * Scan the 9 WinLines for wins and save them in a WinLine Array.
	 * @param si Generated SlotImage from the Slotmachine
	 */
	public void scanWinLines(SlotImage si)
	{
		currentWinLines = new WinLine[9];
		currentWinLines[0] = checkWinLine1(si);
		currentWinLines[1] = checkWinLine2(si);
		currentWinLines[2] = checkWinLine3(si);
		currentWinLines[3] = checkWinLine4(si);
		currentWinLines[4] = checkWinLine5(si);
		currentWinLines[5] = checkWinLine6(si);
		currentWinLines[6] = checkWinLine7(si);
		currentWinLines[7] = checkWinLine8(si);
		currentWinLines[8] = checkWinLine9(si);
		
		updateWinArrays(currentWinLines);
	}
	
	/**
	 * Update the round Win Arrays roundRules, roundShotsDistribute, roundShots, roundDrinksDistribute and roundDrinks
	 * Get the Win Object from each WinLine that has a Win for calculation.
	 * @param winlines Generated with scanWinLines(SlotImage si)
	 */
	public void updateWinArrays(WinLine winlines[])
	{
		for(int i = 0; i < winlines.length; i ++)
		{
			if(winlines[i].isWin())
			{
				Win currentWin = winlines[i].getWin();
				if(currentWin.isAllPlayer())
				{
					if(currentWin.isRule())
					{
						for(int j = 0; j < roundRules.length; j ++)
						{
							roundRules[j] = 1;
						}
						
					}
					else if(currentWin.isShots())
					{
						if(currentWin.isDistribute())
						{
							for(int j = 0; j < roundShotsDistribute.length; j ++)
							{
								roundShotsDistribute[j] = roundShotsDistribute[j] + currentWin.getAmount();
							}
						}
						else
						{
							for(int j = 0; j < roundShots.length; j ++)
							{
								roundShots[j] = roundShots[j] + currentWin.getAmount();
							}
						}
					}
					else 
					{
						if(currentWin.isDistribute())
						{
							for(int j = 0; j < roundDrinksDistribute.length; j ++)
							{
								roundDrinksDistribute[j] = roundDrinksDistribute[j] + currentWin.getAmount();
							}
						}
						else
						{
							for(int j = 0; j < roundDrinks.length; j ++)
							{
								roundDrinks[j] = roundDrinks[j] + currentWin.getAmount();
							}
						}
					}
				}
				else
				{
					if(currentWin.isRule())
					{
						roundRules[currentWin.getPlayerID()] = 1;
					}
					else if(currentWin.isShots())
					{
						if(currentWin.isDistribute())
						{
							roundShotsDistribute[currentWin.getPlayerID()] = roundShotsDistribute[currentWin.getPlayerID()] + currentWin.getAmount();
						}
						else
						{
							roundShots[currentWin.getPlayerID()] = roundShots[currentWin.getPlayerID()] + currentWin.getAmount();
						}
					}
					else 
					{
						if(currentWin.isDistribute())
						{
							roundDrinksDistribute[currentWin.getPlayerID()] = roundDrinksDistribute[currentWin.getPlayerID()] + currentWin.getAmount();
						}
						else
						{
							roundDrinks[currentWin.getPlayerID()] = roundDrinks[currentWin.getPlayerID()] + currentWin.getAmount();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Test a WinLine, just for DEBUG to reproduce errors
	 */
	public void testWinLine()
	{
		WinLine line = new WinLine(currentPlayerSymbol, 1, symbolOffset, difficulty, playerList, logging);
		line.setSymbol(11);
		line.setSymbol(11);
		line.setSymbol(11);
		line.setSymbol(10);
		line.setSymbol(10);
		System.out.println(playerList.get(0).getPlayerSymbol());
		System.out.println(line.isWin());
		System.out.println(line.getSymbol());
		System.out.print(line.getWinLineText(playerList));
	}
	
	/**
	 * Check Line 1 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine1(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 1, symbolOffset, difficulty, playerList, logging);
		int y = 0;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	/**
	 * Check Line 2 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine2(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 2, symbolOffset, difficulty, playerList, logging);
		int y = 1;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	/**
	 * Check Line 3 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine3(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 3, symbolOffset, difficulty, playerList, logging);
		int y = 2;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	/**
	 * Check Line 4 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine4(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 4, symbolOffset, difficulty, playerList, logging);
		line.setSymbol(si.get(0, 1));
		line.setSymbol(si.get(1, 0));
		line.setSymbol(si.get(2, 0));
		line.setSymbol(si.get(3, 0));
		line.setSymbol(si.get(4, 1));
		return line;
	}
	
	/**
	 * Check Line 5 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine5(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 5, symbolOffset, difficulty, playerList, logging);
		line.setSymbol(si.get(0, 1));
		line.setSymbol(si.get(1, 2));
		line.setSymbol(si.get(2, 2));
		line.setSymbol(si.get(3, 2));
		line.setSymbol(si.get(4, 1));
		return line;
	}
	
	/**
	 * Check Line 6 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine6(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 6, symbolOffset, difficulty, playerList, logging);
		line.setSymbol(si.get(0, 0));
		line.setSymbol(si.get(1, 1));
		line.setSymbol(si.get(2, 2));
		line.setSymbol(si.get(3, 1));
		line.setSymbol(si.get(4, 0));
		return line;
	}
	
	/**
	 * Check Line 7 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine7(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 7, symbolOffset, difficulty, playerList, logging);
		line.setSymbol(si.get(0, 2));
		line.setSymbol(si.get(1, 1));
		line.setSymbol(si.get(2, 0));
		line.setSymbol(si.get(3, 1));
		line.setSymbol(si.get(4, 2));
		return line;
	}
	
	/**
	 * Check Line 8 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine8(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 8, symbolOffset, difficulty, playerList, logging);
		line.setSymbol(si.get(0, 0));
		line.setSymbol(si.get(1, 0));
		line.setSymbol(si.get(2, 1));
		line.setSymbol(si.get(3, 2));
		line.setSymbol(si.get(4, 2));
		return line;
	}
	
	/**
	 * Check Line 9 for a Win.
	 * @param si SlotImage generated by SlotMachine
	 * @return Calculated WinLine
	 */
	public WinLine checkWinLine9(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 9, symbolOffset, difficulty, playerList, logging);
		line.setSymbol(si.get(0, 2));
		line.setSymbol(si.get(1, 2));
		line.setSymbol(si.get(2, 1));
		line.setSymbol(si.get(3, 0));
		line.setSymbol(si.get(4, 0));
		return line;
	}
	
	/**
	 * Check SlotImage if it contains Scatter Symbols
	 * @param si SlotImage generated by SlotMachine
	 * @return true if there are 3 or more Scatter Symbols
	 */
	public boolean isFreeGames(SlotImage si)
	{
		int scatter = 0;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			for(int y = 0; y < si.getLengthY(); y ++)
			{
				if(si.get(x, y) == 5)
				{
					scatter ++;
				}
			}
		}
		if(scatter >= 3)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check if there is more than one Player in the current round, that can set a Rule.
	 * @return true if there is more than one player that can set a Rule.
	 */
	public boolean isMoreThanOneRule()
	{
		int ruleCounter = 0;
		for(int i = 0; i < roundRules.length; i ++)
		{
			if(roundRules[i] != 0)
			{
				ruleCounter ++;
			}
		}
		if(ruleCounter > 1)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check active players.
	 * @return true if there is more than one player active.
	 */
	public boolean isMoreThanOnePlayerActive()
	{
		int playerActiveCounter = 0;
		for(int i = 0; i < playerList.size(); i ++)
		{
			if(playerList.get(i).isActive())
			{
				playerActiveCounter ++;
			}
		}
		if(playerActiveCounter > 1)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Return shots Array in current Round 
	 * @return roundShots
	 */
	public int[] getRoundShots()
	{
		return roundShots;
	}
	
	/**
	 * Return shots to distribute Array in current Round 
	 * @return roundShotsDistribute
	 */
	public int[] getRoundShotsDistribute()
	{
		return roundShotsDistribute;
	}
	
	/**
	 * Return drinks Array in current Round
	 * @return roundDrinks
	 */
	public int[] getRoundDrinks()
	{
		return roundDrinks;
	}
	
	/**
	 * Return drinks to distribute Array in current Round 
	 * @return roundDrinksDistribute
	 */
	public int[] getRoundDrinksDistribute()
	{
		return roundDrinksDistribute;
	}
	
	/**
	 * Return rules Array in current Round
	 * @return roundRules
	 */
	public int[] getRoundRules()
	{
		return roundRules;
	}
	
	/**
	 * Generate the next Player ID, maybe only use playerList index?
	 * @return id
	 */
	public static int getID()
	{
		return id++;
	}
	
	/**
	 * Get the currently active Rule
	 * @return rule
	 */
	public String getRule()
	{
		return rule;
	}
	
	/**
	 * Get the id for the next players turn
	 * @return nextPlayerID
	 */
	public int getNextPlayerID()
	{
		int nextPlayer = -1;
		for(int i = currentPlayerID; i < playerList.size(); i++)
		{
			if(i == currentPlayerID)
			{
				continue;
			}
			if(playerList.get(i).isActive())
			{
				return nextPlayer = playerList.get(i).getId();
			}
		}
		if(nextPlayer >= 0)
		{
			return nextPlayer;
		}
		for(int i = 0; i < currentPlayerID; i++)
		{
			if(playerList.get(i).isActive())
			{
				return nextPlayer = playerList.get(i).getId();
			}
		}
		return nextPlayer;
	}
	
	/**
	 * Get the games player list
	 * @return playerList
	 */
	public ArrayList<Player> getPlayerList()
	{
		return playerList;
	}
	
	/**
	 * If there is only one player who is active, return that player
	 * @return last active player
	 */
	public Player getWinner()
	{
		if(isMoreThanOnePlayerActive())
		{
			return null;
		}
		else
		{
			for(int i = 0; i < playerList.size(); i ++)
			{
				if(playerList.get(i).isActive())
				{
					return playerList.get(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the player from the playerList by id
	 * @param id ID of the Player
	 * @return Player with ID, if he exists
	 */
	public Player getPlayerByID(int id)
	{
		for(int i = 0; i < playerList.size(); i ++)
		{
			if(playerList.get(i).getId() == id)
			{
				return playerList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Get the player from the playerList by name
	 * @param name Name of the Player
	 * @return Player with name, if he exists
	 */
	public Player getPlayerByName(String name)
	{
		for(int i = 0; i < playerList.size(); i ++)
		{
			if(playerList.get(i).getName().equals(name))
			{
				return playerList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Get a list of all active Players
	 * @return active Players
	 */
	public ArrayList<Player> getActivePlayers()
	{
		ArrayList<Player> activeList = new ArrayList<Player>();
		for(int i = 0; i <  playerList.size(); i++)
		{
			if(playerList.get(i).isActive())
			{
				activeList.add(playerList.get(i));
			}
		}
		return activeList;
	}
	
	/**
	 * Get the current symbol of the player
	 * @return currentPlayerSymbol
	 */
	public int getCurrentPlayerSymbol() 
	{
		return currentPlayerSymbol;
	}

	/**
	 * Set the current symbol of the player
	 * @param currentPlayerSymbol Symbol of the Player
	 */
	public void setCurrentPlayerSymbol(int currentPlayerSymbol) 
	{
		this.currentPlayerSymbol = currentPlayerSymbol;
	}

	/**
	 * Get the ID of the current Player
	 * @return currentPlayerID
	 */
	public int getCurrentPlayerID() 
	{
		return currentPlayerID;
	}
	
	/**
	 * Get the win count, all wins from all winlines are added together.
	 * @return Number of wins
	 */
	public int getWinCount()
	{
		int count = 0;
		for(int i = 0; i < currentWinLines.length; i ++)
		{
			if(currentWinLines[i].isWin())
			{
				count ++;
			}
		}
		return count;
	}
	
	/**
	 * Get the set difficulty
	 * @return difficulty [0-2]
	 */
	public int getDifficulty()
	{
		return difficulty;
	}

	/**
	 * Set the ID of the current Player
	 * @param currentPlayerID override current player id
	 */
	public void setCurrentPlayerID(int currentPlayerID) 
	{
		this.currentPlayerID = currentPlayerID;
	}
	
	/**
	 * Set the round shots array
	 * @param roundShots array
	 */
	public void setRoundShots(int[] roundShots)
	{
		this.roundShots = roundShots;
	}
	
	/**
	 * Set the round drinks array
	 * @param roundDrinks array
	 */
	public void setRoundDrinks(int[] roundDrinks)
	{
		this.roundDrinks = roundDrinks;
	}
	
	/**
	 * Set the round rules array
	 * @param roundRules array
	 */
	public void setRoundRules(int[] roundRules)
	{
		this.roundRules = roundRules;
	}
	
	/**
	 * Set the round drinks distribute array
	 * @param roundDrinksDistribute array
	 */
	public void setRoundDrinksDistribute(int[] roundDrinksDistribute)
	{
		this.roundDrinksDistribute = roundDrinksDistribute;
	}
	
	/**
	 * Set the round shots distribute array
	 * @param roundShotsDistribute array
	 */
	public void setRoundShotsDistribute(int[] roundShotsDistribute)
	{
		this.roundShotsDistribute = roundShotsDistribute;
	}
	
	/**
	 * Set the round rule
	 * @param rule String
	 */
	public void setRule(String rule)
	{
		this.rule = rule;
	}
	
	/**
	 * Disable a player, if he surrenders
	 * @param playerId number
	 */
	public void setPlayerInactive(int playerId)
	{
		playerList.get(playerId).setActive(false);
		updateAlternativeSymbolList();
	}

	/**
	 * Check if free games are enabled
	 * @return isFreeGameEnabled
	 */
	public boolean isFreeGameEnabled() 
	{
		return isFreeGameEnabled;
	}
	
	/**
	 * Adds all round array values together and checks if they are 0
	 * @return all round arrays 0
	 */
	public boolean allRoundArraysClear()
	{
		int sum = 0;
		for (int i = 0; i < roundDrinksDistribute.length; i ++)
		{
			sum += roundDrinksDistribute[i]+roundShotsDistribute[i]+roundRules[i];
		}
		if(sum == 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check if atleast one winline has a win
	 * @return current wins > 0
	 */
	public boolean hasWin()
	{
		for(int i = 0; i < currentWinLines.length; i ++)
		{
			if(currentWinLines[i].isWin())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if atleast one entry in the roundDrinks or roundShots array > 0
	 * @return boolean true or false
	 */
	public boolean hasRoundShotsOrDrinks()
	{
		for(int i = 0; i < roundDrinks.length; i ++)
		{
			if(roundDrinks[i] > 0 || roundShots[i] > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns if logging was activated in the settings
	 * @return logging
	 */
	public boolean isLogging()
	{
		return logging;
	}

	/**
	 * Get the number of the remaining free spins
	 * @return freeSpinsLeft
	 */
	public int getFreeSpinsLeft() 
	{
		return freeSpinsLeft;
	}

	/**
	 * Get the amount of free spins, that will be set, if a new free game round starts
	 * @return freeSpinsAmount
	 */
	public int getFreeSpinsAmount() 
	{
		return freeSpinsAmount;
	}

	/**
	 * Return the total number of free spins in the current free game round
	 * @return freeSpinsTotal
	 */
	public int getFreeSpinsTotal() 
	{
		return freeSpinsTotal;
	}
	
	/**
	 * Return the symbol offset for the normal symbols to the player symbols
	 * @return symbolOffset
	 */
	public int getSymbolOffset()
	{
		return symbolOffset;
	}
	
	/**
	 * Return the current SlotImage generated by the SlotMachine
	 * @return SlotImage
	 */
	public SlotImage getCurrentSlotImage()
	{
		return currentSlotImage;
	}
	
	/**
	 * Return all WinLines in the current round
	 * @return WinLine array
	 */
	public WinLine[] getCurrentWinLines()
	{
		return currentWinLines;
	}
}