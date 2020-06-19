package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import de.teamproject.drunkenslot.engine.*;
import java.awt.FlowLayout;
import javax.swing.JSplitPane;

public class GameScreenV4 extends JFrame 
{
	private JPanel contentPane;
	private JPanel freeGamesPanel;
	private JPanel playerPanel;
	private JPanel slotPanel;
	private JPanel buttonPanel;
	private JPanel topPanel;
	
	private JLabel mainImageLabel;
	private JLabel freeGamesDescLabel;
	private JLabel freeGamesLabel;
	private JLabel playerDescLabel;
	private JLabel playerLabel;
	private JLabel[][] slotLabels;
	private BufferedImage slotPlaceHolderImage;
	
	private BufferedImage[] slotImages;
	
	private JButton surrenderButton;
	private JButton spinButton;
	private JButton continueButton;
	
	private JTextArea winTextArea;
	
	private Engine engine;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameScreenV4 frame = new GameScreenV4();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void createButtons()
	{
		spinButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				engine.roll();
				//spinButton.setEnabled(false);
				//surrenderButton.setEnabled(false);
				fillSlotmachine();
				engine.printSlot(engine.getCurrentSlotImage());
				updateWinTextArea();
			}
		});
	}
	
	public void fillSlotmachine()
	{
		for(int i = 0; i < 3; i ++)
		{
			for(int j = 0; j < 5; j ++)
			{
				slotLabels[j][i].setIcon(new ImageIcon(slotImages[engine.getCurrentSlotImage().get(j, i)]));
			}
		}
		slotPanel.repaint();
	}
	
	public void createDemoEngine()
	{
		GameConfig config = new GameConfig();
		config.createPlayer(Engine.getID(), "Dominik", null);
		config.createPlayer(Engine.getID(), "Jonas", null);
		
		engine = new Engine(config);
		engine.createGame();
	}
	
	public void createFreeGamesPanel()
	{
		freeGamesPanel = new JPanel();
		freeGamesPanel.setLayout(new BoxLayout(freeGamesPanel, BoxLayout.X_AXIS));
		freeGamesDescLabel = new JLabel("Freegames: ");
		freeGamesPanel.add(freeGamesDescLabel);
		freeGamesLabel = new JLabel("0/0");
		freeGamesPanel.add(freeGamesLabel);
		freeGamesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	
	public void createPlayerPanel()
	{
		playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
		playerDescLabel = new JLabel("Player: ");
		playerPanel.add(playerDescLabel);
		playerLabel = new JLabel("PlayerX");
		playerPanel.add(playerLabel);
	}

	public void createTopPanel()
	{
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		freeGamesPanel.setAlignmentX(LEFT_ALIGNMENT);
		topPanel.add(freeGamesPanel);
		topPanel.add(playerPanel);
	}
	
	public void createSlotPanel()
	{
		slotPanel = new JPanel();
		slotPanel.setLayout(new GridLayout(3, 5));
		slotLabels = new JLabel[5][3];
		for(int i = 0; i < 3; i ++)
		{
			for(int j = 0; j < 5; j ++)
			{
				slotLabels[j][i] = new JLabel(new ImageIcon(slotPlaceHolderImage));
				slotLabels[j][i].setAlignmentX(Component.CENTER_ALIGNMENT);
				slotPanel.add(slotLabels[j][i]);
			}
		}
		slotPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	public void createButtonPanel()
	{
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		spinButton = new JButton("Spin");
		buttonPanel.add(spinButton);
		surrenderButton = new JButton("Surrender");
		buttonPanel.add(surrenderButton);
	}
	
	public void createWinTextArea()
	{
		winTextArea = new JTextArea("", 9, 50);
		winTextArea.setMinimumSize(new Dimension(600, 90));
		winTextArea.setPreferredSize(new Dimension(600, 90));
	}
	
	public void updateWinTextArea()
	{
		winTextArea.setText("");
		for(int i = 0; i < engine.getCurrentWinLines().length; i ++)
		{
			if(engine.getCurrentWinLines()[i].isWin())
			{
				winTextArea.append("Line "+(i+1)+": "+engine.getCurrentWinLines()[i].getWinLineText(engine.getPlayerList()));
			}
		}
	}
	
	public void highlightWinLines()
	{
		
	}

	/**
	 * Create the frame.
	 */
	public GameScreenV4() 
	{
		System.out.println("Hallo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		createDemoEngine();
		
		try 
		{
			loadImage();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		createFreeGamesPanel();
		createPlayerPanel();
		createSlotPanel();
		createButtonPanel();
		createWinTextArea();
		createTopPanel();
		
		createButtons();
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(mainImageLabel);
		contentPane.add(topPanel);
		contentPane.add(slotPanel);
		contentPane.add(winTextArea);
		contentPane.add(buttonPanel);
		System.out.println(spinButton.getActionListeners().length);
	}
	
	public void loadImage() throws IOException
	{
		BufferedImage wPic = ImageIO.read(this.getClass().getResource("/TitleImagePlaceholder.png"));
		mainImageLabel = new JLabel(new ImageIcon(wPic));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		slotPlaceHolderImage = ImageIO.read(this.getClass().getResource("/SlotImagePlaceholder.png"));
		
		slotImages = new BufferedImage[engine.getSymbolOffset() + engine.getPlayerList().size()];
		for(int i = 0; i < (engine.getSymbolOffset() + engine.getPlayerList().size()); i ++)
		{
			slotImages[i] = ImageIO.read(this.getClass().getResource("/slotImages/slotSymbol"+i+".png"));
		}
	}

}
