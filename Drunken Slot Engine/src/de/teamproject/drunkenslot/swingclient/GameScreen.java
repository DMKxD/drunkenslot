package de.teamproject.drunkenslot.swingclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.SwingConstants;

public class GameScreen
{
	private JPanel contentPane;
	private JPanel freeGamesPanel;
	private JPanel playerPanel;
	private JPanel slotPanel;
	private JPanel buttonPanel;
	private JPanel topPanel;
	private JPanel outerSlotPanel;
	
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

	private final int slotLineDelay = 8;
	private final int minRollCounter = 20;
	private Timer rollTimer;
	private int rollCounter;
	private Timer highLightTimer;
	private int slotLineDelayTimer;
	
	private boolean[] stopped = new boolean[5];
	private boolean hasShownHighlight = false;
	private int lastHighlight = 0;
	private int borderThickness = 5;
	
	private ActionListener spinActionListener, stopActionListener;
	
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
		clearHighlights();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(mainImageLabel);
		contentPane.add(topPanel);
		outerSlotPanel = new JPanel();
		outerSlotPanel.add(slotPanel);
		contentPane.add(outerSlotPanel);
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
		hasShownHighlight = false;
		for(int i = 0; i < stopped.length; i ++)
		{
			stopped[i] = false;
		}
	}
	
	public void createTimer()
	{
		//TODO Slow start and Stop, 2 different Timers that get triggert from this one
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
					highLightTimer.start();
					spinButton.setText("Drehen");
					spinButton.removeActionListener(stopActionListener);
					spinButton.addActionListener(spinActionListener);
					spinButton.setEnabled(false);
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
		
		highLightTimer = new Timer(800, new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				highlightNextWinLine();
			}
		});
	}
	
	public void createButtons()
	{
		spinActionListener = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				spinButton.setText("Stop");
				spinButton.addActionListener(stopActionListener);
				surrenderButton.setEnabled(false);
				engine.roll();
				rollTimer.start();
				spinButton.removeActionListener(this);
			}
		};
		
		stopActionListener = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				spinButton.setText("Drehen");
				spinButton.addActionListener(spinActionListener);
				spinButton.setEnabled(false);
				updateWinTextArea();
				showDialogs();
				rollTimer.stop();
				highLightTimer.start();
				fillSlotmachine();
				spinButton.removeActionListener(this);
			}
		};
		
		spinButton.addActionListener(spinActionListener);
		continueButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				engine.finalizeRound();
				highLightTimer.stop();
				clearHighlights();
				if(engine.isMoreThanOnePlayerActive())
				{
					if(engine.isLogging())
					{
						drunkenSlotGUI.switchToStandingsScreen();
					}
					else
					{
						engine.clearRoundArrays();
						drunkenSlotGUI.switchToGameScreen();
					}
				}
				else
				{
					if(engine.isLogging())
					{
						drunkenSlotGUI.switchToEndScreen();
					}
					else
					{
						Object[] options = {"Beenden"};
						int n = JOptionPane.showOptionDialog(drunkenSlotGUI.getMainFrame(),
							    "Gewinner",
							    "Spieler "+engine.getWinner().getName()+" hat gewonnen",
							    JOptionPane.YES_OPTION,
							    JOptionPane.QUESTION_MESSAGE,
							    null,//TODO Icon
							    options,
							    options[1]);
						if(n == JOptionPane.YES_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							drunkenSlotGUI.switchToLobbyScreen();
						}
					}
				}
			}
		});
		
		surrenderButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(engine.isLogging())
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
						engine.finalizeRound();
						highLightTimer.stop();
						clearHighlights();
						engine.updateCurrentPlayer();
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
				else
				{
					engine.getPlayerList().get(engine.getCurrentPlayerID()).setActive(false);
					engine.finalizeRound();
					highLightTimer.stop();
					clearHighlights();
					engine.updateCurrentPlayer();
					if(engine.isMoreThanOnePlayerActive())
					{
						drunkenSlotGUI.switchToGameScreen();
					}
					else
					{
						Object[] options = {"Beenden"};
						int n = JOptionPane.showOptionDialog(drunkenSlotGUI.getMainFrame(),
							    "Spieler "+engine.getWinner().getName()+" hat gewonnen",
							    "Gewinner",
							    JOptionPane.YES_OPTION,
							    JOptionPane.QUESTION_MESSAGE,
							    null,//TODO Icon
							    options,
							    options[0]);
						if(n == JOptionPane.YES_OPTION || n == JOptionPane.CLOSED_OPTION)
						{
							drunkenSlotGUI.switchToLobbyScreen();
						}
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
		FlowLayout flowLayout = (FlowLayout) playerPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		//playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
		playerDescLabel = new JLabel("Spieler: ");
		playerDescLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		playerPanel.add(playerDescLabel);
		playerLabel = new JLabel("PlayerX");
		playerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
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
		for(int i = 0; i < 3; i ++)
		{
			for(int j = 0; j < 5; j ++)
			{
				slotLabels[j][i].setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255) , borderThickness, false));
			}
		}
	}
	
	public void highlightNextWinLine()
	{
		if(engine.hasWin() || engine.isFreeGames(engine.getCurrentSlotImage()))
		{
			if((!engine.hasWin() && engine.isFreeGames(engine.getCurrentSlotImage())) || engine.getWinCount() == 1)
			{
				if(hasShownHighlight)
				{
					clearHighlights();
					hasShownHighlight = !hasShownHighlight;
					slotPanel.revalidate();
					slotPanel.repaint();
				}
				else
				{
					switch(getNextHighlight())
					{
						case 0:
							highlightWinLine1(engine.getCurrentWinLines()[0].getLength());
							break;
						case 1:
							highlightWinLine2(engine.getCurrentWinLines()[1].getLength());
							break;
						case 2:
							highlightWinLine3(engine.getCurrentWinLines()[2].getLength());
							break;
						case 3:
							highlightWinLine4(engine.getCurrentWinLines()[3].getLength());
							break;
						case 4:
							highlightWinLine5(engine.getCurrentWinLines()[4].getLength());
							break;
						case 5:
							highlightWinLine6(engine.getCurrentWinLines()[5].getLength());
							break;
						case 6:
							highlightWinLine7(engine.getCurrentWinLines()[6].getLength());
							break;
						case 7:
							highlightWinLine8(engine.getCurrentWinLines()[7].getLength());
							break;
						case 8:
							highlightWinLine9(engine.getCurrentWinLines()[8].getLength());
							break;
						case 9:
							highlightScatter();
							break;
						default:
							clearHighlights();
							break;
					}
					slotPanel.revalidate();
					slotPanel.repaint();
					hasShownHighlight = !hasShownHighlight;
				}
			}
			else if(engine.hasWin())
			{
				if(hasShownHighlight)
				{
					clearHighlights();
					hasShownHighlight = !hasShownHighlight;
					slotPanel.revalidate();
					slotPanel.repaint();
				}
				else
				{
					int i = getNextHighlight();
					switch(i)
					{
						case 0:
							highlightWinLine1(engine.getCurrentWinLines()[0].getLength());
							break;
						case 1:
							highlightWinLine2(engine.getCurrentWinLines()[1].getLength());
							break;
						case 2:
							highlightWinLine3(engine.getCurrentWinLines()[2].getLength());
							break;
						case 3:
							highlightWinLine4(engine.getCurrentWinLines()[3].getLength());
							break;
						case 4:
							highlightWinLine5(engine.getCurrentWinLines()[4].getLength());
							break;
						case 5:
							highlightWinLine6(engine.getCurrentWinLines()[5].getLength());
							break;
						case 6:
							highlightWinLine7(engine.getCurrentWinLines()[6].getLength());
							break;
						case 7:
							highlightWinLine8(engine.getCurrentWinLines()[7].getLength());
							break;
						case 8:
							highlightWinLine9(engine.getCurrentWinLines()[8].getLength());
							break;
						case 9:
							highlightScatter();
							break;
						default:
							clearHighlights();
							break;
					}
					hasShownHighlight = !hasShownHighlight;
				}
				slotPanel.revalidate();
				slotPanel.repaint();
			}
		}
		else
		{
			highLightTimer.stop();
		}
	}
	
	public int getNextHighlight()
	{
		if((lastHighlight + 1) >= engine.getCurrentWinLines().length)
		{
			lastHighlight = 0;
		}
		for(int i = lastHighlight + 1; i < engine.getCurrentWinLines().length; i ++)
		{
			if(engine.getCurrentWinLines()[i].isWin())
			{
				lastHighlight = i;
				return i;
			}
		}
		if(engine.isFreeGames(engine.getCurrentSlotImage()))
		{
			lastHighlight = 9;
			return 9;
		}
		for(int i = 0; i < lastHighlight; i ++)
		{
			if(engine.getCurrentWinLines()[i].isWin())
			{
				lastHighlight = i;
				return i;
			}
		}
		return lastHighlight;
	}
	
	public void highlightWinLine1(int length)//Hellblau
	{
		int y = 0;
		for(int x = 0; x < length; x ++)
		{
			slotLabels[x][y].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255) , borderThickness, false));
		}
	}
	
	public void highlightWinLine2(int length)//Rot
	{
		int y = 1;
		for(int x = 0; x < length; x ++)
		{
			slotLabels[x][y].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0) , borderThickness, false));
		}
	}
	
	public void highlightWinLine3(int length)//Grün
	{
		int y = 2;
		for(int x = 0; x < length; x ++)
		{
			slotLabels[x][y].setBorder(BorderFactory.createLineBorder(new Color(0, 255, 0) , borderThickness, false));
		}
	}
	
	public void highlightWinLine4(int length)
	{
		if(length >= 3)
		{
			slotLabels[0][1].setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0) , borderThickness, false));
			slotLabels[1][0].setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0) , borderThickness, false));
			slotLabels[2][0].setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0) , borderThickness, false));
		}
		if(length >= 4)
		{
			slotLabels[3][0].setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0) , borderThickness, false));
		}
		if(length == 5)
		{
			slotLabels[4][1].setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0) , borderThickness, false));
		}
	}
	
	public void highlightWinLine5(int length)
	{
		if(length >= 3)
		{
			slotLabels[0][1].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255) , borderThickness, false));
			slotLabels[1][2].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255) , borderThickness, false));
			slotLabels[2][2].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255) , borderThickness, false));
		}
		if(length >= 4)
		{
			slotLabels[3][2].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255) , borderThickness, false));
		}
		if(length == 5)
		{
			slotLabels[4][1].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255) , borderThickness, false));
		}
	}
	
	public void highlightWinLine6(int length)
	{
		if(length >= 3)
		{
			slotLabels[0][0].setBorder(BorderFactory.createLineBorder(new Color(128, 255, 0) , borderThickness, false));
			slotLabels[1][1].setBorder(BorderFactory.createLineBorder(new Color(128, 255, 0) , borderThickness, false));
			slotLabels[2][2].setBorder(BorderFactory.createLineBorder(new Color(128, 255, 0) , borderThickness, false));
		}
		if(length >= 4)
		{
			slotLabels[3][1].setBorder(BorderFactory.createLineBorder(new Color(128, 255, 0) , borderThickness, false));
		}
		if(length == 5)
		{
			slotLabels[4][0].setBorder(BorderFactory.createLineBorder(new Color(128, 255, 0) , borderThickness, false));
		}
	}
	
	public void highlightWinLine7(int length)
	{
		if(length >= 3)
		{
			slotLabels[0][2].setBorder(BorderFactory.createLineBorder(new Color(255, 128, 0) , borderThickness, false));
			slotLabels[1][1].setBorder(BorderFactory.createLineBorder(new Color(255, 128, 0) , borderThickness, false));
			slotLabels[2][0].setBorder(BorderFactory.createLineBorder(new Color(255, 128, 0) , borderThickness, false));
		}
		if(length >= 4)
		{
			slotLabels[3][1].setBorder(BorderFactory.createLineBorder(new Color(255, 128, 0) , borderThickness, false));
		}
		if(length == 5)
		{
			slotLabels[4][2].setBorder(BorderFactory.createLineBorder(new Color(255, 128, 0) , borderThickness, false));
		}
	}
	
	public void highlightWinLine8(int length)
	{
		if(length >= 3)
		{
			slotLabels[0][0].setBorder(BorderFactory.createLineBorder(new Color(0, 128, 255) , borderThickness, false));
			slotLabels[1][0].setBorder(BorderFactory.createLineBorder(new Color(0, 128, 255) , borderThickness, false));
			slotLabels[2][1].setBorder(BorderFactory.createLineBorder(new Color(0, 128, 255) , borderThickness, false));
		}
		if(length >= 4)
		{
			slotLabels[3][2].setBorder(BorderFactory.createLineBorder(new Color(0, 128, 255) , borderThickness, false));
		}
		if(length == 5)
		{
			slotLabels[4][2].setBorder(BorderFactory.createLineBorder(new Color(0, 128, 255) , borderThickness, false));
		}
	}
	
	public void highlightWinLine9(int length)
	{
		if(length >= 3)
		{
			slotLabels[0][2].setBorder(BorderFactory.createLineBorder(new Color(128, 0, 255) , borderThickness, false));
			slotLabels[1][2].setBorder(BorderFactory.createLineBorder(new Color(128, 0, 255) , borderThickness, false));
			slotLabels[2][1].setBorder(BorderFactory.createLineBorder(new Color(128, 0, 255) , borderThickness, false));
		}
		if(length >= 4)
		{
			slotLabels[3][0].setBorder(BorderFactory.createLineBorder(new Color(128, 0, 255) , borderThickness, false));
		}
		if(length == 5)
		{
			slotLabels[4][0].setBorder(BorderFactory.createLineBorder(new Color(128, 0, 255) , borderThickness, false));
		}
	}
	
	public void highlightScatter()
	{
		for(int i = 0; i < 5; i ++)
		{
			for (int j = 0; j < 3; j++)
			{
				if(engine.getCurrentSlotImage().get(i, j) == 5)
				{
					slotLabels[i][j].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0) , borderThickness, false));
				}
			}
		}
	}
	
	public void showDialogs()
	{
		if(engine.isLogging())
		{
			boolean dialogShown = false;
			for(int i = 0; i < engine.getRoundDrinksDistribute().length; i++)
			{
				if(engine.getRoundDrinksDistribute()[i] != 0)
				{
					DistributionDialog distDialog = new DistributionDialog(drunkenSlotGUI, engine, true, engine.getRoundDrinksDistribute()[i], i);
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
						DistributionDialog distDialog = new DistributionDialog(drunkenSlotGUI, engine, false, engine.getRoundShotsDistribute()[i], i);
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
						RuleDialog ruleDialog = new RuleDialog(drunkenSlotGUI, engine, i);
						ruleDialog.setVisible(true);
						dialogShown = true;
						break;
					}
				}
			}
		}
		else
		{
			engine.clearRoundArrays();
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
	
	public void updateEngine()
	{
		this.engine = drunkenSlotGUI.getEngine();
		try 
		{
			loadImage();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
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
