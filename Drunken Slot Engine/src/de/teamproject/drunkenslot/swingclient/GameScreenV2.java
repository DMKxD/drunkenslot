package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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

public class GameScreenV2 extends JFrame 
{
	private JPanel contentPane;
	private JPanel freeGamesPanel;
	private JPanel playerPanel;
	private JPanel slotPanel;
	private JPanel buttonPanel;
	
	private JLabel mainImageLabel;
	private JLabel freeGamesDescLabel;
	private JLabel freeGamesLabel;
	private JLabel playerDescLabel;
	private JLabel playerLabel;
	private JLabel[][] slotLabels;
	private BufferedImage slotPlaceHolderImage;
	
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
					GameScreenV2 frame = new GameScreenV2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void createDemoEngine()
	{
		GameConfig config = new GameConfig();
		config.createPlayer(Engine.getID(), "Dominik", null);
		config.createPlayer(Engine.getID(), "Jonas", null);
		
		Engine engine = new Engine(config);
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
	
	public void createSlotPanel()
	{
		slotPanel = new JPanel();
		slotPanel.setLayout(new GridLayout(3, 5));
		slotLabels = new JLabel[5][3];
		for(int i = 0; i < 5; i ++)
		{
			for(int j = 0; j < 3; j ++)
			{
				slotLabels[i][j] = new JLabel(new ImageIcon(slotPlaceHolderImage));
				slotLabels[i][j].setAlignmentX(Component.CENTER_ALIGNMENT);
				slotPanel.add(slotLabels[i][j]);
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
	}

	/**
	 * Create the frame.
	 */
	public GameScreenV2() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
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
		createDemoEngine();
		createWinTextArea();
		setGridBagLayout();
	}
	
	public void setGridBagLayout()
	{
		GridBagLayout gbl = new GridBagLayout();
		contentPane.setLayout(gbl);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		
		gbl.setConstraints(mainImageLabel, gbc);
		contentPane.add(mainImageLabel);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		
		gbl.setConstraints(freeGamesPanel, gbc);
		contentPane.add(freeGamesPanel);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		
		gbl.setConstraints(playerPanel, gbc);
		contentPane.add(playerPanel);
		
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 5;
		gbc.gridheight = 3;
		
		gbl.setConstraints(slotPanel, gbc);
		contentPane.add(slotPanel);
		
		gbc.gridx = 3;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		
		gbl.setConstraints(winTextArea, gbc);
		contentPane.add(winTextArea);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 5;
		gbc.gridheight = 2;
		
		gbl.setConstraints(buttonPanel, gbc);
		contentPane.add(buttonPanel);
		
	}
	
	public void loadImage() throws IOException
	{
		BufferedImage wPic = ImageIO.read(this.getClass().getResource("/TitleImagePlaceholder.png"));
		mainImageLabel = new JLabel(new ImageIcon(wPic));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		slotPlaceHolderImage = ImageIO.read(this.getClass().getResource("/SlotImagePlaceholder.png"));
	}

}
