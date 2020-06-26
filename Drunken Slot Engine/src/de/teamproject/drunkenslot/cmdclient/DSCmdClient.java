package de.teamproject.drunkenslot.cmdclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;

import de.teamproject.drunkenslot.engine.Engine;
import de.teamproject.drunkenslot.engine.GameConfig;
import de.teamproject.drunkenslot.engine.SlotImage;

public class DSCmdClient 
{
	static Scanner sc;
	private Engine engine;
	
	private final int slotLineDelay = 8;
	private final int minRollCounter = 20;
	private Thread rollThread;
	private int rollCounter;
	private Thread highLightThread;
	private int slotLineDelayTimer;
	
	private boolean[] stopped = new boolean[5];
	private boolean hasShownHighlight = false;
	private int lastHighlight = 0;
	
	private String[][] slotSymbols;
	
	public DSCmdClient()
	{
		slotSymbols = new String[5][3];
		resetThreads();
		resetSlotSymbols();
		createDemoEngine();
		createRollThread();
		engine.roll();
		rollThread.start();
	}
	
	public void createRollThread()
	{
		rollThread = new Thread(new Runnable() 
		{
			
			@Override
			public void run() 
			{
				while(!isAllStopped())
				{
					try 
					{
						Thread.sleep(80);
					} 
					catch (InterruptedException e) 
					{
						//TODO
					}
					if(rollCounter == 0)
					{
						if(slotLineDelayTimer == 0)
						{
							slotLineDelayTimer = slotLineDelay;
							boolean stoppedOne = false;
							if(!stopped[0] && !stoppedOne)
							{
								stopped[0] = true;
								stoppedOne = true;
								fillSlotmachineLine(0);
							}
							
							if(!stopped[1] && !stoppedOne)
							{
								stopped[1] = true;
								stoppedOne = true;
								fillSlotmachineLine(1);
							}
							
							if(!stopped[2] && !stoppedOne)
							{
								stopped[2] = true;
								stoppedOne = true;
								fillSlotmachineLine(2);
							}
							
							if(!stopped[3] && !stoppedOne)
							{
								stopped[3] = true;
								stoppedOne = true;
								fillSlotmachineLine(3);
							}
							
							if(!stopped[4] && !stoppedOne)
							{
								stopped[4] = true;
								stoppedOne = true;
								fillSlotmachineLine(4);
							}
						}
						
						if(!stopped[0])
						{
							fillSlotmachineRandom(0);
						}
						if(!stopped[1])
						{
							fillSlotmachineRandom(1);
						}
						if(!stopped[2])
						{
							fillSlotmachineRandom(2);
						}
						if(!stopped[3])
						{
							fillSlotmachineRandom(3);
						}
						if(!stopped[4])
						{
							fillSlotmachineRandom(4);
						}
						
						slotLineDelayTimer --;
					}
					else
					{
						if(!stopped[0])
						{
							fillSlotmachineRandom(0);
						}
						if(!stopped[1])
						{
							fillSlotmachineRandom(1);
						}
						if(!stopped[2])
						{
							fillSlotmachineRandom(2);
						}
						if(!stopped[3])
						{
							fillSlotmachineRandom(3);
						}
						if(!stopped[4])
						{
							fillSlotmachineRandom(4);
						}
						rollCounter --;
					}
					clearScreen();
					printSlot();
				}
				/*if(isAllStopped())
				{
					//fillSlotmachine();
					//engine.printSlot(engine.getCurrentSlotImage());
					updateWinTextArea();
					showDialogs();
					Thread.
					highLightTimer.start();
					spinButton.setText("Drehen");
					spinButton.removeActionListener(stopActionListener);
					spinButton.addActionListener(spinActionListener);
					spinButton.setEnabled(false);
				}*/
			}
		});
	}
	
	public void createDemoEngine()
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
		config.createPlayer(Engine.getID(), "Nino", null);*/
		
		engine = new Engine(config);
		engine.createGame();
	}
	
	public void resetSlotSymbols()
	{
		for(int i = 0; i < 5; i ++)
		{
			for (int j = 0; j < 3; j++)
			{
				slotSymbols[i][j] = "XX";
			}
		}
	}
	
	public void resetThreads()
	{
		rollCounter = minRollCounter + ThreadLocalRandom.current().nextInt(0, 10 + 1);
		slotLineDelayTimer = slotLineDelay;
		hasShownHighlight = false;
		for(int i = 0; i < stopped.length; i ++)
		{
			stopped[i] = false;
		}
	}
	
	public void preGameLoop()
	{
		
	}
	
	public void initEngine(GameConfig config)
	{
		engine = new Engine(config);
		engine.createGame();
	}
	
	/**
	 * Demo GameLoop for testing the engine in CMD
	 * will be reused for offline client and server
	 * game loop.
	 */
	public void gameLoop()
	{
		sc = new Scanner(System.in);
		while(engine.isMoreThanOnePlayerActive())
		{
			if(!engine.isFreeGameEnabled())
			{
				if(!askPlayerForTurn())
				{
					engine.setPlayerInactive(engine.getCurrentPlayerID());
					showStandingsScreen();
					engine.updateCurrentPlayer();
					continue;
				}
			}
			else
			{
				System.out.println("Freispiel "+engine.getFreeSpinsLeft()+"/"+engine.getFreeSpinsTotal());
				engine.updateFreeGames();
				waitForEnter();
			}
			SlotImage si = engine.roll();
			printSlot();
			engine.scanWinLines(si);
			printWinLines();
			distributeRoundShots();
			distributeRoundDrinks();
			checkChangeRule();
			
			engine.finalizeRound();
			showStandingsScreen();
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {}
			waitForEnter();
			engine.updateCurrentPlayer();
		}
		showResultScreen();
		sc.close();
	}
	
	/**
	 * Print current standings in the Console
	 */
	public void showStandingsScreen()
	{
		System.out.println("----------------------------------------------------");
		System.out.println("Spieler			Shots			Drinks			Aktiv");
		for(int i = 0; i < engine.getPlayerList().size(); i ++)
		{
			System.out.println(engine.getPlayerList().get(i).getName()+"			"+engine.getPlayerList().get(i).getShots()+
								"			"+engine.getPlayerList().get(i).getDrinks()+"			"+engine.getPlayerList().get(i).isActive());
		}
		System.out.println("Regel: "+engine.getRule());
		System.out.println("----------------------------------------------------");
	}
	
	/**
	 * Print end results in the Console
	 */
	public void showResultScreen()
	{
		System.out.println("----------------------------------------------------");
		System.out.println();
		System.out.println("Gewinner: "+engine.getWinner().getName());
		System.out.println();
		System.out.println("Spieler			Shots			Drinks			Aktiv");
		for(int i = 0; i < engine.getPlayerList().size(); i ++)
		{
			System.out.println(engine.getPlayerList().get(i).getName()+"			"+engine.getPlayerList().get(i).getShots()+
								"			"+engine.getPlayerList().get(i).getDrinks()+"			"+engine.getPlayerList().get(i).isActive());
		}
		System.out.println("Regel: "+engine.getRule());
		System.out.println("----------------------------------------------------");
	}
	
	public void printWinLines()
	{
		for(int i = 0; i < engine.getCurrentWinLines().length; i ++)
		{
			System.out.print(engine.getCurrentWinLines()[i].winLineText());
		}
	}
	
	/**
	 * Work through the roundShotsDistribute Array and let each player distribute the Shots to another Player.
	 */
	public void distributeRoundShots()
	{
		for(int i = 0; i < engine.getRoundShotsDistribute().length; i ++)
		{
			if(engine.getRoundShotsDistribute()[i] != 0)
			{
				if(engine.getPlayerList().get(i).isActive())
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
		while(engine.getRoundShotsDistribute()[playerID] != 0)
		{
			System.out.println("Spieler "+engine.getPlayerList().get(playerID).getName()+" verteile noch "+engine.getRoundShotsDistribute()[playerID]+" Shot(s).");
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
			for(int i = 0; i < engine.getPlayerList().size(); i ++)
			{
				if(engine.getPlayerList().get(i).getName().equals(name))
				{
					if(engine.getPlayerList().get(i).isActive())
					{
						playerNameFound = true;
						targetPlayer = i;
						break;
					}
				}
			}
			if(playerNameFound)
			{
				System.out.print("Bitte gib die Anzahl an Shots ein [1-"+ engine.getRoundShotsDistribute()[playerID]+"]: ");
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
				if(amount <= 0 || amount > engine.getRoundShotsDistribute()[playerID])
				{
					System.out.println("Konnte angegebenen Spieler nicht finden!");
				}
				else
				{
					int[] roundShotsDistribute = engine.getRoundShotsDistribute();
					roundShotsDistribute[playerID] -= amount;
					engine.setRoundShotsDistribute(roundShotsDistribute);
					int[] roundShots = engine.getRoundShots();
					roundShots[targetPlayer] += amount;
					engine.setRoundShots(roundShots);
				}
			}
			else
			{
				System.out.println("Angegebener Spieler nicht vorhanden oder inaktiv!");
			}
		}
		System.out.println("----------------------------------------------------");
	}
	
	/**
	 * Work through the roundDrinksDistribute Array and let each player distribute the Drinks to another Player.
	 */
	public void distributeRoundDrinks()
	{
		for(int i = 0; i < engine.getRoundDrinksDistribute().length; i ++)
		{
			if(engine.getRoundDrinksDistribute()[i] != 0)
			{
				if(engine.getPlayerList().get(i).isActive())
				{
					distributeRoundDrinksLoop(i);
				}
			}
		}
	}
	
	/**
	 * Loop as long as the roundDrinksDistribute for playerID is not empty and
	 * let the player distribute the Drinks to the other players.
	 * @param playerID Id from the Player that can distribute Drinks
	 */
	public void distributeRoundDrinksLoop(int playerID)//TODO für Client-Server anpassen
	{
		System.out.println("----------------------------------------------------");
		while(engine.getRoundDrinksDistribute()[playerID] != 0)
		{
			System.out.println("Spieler "+engine.getPlayerList().get(playerID).getName()+" verteile noch "+engine.getRoundDrinksDistribute()[playerID]+" Schlücke.");
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
			for(int i = 0; i < engine.getPlayerList().size(); i ++)
			{
				if(engine.getPlayerList().get(i).getName().equals(name))
				{
					if(engine.getPlayerList().get(i).isActive())
					{
						playerNameFound = true;
						targetPlayer = i;
						break;
					}
				}
			}
			if(playerNameFound)
			{
				System.out.print("Bitte gib die Anzahl an Schlücken ein [1-"+ engine.getRoundDrinksDistribute()[playerID]+"]: ");
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
				if(amount <= 0 || amount > engine.getRoundDrinksDistribute()[playerID])
				{
					System.out.println("Konnte angegebenen Spieler nicht finden!");
				}
				else
				{
					int[] roundDrinksDistribute = engine.getRoundDrinksDistribute();
					roundDrinksDistribute[playerID] -= amount;
					engine.setRoundDrinksDistribute(roundDrinksDistribute);
					int[] roundDrinks = engine.getRoundDrinks();
					roundDrinks[targetPlayer] += amount;
					engine.setRoundDrinks(roundDrinks);
				}
			}
			else
			{
				System.out.println("Angegebener Spieler nicht vorhanden oder inaktiv!");
			}
		}
		System.out.println("----------------------------------------------------");
	}
	
	/**
	 * Work through the roundRules Array and pick a player randomly if more than 1 player can set a rule
	 */
	public void checkChangeRule()
	{
		int ruleID = -1;
		if(engine.isMoreThanOneRule())
		{
			ArrayList<Integer> ids = new ArrayList<Integer>();
			for(int i = 0; i < engine.getRoundRules().length; i ++)
			{
				if(engine.getRoundRules()[i] != 0)
				{
					ids.add(i);
				}
			}
			
			while(ids.size() > 0)
			{
				int nextRuleID = ids.get(ThreadLocalRandom.current().nextInt(0, ids.size()));
				if(engine.getPlayerList().get(nextRuleID).isActive())
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
			for(int i = 0; i < engine.getRoundRules().length; i ++)
			{
				if(engine.getRoundRules().length != 0)
				{
					if(engine.getPlayerList().get(i).isActive())
					{
						rulesLoop(i);
					}
				}
			}
		}
	}
	
	/**
	 * Loop as long as the player has not set a new rule correctly
	 * @param playerID Id from the Player that can set a Rule
	 */
	public void rulesLoop(int playerID) 
	{
		boolean nextRuleSet = false;
		while(!nextRuleSet)
		{
			System.out.println("----------------------------------------------------");
			System.out.print("Spieler "+engine.getPlayerList().get(playerID).getName()+" gib deine Regel ein: ");
			String ruleInput = sc.nextLine();
			if(ruleInput.trim().equals("") || ruleInput == null)
			{
				System.out.println("Keine Eingabe!");
				continue;
			}
			engine.setRule(ruleInput);
			nextRuleSet = true;
			System.out.println("----------------------------------------------------");
		}
	}
	
	/**
	 * Ask next player if he wants to surrender or play the next round
	 * @return boolean player plays or surrenders
	 */
	public boolean askPlayerForTurn()
	{
		boolean yesOrNo = false;
		while(yesOrNo == false)
		{
			System.out.print("Spieler "+engine.getPlayerList().get(engine.getCurrentPlayerID()).getName()+" möchtest du diese Runde spielen? [Ja / Nein]: ");
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
				System.out.println("Falsche Eingabe!");
			}
		}
		return false;
	}
	
	/**
	 * Print out the generated SlotImage
	 * @param si SlotImage generated by SlotMachine
	 */
	public void printSlot()
	{
		System.out.println(" Freispiele "+engine.getFreeSpinsLeft()+"/"+engine.getFreeSpinsTotal()+"     "
							+"Spieler: "+engine.getPlayerList().get(engine.getCurrentPlayerID()).getName());
		System.out.println("---------------------------------------");
		for(int y = 0; y < 3; y ++)
		{
			String line = "| (";
			for(int x = 0; x < 5; x ++)
			{
				if(x < 4)
				{
					line += " "+slotSymbols[x][y]+"    ";
				}
				else
				{
					line += " "+slotSymbols[x][y]+" ";
				}
			}
			line +=") |";
			System.out.println(line);
		}
		System.out.println("---------------------------------------");
	}
	
	public void fillSlotmachineLine(int column)
	{
		for(int y = 0; y < 3; y ++)
		{
			slotSymbols[column][y] = slotSymbolConverter(engine.getCurrentSlotImage().get(column, y));
		}
	}
	
	public void fillSlotmachineRandom(int column)
	{
		slotSymbols[column][2] = slotSymbols[column][1];
		slotSymbols[column][1] = slotSymbols[column][0];
		slotSymbols[column][0] = slotSymbolConverter(ThreadLocalRandom.current().nextInt(0, engine.getSymbolOffset() + engine.getPlayerList().size()));
	}
	
	public String slotSymbolConverter(int i)
	{
		switch(i)
		{
			case 0:
				return "DS";
			case 1:
				return "DD";
			case 2:
				return "TS";
			case 3:
				return "TD";
			case 4:
				return "RL";
			case 5:
				return "SC";
			case 6:
				return "WL";
			case 7:
				return "WA";
			case 8:
				return "NT";
			case 9:
				return "NT";
			case 10:
				return "NT";
			case 11:
				return "P0";
			case 12:
				return "P1";
			case 13:
				return "P2";
			case 14:
				return "P3";
			case 15:
				return "P4";
			case 16:
				return "P5";
			case 17:
				return "P6";
			case 18:
				return "P7";
			case 19:
				return "P8";
			case 20:
				return "P9";
		}
		return "";
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
	
    public void clearScreen()//TODO Windows specific
    {  
    	 final String os = System.getProperty("os.name");
         if (os.contains("Windows"))
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			try {
				Runtime.getRuntime().exec("clear");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        System.out.print("\n");
    }
    
    public boolean isAllStopped()
	{
		boolean isStopped = true;
		for(int i = 0; i < stopped.length; i ++)
		{
			isStopped = stopped[i];
		}
		return isStopped;
	}
}
