package de.teamproject.drunkenslot.engine;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Engine implements GameModel
{
	private static int id = 0;
	private int currentPlayerSymbol = 8;//8 is player 0;
	private int currentPlayerID = 0;
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private SlotMachine slotMachine;
	private boolean isFreeGameEnabled = false;
	private int freeSpinsLeft = 0;
	private int freeSpinsAmount = 5;
	private int freeSpinsTotal = 0;
	private String rule = "";
	
	private int[] roundShots, roundDrinks, roundShotsDistribute, roundDrinksDistribute, roundRules;
	
	static Scanner sc;
	
	@Override
	public void createPlayer(int id, String name, Image image) 
	{
		playerList.add(new Player(id, name, image));
	}

	@Override
	public void createGame() //TODO mit Schwierigkeiten
	{
		roundShots = new int[playerList.size()];
		roundDrinks = new int[playerList.size()];
		roundShotsDistribute = new int[playerList.size()];
		roundDrinksDistribute = new int[playerList.size()];
		roundRules = new int[playerList.size()];
		createSlotMachine();
	}
	
	/**
	 * Demo GameLoop for testing the engine in CMD
	 * will be reused for offline client and server
	 * game loop.
	 */
	public void gameLoop()
	{
		sc = new Scanner(System.in);
		while(isMoreThanOnePlayerActive())
		{
			if(!isFreeGameEnabled)
			{
				if(!askPlayerForTurn())
				{
					playerList.get(currentPlayerID).setActive(false);
					showStandingsScreen();
					updateCurrentPlayer();
					continue;
				}
			}
			else
			{
				System.out.println("Freispiel "+freeSpinsLeft+"/"+freeSpinsTotal);
				updateFreeGames();
				waitForEnter();
			}
			SlotImage si = roll();
			printSlot(si);//DEBUG
			scanWinLines(si);
			distributeRoundShots();
			distributeRoundDrinks();
			checkChangeRule();
			
			finalizeRound();
			showStandingsScreen();
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {}
			waitForEnter();
			updateCurrentPlayer();
		}
		showResultScreen();
		sc.close();
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
	 * Wait for player to press Enter, used for Demo GameLoop
	 */
	public void waitForEnter()
	{
		System.out.println("Bitte drücke Eingabe zum fortsetzen...");
		try
		{
			System.in.read();
		}
		catch(Exception e){}
	}
	
	/**
	 * Print current standings in the Console
	 */
	public void showStandingsScreen()
	{
		System.out.println("----------------------------------------------------");
		System.out.println("Spieler			Shots			Drinks			Aktiv");
		for(int i = 0; i < playerList.size(); i ++)
		{
			System.out.println(playerList.get(i).getName()+"			"+playerList.get(i).getShots()+
								"			"+playerList.get(i).getDrinks()+"			"+playerList.get(i).isActive());
		}
		System.out.println("Regel: "+rule);
		System.out.println("----------------------------------------------------");
	}
	
	/**
	 * Print end results in the Console
	 */
	public void showResultScreen()
	{
		System.out.println("----------------------------------------------------");
		System.out.println();
		System.out.println("Gewinner: "+getWinner().getName());
		System.out.println();
		System.out.println("Spieler			Shots			Drinks			Aktiv");
		for(int i = 0; i < playerList.size(); i ++)
		{
			System.out.println(playerList.get(i).getName()+"			"+playerList.get(i).getShots()+
								"			"+playerList.get(i).getDrinks()+"			"+playerList.get(i).isActive());
		}
		System.out.println("Regel: "+rule);
		System.out.println("----------------------------------------------------");
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
	 * Create Slot Machine with the standard symbols 0 - 7
	 * and the player symbols 8-n
	 * 0 = Distribute Shot
	 * 1 = Distribute Drink
	 * 2 = Drink Shot
	 * 3 = Drink Drink
	 * 4 = Rule
	 * 5 = Scatter
	 * 6 = Wild
	 * 7 = All Player Wild
	 * 8-n = Player Symbol (Only Wild if symbols from one player is in the line, 2 different player symbols break a win line)
	 */
	public void createSlotMachine()
	{
		ArrayList<Integer> symbols = new ArrayList<Integer>();
		
		for(int i = 0; i < (8 + playerList.size()); i++)
		{
			symbols.add(i);
		}
		slotMachine = new SlotMachine(symbols);
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
	public SlotImage roll()//TODO Schwierigkeiten
	{
		if(isFreeGameEnabled)//First Iteration, alle Spieler Wilds durch normale ersetzen
		{
			SlotImage slotImage = slotMachine.generateRandom();
			for(int i = 0; i < slotImage.getLengthX(); i ++)
			{
				for (int j = 0; j < slotImage.getLengthY(); j++)
				{
					if(slotImage.get(i, j) >= slotMachine.getSymbolOffset())
					{
						slotImage.set(i, j, 6);
					}
				}
			}
			return slotImage;
		}
		else
		{
			return slotMachine.generateRandom();
		}
	}
	
	/**
	 * Scan the 9 WinLines for wins and save them in a WinLine Array.
	 * @param si Generated SlotImage from the Slotmachine
	 */
	public void scanWinLines(SlotImage si)//TODO codierung zum hin und her senden
	{
		WinLine winlines[] = new WinLine[9];
		winlines[0] = checkWinLine1(si);
		winlines[1] = checkWinLine2(si);
		winlines[2] = checkWinLine3(si);
		winlines[3] = checkWinLine4(si);
		winlines[4] = checkWinLine5(si);
		winlines[5] = checkWinLine6(si);
		winlines[6] = checkWinLine7(si);
		winlines[7] = checkWinLine8(si);
		winlines[8] = checkWinLine9(si);
		//TODO DEBUG and CMD
		for(int i = 0; i < winlines.length; i ++)
		{
			System.out.print(winlines[i].winLineText());
		}
		updateWinArrays(winlines);
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
							for(int j = 0; j < roundRules.length; j ++)
							{
								roundShotsDistribute[j] = roundShotsDistribute[j] + currentWin.getAmount();
							}
						}
						else
						{
							for(int j = 0; j < roundRules.length; j ++)
							{
								roundShots[j] = roundShots[j] + currentWin.getAmount();
							}
						}
					}
					else 
					{
						if(currentWin.isDistribute())
						{
							for(int j = 0; j < roundRules.length; j ++)
							{
								roundDrinksDistribute[j] = roundDrinksDistribute[j] + currentWin.getAmount();
							}
						}
						else
						{
							for(int j = 0; j < roundRules.length; j ++)
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
		//DEBUG
		System.out.println("Round Shots:"+Arrays.toString(roundShots));
		System.out.println("Round Shots Distribute:"+Arrays.toString(roundShotsDistribute));
		System.out.println("Round Drinks:"+Arrays.toString(roundDrinks));
		System.out.println("Round Drinks Distribute:"+Arrays.toString(roundDrinksDistribute));
		System.out.println("Round Rules:"+Arrays.toString(roundRules));
	}
	
	/**
	 * Work threw the roundShotsDistribute Array and let each player distribute the Shots to another Player.
	 */
	public void distributeRoundShots()
	{
		for(int i = 0; i < roundShotsDistribute.length; i ++)
		{
			if(roundShotsDistribute[i] != 0)
			{
				if(playerList.get(i).isActive())
				{
					distributeRoundShotsLoop(i);
				}
			}
		}
	}
	
	/**
	 * Loop as long as the roundShotsDistribute for playerID is not empty and
	 * let the player distribute the Shots to the other players.
	 * @param playerID Id from the Player that can distribute Shots
	 */
	public void distributeRoundShotsLoop(int playerID)//TODO für Client-Server anpassen
	{
		System.out.println("----------------------------------------------------");
		while(roundShotsDistribute[playerID] != 0)
		{
			System.out.println("Spieler "+playerList.get(playerID).getName()+" verteile noch "+roundShotsDistribute[playerID]+" Shot(s).");
			System.out.print("Bitte gib den Spielernamen ein: ");
			String name;
			try
			{
				name = sc.nextLine();
			}
			catch(NoSuchElementException e)
			{
				continue;
			}
			boolean playerNameFound = false;
			int targetPlayer = -1;
			for(int i = 0; i < playerList.size(); i ++)
			{
				if(playerList.get(i).getName().equals(name))
				{
					if(playerList.get(i).isActive())
					{
						playerNameFound = true;
						targetPlayer = i;
						break;
					}
				}
			}
			if(playerNameFound)
			{
				System.out.print("Bitte gib die Anzahl an Shots ein [1-"+ roundShotsDistribute[playerID]+"]: ");
				int amount = 0;
				try
				{
					amount = sc.nextInt();
				}
				catch(InputMismatchException e)
				{
					System.out.println("Eingabe ist keine Zahl!");
					continue;
				}
				;
				if(amount <= 0 || amount > roundShotsDistribute[playerID])
				{
					System.out.println("Konnte angegebenen Spieler nicht finden!");
				}
				else
				{
					roundShotsDistribute[playerID] -= amount;
					roundShots[targetPlayer] += amount;
				}
			}
			else
			{
				System.out.println("Angegebener Spieler nicht vorhanden oder inaktiv!");
			}
		}
		System.out.println("----------------------------------------------------");
	}
	
	public void distributeRoundDrinks()
	{
		for(int i = 0; i < roundDrinksDistribute.length; i ++)
		{
			if(roundDrinksDistribute[i] != 0)
			{
				if(playerList.get(i).isActive())
				{
					distributeRoundDrinksLoop(i);
				}
			}
		}
	}
	
	public void distributeRoundDrinksLoop(int playerID)//TODO für Client-Server anpassen
	{
		System.out.println("----------------------------------------------------");
		while(roundDrinksDistribute[playerID] != 0)
		{
			System.out.println("Spieler "+playerList.get(playerID).getName()+" verteile noch "+roundDrinksDistribute[playerID]+" Schlücke.");
			System.out.print("Bitte gib den Spielernamen ein: ");
			String name = "";
			try
			{
				name = sc.nextLine();
			}
			catch (NoSuchElementException e)
			{
				System.out.println("Dieser Spieler existiert nicht");
				continue;
			}
			boolean playerNameFound = false;
			int targetPlayer = -1;
			for(int i = 0; i < playerList.size(); i ++)
			{
				if(playerList.get(i).getName().equals(name))
				{
					if(playerList.get(i).isActive())
					{
						playerNameFound = true;
						targetPlayer = i;
						break;
					}
				}
			}
			if(playerNameFound)
			{
				System.out.print("Bitte gib die Anzahl an Schlücken ein [1-"+ roundDrinksDistribute[playerID]+"]: ");
				int amount = 0;
				try
				{
					amount = sc.nextInt();
				}
				catch(InputMismatchException e)
				{
					System.out.println("Eingabe ist keine Zahl!");
					continue;
				}
				;
				if(amount <= 0 || amount > roundDrinksDistribute[playerID])
				{
					System.out.println("Konnte angegebenen Spieler nicht finden!");
				}
				else
				{
					roundDrinksDistribute[playerID] -= amount;
					roundDrinks[targetPlayer] += amount;
				}
			}
			else
			{
				System.out.println("Angegebener Spieler nicht vorhanden oder inaktiv!");
			}
		}
		System.out.println("----------------------------------------------------");
	}
	
	public void checkChangeRule()
	{
		int ruleID = -1;
		if(isMoreThanOneRule())
		{
			ArrayList<Integer> ids = new ArrayList<Integer>();
			for(int i = 0; i < roundRules.length; i ++)
			{
				if(roundRules[i] != 0)
				{
					ids.add(i);
				}
			}
			Random random = new Random();
			while(ids.size() > 0)
			{
				int nextRuleID = ids.get(random.nextInt(ids.size()));
				if(playerList.get(nextRuleID).isActive())
				{
					ruleID = nextRuleID;
					break;
				}
				else
				{
					ids.remove(nextRuleID);
				}
				System.out.println("RULE");//TODO Debug
			}
			
		}
		if(ruleID != -1)
		{
			rulesLoop(ruleID);
		}
		else
		{
			for(int i = 0; i < roundRules.length; i ++)
			{
				if(roundRules[i] != 0)
				{
					if(playerList.get(i).isActive())
					{
						rulesLoop(i);
					}
				}
			}
		}
	}
	
	public void rulesLoop(int playerID) 
	{
		boolean nextRuleSet = false;
		while(!nextRuleSet)
		{
			System.out.println("----------------------------------------------------");
			System.out.print("Spieler "+playerList.get(playerID).getName()+" gib deine Regel ein: ");
			String ruleInput = sc.nextLine();
			if(ruleInput.trim().equals("") || ruleInput == null)
			{
				System.out.println("Keine Eingabe!");
				continue;
			}
			rule = ruleInput;
			nextRuleSet = true;
			System.out.println("----------------------------------------------------");
		}
	}

	
	public void testWinLine()//DEBUG Falls ein fehler auftritt hiermit den Fehler rekonstruieren und testen
	{
		WinLine line = new WinLine(currentPlayerSymbol, 1);
		line.setSymbol(2);
		line.setSymbol(9);
		line.setSymbol(9);
		line.setSymbol(7);
		line.setSymbol(6);
		System.out.print(line.winLineText());
	}
	
	public WinLine checkWinLine1(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 1);
		int y = 0;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	public WinLine checkWinLine2(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 2);
		int y = 1;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	public WinLine checkWinLine3(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 3);
		int y = 2;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	public WinLine checkWinLine4(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 4);
		line.setSymbol(si.get(0, 1));
		line.setSymbol(si.get(1, 0));
		line.setSymbol(si.get(2, 0));
		line.setSymbol(si.get(3, 0));
		line.setSymbol(si.get(4, 1));
		return line;
	}
	
	public WinLine checkWinLine5(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 5);
		line.setSymbol(si.get(0, 1));
		line.setSymbol(si.get(1, 2));
		line.setSymbol(si.get(2, 2));
		line.setSymbol(si.get(3, 2));
		line.setSymbol(si.get(4, 1));
		return line;
	}
	
	public WinLine checkWinLine6(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 6);
		line.setSymbol(si.get(0, 0));
		line.setSymbol(si.get(1, 1));
		line.setSymbol(si.get(2, 2));
		line.setSymbol(si.get(3, 1));
		line.setSymbol(si.get(4, 0));
		return line;
	}
	
	public WinLine checkWinLine7(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 7);
		line.setSymbol(si.get(0, 2));
		line.setSymbol(si.get(1, 1));
		line.setSymbol(si.get(2, 0));
		line.setSymbol(si.get(3, 1));
		line.setSymbol(si.get(4, 2));
		return line;
	}
	
	public WinLine checkWinLine8(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 8);
		line.setSymbol(si.get(0, 0));
		line.setSymbol(si.get(1, 0));
		line.setSymbol(si.get(2, 1));
		line.setSymbol(si.get(3, 2));
		line.setSymbol(si.get(4, 2));
		return line;
	}
	
	public WinLine checkWinLine9(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSymbol, 9);
		line.setSymbol(si.get(0, 2));
		line.setSymbol(si.get(1, 2));
		line.setSymbol(si.get(2, 1));
		line.setSymbol(si.get(3, 0));
		line.setSymbol(si.get(4, 0));
		return line;
	}
	
	public boolean askPlayerForTurn()
	{
		boolean yesOrNo = false;
		while(yesOrNo == false)
		{
			System.out.print("Spieler "+playerList.get(currentPlayerID).getName()+" möchtest du diese Runde spielen? [Ja / Nein]: ");
			String name;
			try
			{
				name = sc.nextLine();
			}
			catch(NoSuchElementException e)
			{
				continue;
			}
			if(name.equalsIgnoreCase("Ja"))
			{
				yesOrNo = true;
				return true;
			}
			else if(name.equalsIgnoreCase("Nein"))
			{
				yesOrNo = true;
				return false;
			}
			else
			{
				;
				System.out.println("Falsche Eingabe!");
			}
		}
		return false;
	}
	
	public void printSlot(SlotImage si)
	{
		System.out.println("\n");
		for(int y = 0; y < si.getLengthY(); y ++)
		{
			String line = "[";
			for(int x = 0; x < si.getLengthX(); x ++)
			{
				if(x < (si.getLengthX() - 1))
				{
					line += si.get(x, y)+" ";
				}
				else
				{
					line += si.get(x, y);
				}
			}
			line +="]";
			System.out.println(line);
		}
	}
	
	public boolean isFreeGames(SlotImage si)
	{
		int scatter = 0;
		for(int y = 0; y < si.getLengthY(); y ++)
		{
			for(int x = 0; x < si.getLengthX(); x ++)
			{
				if(si.get(x, y) == 5)
				{
					scatter ++;
				}
			}
		}
		if(scatter == 3)
		{
			return true;
		}
		return false;
	}
	
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
	
	public int[] getRoundShots()
	{
		return roundShots;
	}
	
	public int[] getRoundShotsDistribute()
	{
		return roundShotsDistribute;
	}
	
	public int[] getRoundDrinks()
	{
		return roundDrinks;
	}
	
	public int[] getRoundDrinksDistribute()
	{
		return roundDrinksDistribute;
	}
	
	public int[] getRoundRules()
	{
		return roundRules;
	}
	
	public static int getID()
	{
		return id++;
	}
	
	public String getRule()
	{
		return rule;
	}
	
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
				nextPlayer = playerList.get(i).getId();
			}
		}
		if(nextPlayer != -1)
		{
			return nextPlayer;
		}
		for(int i = 0; i < currentPlayerID; i++)
		{
			if(playerList.get(i).isActive())
			{
				nextPlayer = playerList.get(i).getId();
			}
		}
		return nextPlayer;
	}
	
	public ArrayList<Player> getPlayerList()
	{
		return playerList;
	}
	
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

	public int getCurrentPlayerSymbol() 
	{
		return currentPlayerSymbol;
	}

	public void setCurrentPlayerSymbol(int currentPlayerSymbol) 
	{
		this.currentPlayerSymbol = currentPlayerSymbol;
	}

	public int getCurrentPlayerID() 
	{
		return currentPlayerID;
	}

	public void setCurrentPlayerID(int currentPlayerID) 
	{
		this.currentPlayerID = currentPlayerID;
	}

	public boolean isFreeGameEnabled() 
	{
		return isFreeGameEnabled;
	}

	public int getFreeSpinsLeft() 
	{
		return freeSpinsLeft;
	}

	public int getFreeSpinsAmount() 
	{
		return freeSpinsAmount;
	}

	public int getFreeSpinsTotal() 
	{
		return freeSpinsTotal;
	}

	public void setSlotMachine(SlotMachine slotMachine) 
	{
		this.slotMachine = slotMachine;
	}
}
