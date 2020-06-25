package de.teamproject.drunkenslot.swingclient;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import de.teamproject.drunkenslot.engine.Engine;
import de.teamproject.drunkenslot.engine.GameConfig;

public class DrunkenSlotGUI 
{
	private JFrame mainFrame;
	private JPanel contentPane;
	private MainScreen mainScreen;
	private LobbyScreen lobbyScreen;
	private GameScreen gameScreen;
	private StandingsScreen standingsScreen;
	private EndScreen endScreen;
	private Engine engine;
	
	public DrunkenSlotGUI()
	{
		createDemoEngine();
		createScreens();
		createMainFrame();
	}
	
	public void createScreens()
	{
		mainScreen = new MainScreen(this);
		lobbyScreen = new LobbyScreen(this);
		gameScreen = new GameScreen(this);
		standingsScreen = new StandingsScreen(this);
		endScreen = new EndScreen(this);
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
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setBounds(100, 100, 1000, 750);
		positionieren(mainFrame, 0, 0);
		setWindowListener();
		contentPane = new JPanel();
		switchToGameScreen();
		//switchToMainScreen();
	}
	
	public void positionieren(Component component, int x, int y)
    {
        double lXKoordinate = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2.0 - component.getWidth() / 2.0;
        double lYKoordinate = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2.0 - component.getHeight() / 2.0;
        component.setLocation((int)lXKoordinate + x, (int)lYKoordinate + y);
    }
	
	public GameScreen getGameScreen()
	{
		return gameScreen;
	}
	
	public StandingsScreen getStandingsScreen()
	{
		return standingsScreen;
	}
	
	public EndScreen getEndScreen()
	{
		return endScreen;
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
		gameScreen.resetTimer();
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
	
	public void switchToEndScreen()
	{
		endScreen.updateTable();
		endScreen.setWinner(engine.getWinner().getName());
		contentPane = endScreen.getScreen();
		mainFrame.setContentPane(contentPane);
		mainFrame.revalidate();
		mainFrame.repaint();
	}
	
	public void switchToMainScreen()
	{
		contentPane = mainScreen.getScreen();
		mainFrame.setContentPane(contentPane);
		mainFrame.revalidate();
		mainFrame.repaint();
	}
	
	public void switchToLobbyScreen()
	{
		contentPane = lobbyScreen.getScreen();
		mainFrame.setContentPane(contentPane);
		mainFrame.revalidate();
		mainFrame.repaint();
	}
	
	public void setWindowListener()
	{
		mainFrame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Object[] options = {"Ja", "Nein"};
				int submit = JOptionPane.showOptionDialog(	mainFrame,
															"Möchten sie das Programm wirklich beenden?", 
															"Beenden",
															JOptionPane.YES_NO_OPTION,
															JOptionPane.QUESTION_MESSAGE,
															null,//TODO Icon
															options,
															options[1]);
				if(submit == JOptionPane.YES_OPTION)
				{
					mainFrame.dispose();
					System.exit(0);
				}
			}
		});
	}
	
	public void setEngine(Engine engine)
	{
		this.engine = engine;
		gameScreen.updateEngine();
		standingsScreen.updateEngine();
		endScreen.updateEngine();
	}

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
