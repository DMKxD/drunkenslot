package de.teamproject.drunkenslot.swingclient;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import de.teamproject.drunkenslot.engine.*;
import java.awt.FlowLayout;
import javax.swing.Box;

public class GameScreen
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
	private DrunkenSlotGUI drunkenSlotGUI;
	private Component rigidArea;
	private Component rigidArea_1;
	private Component rigidArea_2;
	
	private boolean freeGamesStartetThisRound = false;

	private final int slotLineDelay = 15;
	private final int minRollCounter = 20;
	private Timer rollTimer;
	private int rollCounter;
	private Timer highLightTimer;
	private int slotLineDelayTimer;
	
	private boolean[] stopped = new boolean[5];
	
	
	public GameScreen(DrunkenSlotGUI drunkenSlotGUI) 
	{
		this.drunkenSlotGUI = drunkenSlotGUI;
		this.engine = drunkenSlotGUI.getEngine();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
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
		resetTimer();
		createTimer();
		createButtons();
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(mainImageLabel);
		contentPane.add(topPanel);
		contentPane.add(slotPanel);
		contentPane.add(winTextArea);
		contentPane.add(buttonPanel);
	}
	
	public void clearAndUpdateScreen()
	{
		resetSlotmachine();
		playerLabel.setText(engine.getPlayerList().get(engine.getCurrentPlayerID()).getName());
		freeGamesLabel.setText(engine.getFreeSpinsLeft()+"/"+engine.getFreeSpinsTotal());
		spinButton.setEnabled(true);
		if(engine.isFreeGameEnabled())
		{
			surrenderButton.setEnabled(false);
		}
		else
		{
			surrenderButton.setEnabled(true);
		}
		continueButton.setEnabled(false);
		winTextArea.setText("");
	}
	
	public void resetTimer()
	{
		rollCounter = minRollCounter + ThreadLocalRandom.current().nextInt(0, 10 + 1);
		slotLineDelayTimer = slotLineDelay;
	}
	
	public void createTimer()
	{
		rollTimer = new Timer(80, new ActionListener()//TODO übergang zu den richtigen symbolen mit sloweren speed, anderen Timer starten, custom action listener;
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(isAllStopped())
				{
					//fillSlotmachine();
					//engine.printSlot(engine.getCurrentSlotImage());
					updateWinTextArea();
					showDialogs();
					rollTimer.stop();
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
				spinButton.setEnabled(false);
				surrenderButton.setEnabled(false);
				engine.roll();
				rollTimer.start();
			}
		});
		continueButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				engine.finalizeRound();
				if(engine.isMoreThanOnePlayerActive())
				{
					drunkenSlotGUI.switchToStandingsScreen();
				}
				else
				{
					drunkenSlotGUI.switchToEndScreen();
				}
			}
		});
		
		surrenderButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Object[] options = {"Ja","Nein"};
				int n = JOptionPane.showOptionDialog(drunkenSlotGUI.getMainFrame(),
					    "Wirklich aufgeben?",
					    "Spieler "+engine.getPlayerList().get(engine.getCurrentPlayerID()).getName()+" will aufgeben.",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,//TODO Icon
					    options,
					    options[1]);
				if(n == JOptionPane.YES_OPTION)
				{
					engine.getPlayerList().get(engine.getCurrentPlayerID()).setActive(false);
					if(engine.isMoreThanOnePlayerActive())
					{
						drunkenSlotGUI.switchToStandingsScreen();
					}
					else
					{
						drunkenSlotGUI.switchToEndScreen();
					}
				}
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
	
	public void fillSlotmachineLine(int x)
	{
		for(int y = 0; y < 3; y ++)
		{
			slotLabels[x][y].setIcon(new ImageIcon(slotImages[engine.getCurrentSlotImage().get(x, y)]));
			slotPanel.repaint();
		}
	}
	
	public void fillSlotmachineRandom(int x)//TODO Debug bei 10 Spielern
	{
		slotLabels[x][2].setIcon(slotLabels[x][1].getIcon());
		slotLabels[x][1].setIcon(slotLabels[x][0].getIcon());
		slotLabels[x][0].setIcon(new ImageIcon(slotImages[ThreadLocalRandom.current().nextInt(0, engine.getSymbolOffset() + engine.getPlayerList().size())]));
		slotPanel.repaint();
	}
	
	public void resetSlotmachine()
	{
		for(int i = 0; i < 3; i ++)
		{
			for(int j = 0; j < 5; j ++)
			{
				slotLabels[j][i].setIcon(new ImageIcon(slotPlaceHolderImage));
			}
		}
	}
	
	public void setEngine(Engine engine)
	{
		this.engine = engine;
	}
	
	public void createFreeGamesPanel()
	{
		freeGamesPanel = new JPanel();
		freeGamesPanel.setLayout(new BoxLayout(freeGamesPanel, BoxLayout.X_AXIS));
		freeGamesDescLabel = new JLabel("Freispiele: ");
		freeGamesPanel.add(freeGamesDescLabel);
		freeGamesLabel = new JLabel("0/0");
		freeGamesPanel.add(freeGamesLabel);
		freeGamesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	
	public void createPlayerPanel()
	{
		playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
		playerDescLabel = new JLabel("Spieler: ");
		playerPanel.add(playerDescLabel);
		playerLabel = new JLabel("PlayerX");
		playerPanel.add(playerLabel);
	}

	public void createTopPanel()
	{
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		freeGamesPanel.setAlignmentX(JFrame.LEFT_ALIGNMENT);
		topPanel.add(freeGamesPanel);
		
		rigidArea_2 = Box.createRigidArea(new Dimension(200, 20));
		topPanel.add(rigidArea_2);
		topPanel.add(playerPanel);
	}
	
	public void createSlotPanel()
	{
		slotPanel = new JPanel();
		slotPanel.setLayout(new GridLayout(3, 5, 10, 10));
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
		spinButton = new JButton("Drehen");
		buttonPanel.add(spinButton);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		buttonPanel.add(rigidArea);
		surrenderButton = new JButton("Aufgeben");
		buttonPanel.add(surrenderButton);
		
		rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		buttonPanel.add(rigidArea_1);
		continueButton = new JButton("Weiter");
		continueButton.setEnabled(false);
		buttonPanel.add(continueButton);
	}
	
	public void createWinTextArea()
	{
		winTextArea = new JTextArea("", 9, 50);
		winTextArea.setMinimumSize(new Dimension(600, 90));
		winTextArea.setPreferredSize(new Dimension(600, 90));
		winTextArea.setEditable(false);
		winTextArea.setBorder(BorderFactory.createTitledBorder("Gewinne:"));
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
		if(engine.isFreeGames(engine.getCurrentSlotImage()))
		{
			if(engine.isFreeGameEnabled())
			{
				engine.checkFreeGames(engine.getCurrentSlotImage());
				winTextArea.append(engine.getFreeSpinsAmount()+" weitere Freispiele hinzugefügt!");
			}
			else
			{
				engine.checkFreeGames(engine.getCurrentSlotImage());
				winTextArea.append(engine.getFreeSpinsAmount()+" Freispiele gewonnen!");
				freeGamesStartetThisRound = true;
			}
		}
	}
	
	public void clearHighlights()
	{
		
	}
	
	public void highlightNextWinLine()
	{
		
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
	
	public void showDialogs()
	{
		boolean dialogShown = false;
		for(int i = 0; i < engine.getRoundDrinksDistribute().length; i++)
		{
			if(engine.getRoundDrinksDistribute()[i] != 0)
			{
				DistributionDialog distDialog = new DistributionDialog(this, engine, true, engine.getRoundDrinksDistribute()[i], i);
				distDialog.setVisible(true);
				dialogShown = true;
				break;
			}
		}
		if(!dialogShown)
		{
			for(int i = 0; i < engine.getRoundShotsDistribute().length; i++)
			{
				if(engine.getRoundShotsDistribute()[i] != 0)
				{
					DistributionDialog distDialog = new DistributionDialog(this, engine, false, engine.getRoundShotsDistribute()[i], i);
					distDialog.setVisible(true);
					dialogShown = true;
					break;
				}
			}
		}
		if(!dialogShown)
		{
			for(int i = 0; i < engine.getRoundRules().length; i++)
			{
				if(engine.getRoundRules()[i] != 0)
				{
					RuleDialog ruleDialog = new RuleDialog(this, engine, i);
					ruleDialog.setVisible(true);
					dialogShown = true;
					break;
				}
			}
		}
		
		if(engine.allRoundArraysClear())
		{
			continueButton.setEnabled(true);
			if(!engine.isFreeGameEnabled())
			{
				engine.updateCurrentPlayer();
			}
			else
			{
				if(!freeGamesStartetThisRound)
				{
					engine.updateFreeGames();
				}
				else
				{
					freeGamesStartetThisRound = false;
				}
			}
		}
	}
	
	public void updateEngine()
	{
		this.engine = drunkenSlotGUI.getEngine();
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
	
	public JPanel getScreen()
	{
		return contentPane;
	}
}
