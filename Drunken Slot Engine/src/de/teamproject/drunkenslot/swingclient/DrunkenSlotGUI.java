package de.teamproject.drunkenslot.swingclient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.teamproject.drunkenslot.engine.Engine;
import de.teamproject.drunkenslot.engine.GameConfig;

public class DrunkenSlotGUI 
{
	private JFrame mainFrame;
	private JPanel contentPane;
	private GameScreen gameScreen;
	private StandingsScreen standingsScreen;
	private Engine engine;
	
	public DrunkenSlotGUI()
	{
		createDemoEngine();
		createScreens();
		createMainFrame();
	}
	
	public void createScreens()
	{
		gameScreen = new GameScreen(this);
		standingsScreen = new StandingsScreen(this);
	}
	
	public void createDemoEngine()
	{
		GameConfig config = new GameConfig();
		config.createPlayer(Engine.getID(), "Dominik", null);
		config.createPlayer(Engine.getID(), "Jonas", null);
		
		engine = new Engine(config);
		engine.createGame();
	}
	
	private void createMainFrame() 
	{
		mainFrame = new JFrame("Drunken Slot");
		mainFrame.setBounds(100, 100, 1000, 750);
		contentPane = new JPanel();
		switchToGameScreen();
	}
	
	public GameScreen getGameScreen()
	{
		return gameScreen;
	}
	
	public Engine getEngine()
	{
		return engine;
	}
	
	public JFrame getMainFrame()
	{
		return mainFrame;
	}
	
	public void switchToGameScreen()
	{
		gameScreen.clearAndUpdateScreen();
		contentPane = gameScreen.getScreen();
		mainFrame.setContentPane(contentPane);
		mainFrame.revalidate();
		mainFrame.repaint();
	}
	
	public void switchToStandingsScreen()
	{
		standingsScreen.updateTable();
		contentPane = standingsScreen.getScreen();
		mainFrame.setContentPane(contentPane);
		mainFrame.revalidate();
		mainFrame.repaint();
	}

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					DrunkenSlotGUI gui = new DrunkenSlotGUI();
					gui.getMainFrame().setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
}
