package de.teamproject.drunkenslot.cmdclient;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import de.teamproject.drunkenslot.engine.Engine;
import de.teamproject.drunkenslot.engine.SlotImage;

public class DSCmdClient 
{
	static Scanner sc;
	private Engine engine;
	
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
			printSlot(si);//DEBUG
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
			Random random = new Random();
			while(ids.size() > 0)
			{
				int nextRuleID = ids.get(random.nextInt(ids.size()));
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
	
    public void clearScreen() 
    {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}
