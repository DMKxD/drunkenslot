package de.teamproject.drunkenslot.swingclient;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private JLabel mainImageLabel;
	private BufferedImage slotPlaceHolderImage;
	private BufferedImage slotImages[];
	
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
	
	private void createMainFrame() 
	{
		mainFrame = new JFrame("Drunken Slot");
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setBounds(100, 100, 1000, 750);
		positionieren(mainFrame, 0, 0);
		setWindowListener();
		contentPane = new JPanel();
		//switchToGameScreen();
		//switchToMainScreen();
		switchToLobbyScreen();
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
	
	public void loadImage() throws IOException
	{
		BufferedImage placeholder = ImageIO.read(this.getClass().getResource("/TitleImagePlaceholder.png"));
		mainImageLabel = new JLabel(new ImageIcon(placeholder));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		slotPlaceHolderImage = ImageIO.read(this.getClass().getResource("/SlotImagePlaceholder.png"));
		
		slotImages = new BufferedImage[engine.getSymbolOffset() + engine.getPlayerList().size()];
		for(int i = 0; i < (engine.getSymbolOffset() + engine.getPlayerList().size()); i ++)
		{
			slotImages[i] = ImageIO.read(this.getClass().getResource("/slotImages/slotSymbol"+i+".png"));
		}
	}
	
	public BufferedImage[] getSlotImages()
	{
		return slotImages;
	}
	
	public BufferedImage getSlotPlaceHolderImage()
	{
		return slotPlaceHolderImage;
	}
	
	public JLabel getMainImageLabel()
	{
		return mainImageLabel;
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
