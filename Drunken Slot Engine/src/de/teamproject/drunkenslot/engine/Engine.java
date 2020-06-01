package de.teamproject.drunkenslot.engine;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Engine implements GameModel
{
	private static int id = 0;
	private int currentPlayerSymbol = 8;//8 is player 0;
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private SlotMachine slotMachine;
	private boolean isFreeGameEnabled = false;
	private int freeSpinsLeft = 0;
	private int freeGamesAmount = 5;
	private String rule = "";
	
	private int[] roundShots, roundDrinks, roundShotsDistribute, roundDrinksDistribute;
	private int[] roundRules;
	
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
	
	public void gameLoop()
	{
		while(isMoreThanOnePlayerActive())
		{
			SlotImage si = roll();
			printSlot(si);//DEBUG
			scanWinLines(si);
			distributeRoundShots();
			distributeRoundDrinks();
			//TODO Rules and Freegames
			
			finalizeRound();
		}
	}
	
	public void finalizeRound()
	{
		for(int i = 0; i < roundShots.length; i ++)
		{
			playerList.get(i).setShots(playerList.get(i).getShots() + roundShots[i]);
		}
		
		for(int i = 0; i < roundDrinks.length; i ++)
		{
			playerList.get(i).setShots(playerList.get(i).getDrinks() + roundDrinks[i]);
		}
		
		clearRoundArrays();
	}
	
	private void clearRoundArrays() 
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
	
	public static int getID()
	{
		return id++;
	}
	
	public void createSlotMachine()
	{
		ArrayList<Integer> symbols = new ArrayList<Integer>();
		
		for(int i = 0; i < (8 + playerList.size()); i++)
		{
			//0 = Shot Verteilen
			//1 = Schluck Verteilen
			//2 = Shot Trinken
			//3 = Schluck Trinken
			//4 = Regel
			//5 = Scatter
			//6 = Wild
			//7 = Wild für alle Spieler
			//8-n = Player 0-n
			symbols.add(i);
		}
		slotMachine = new SlotMachine(symbols);
	}
	
	public void checkFreeGames(SlotImage si)
	{
		if(isFreeGames(si))
		{
			if(isFreeGameEnabled)
			{
				freeSpinsLeft = freeSpinsLeft + freeGamesAmount;
			}
			else
			{
				isFreeGameEnabled = true;
				freeSpinsLeft = freeGamesAmount;
			}
		}
	}
	
	public SlotImage roll()//TODO Schwierigkeiten
	{
		return slotMachine.generateRandom();
	}
	
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
		for(int i = 0; i < winlines.length; i ++)
		{
			System.out.print(winlines[i].winLineText());
		}
		updateWinArrays(winlines);
		//TODO Spieler die Shots zuweisen, und Schlücke verteilen einbauen.
	}
	
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
							roundRules[j] = roundRules[j] ++;
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
						roundRules[currentWin.getPlayerID()] = roundRules[currentWin.getPlayerID()] ++;
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
	
	public void distributeRoundShots()
	{
		for(int i = 0; i < roundShotsDistribute.length; i ++)
		{
			if(roundShotsDistribute[i] != 0)
			{
				distributeRoundShotsLoop(i);
			}
		}
	}
	
	public void distributeRoundShotsLoop(int playerID)//TODO für Client-Server anpassen
	{
		while(roundShotsDistribute[playerID] != 0)
		{
			System.out.println("Spieler "+playerList.get(playerID).getName()+" verteile noch "+roundShotsDistribute[playerID]+" Shot(s).");
			Scanner sc = new Scanner(System.in);
			System.out.print("Bitte gib den Spielernamen ein: ");
			String name = sc.next();
			boolean playerNameFound = false;
			int targetPlayer = -1;
			for(int i = 0; i < playerList.size(); i ++)
			{
				if(playerList.get(i).getName().equals(name))
				{
					playerNameFound = true;
					targetPlayer = i;
					break;
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
				sc.close();
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
				System.out.println("Konnte angegebenen Spieler nicht finden!");
			}
		}
	}
	
	public void distributeRoundDrinks()
	{
		for(int i = 0; i < roundDrinksDistribute.length; i ++)
		{
			if(roundDrinksDistribute[i] != 0)
			{
				distributeRoundDrinksLoop(i);
			}
		}
	}
	
	public void distributeRoundDrinksLoop(int playerID)//TODO für Client-Server anpassen
	{
		while(roundDrinksDistribute[playerID] != 0)
		{
			System.out.println("Spieler "+playerList.get(playerID).getName()+" verteile noch "+roundDrinksDistribute[playerID]+" Schlücke.");
			Scanner sc = new Scanner(System.in);
			System.out.print("Bitte gib den Spielernamen ein: ");
			String name = sc.next();
			boolean playerNameFound = false;
			int targetPlayer = -1;
			for(int i = 0; i < playerList.size(); i ++)
			{
				if(playerList.get(i).getName().equals(name))
				{
					playerNameFound = true;
					targetPlayer = i;
					break;
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
				sc.close();
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
				System.out.println("Konnte angegebenen Spieler nicht finden!");
			}
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
	
	public String getRule()
	{
		return rule;
	}
	
	public ArrayList<Player> getPlayerList()
	{
		return playerList;
	}
}
