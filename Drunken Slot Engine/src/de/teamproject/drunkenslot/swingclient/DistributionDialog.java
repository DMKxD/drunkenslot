package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import de.teamproject.drunkenslot.engine.*;

public class DistributionDialog extends JDialog 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPanel gridPanel;
	private JPanel buttonPanel;
	private JLabel headerLaber;
	private JTextField[] amountTextFields;
	private JButton okButton = new JButton("Best\u00E4tigen");
	private GameScreen parentGUI;
	private final int amount, playerID;
	private Engine engine;
	private boolean drinks;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		try 
		{
			GameConfig config = new GameConfig();
			config.createPlayer(Engine.getID(), "Dominik", null);
			config.createPlayer(Engine.getID(), "Jonas", null);
			config.createPlayer(Engine.getID(), "Test2", null);
			config.createPlayer(Engine.getID(), "Test3", null);
			config.createPlayer(Engine.getID(), "Test4", null);
			config.createPlayer(Engine.getID(), "Test5", null);
			config.createPlayer(Engine.getID(), "Test6", null);
			config.createPlayer(Engine.getID(), "Test7", null);
			config.createPlayer(Engine.getID(), "Test8", null);
			config.createPlayer(Engine.getID(), "Test9", null);
			
			Engine engine = new Engine(config);
			engine.createGame();
			engine.setPlayerInactive(4);
			int roundDrinksDistribute[] = {15, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
			engine.setRoundDrinksDistribute(roundDrinksDistribute);
			
			int testAmount = 15;
			
			DistributionDialog dialog = new DistributionDialog(new GameScreen(), engine, true, testAmount, 0);
			dialog.setVisible(true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public DistributionDialog(GameScreen parentGUI, Engine engine, boolean drinks, int amount, int playerID) 
	{
		this.amount = amount;
		this.parentGUI = parentGUI;
		this.engine = engine;
		this.drinks = drinks;
		this.playerID = playerID;
		
		setBounds(100, 100, 600, 200);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		createTitle();
		createContentPanel();
		createHeaderLabel();
		createGridPanel(engine.getActivePlayers());
		createButtonPanel();
		createButton();
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void createTitle() 
	{
		if(drinks)
		{
			if(amount > 1)
			{
				setTitle("Schlücke verteilen");
			}
			else
			{
				setTitle("Schluck verteilen");
			}
		}
		else
		{
			if(amount > 1)
			{
				setTitle("Shots verteilen");
			}
			else
			{
				setTitle("Shot verteilen");
			}
		}
	}

	public void createGridPanel(ArrayList<Player> activePlayer)
	{
		amountTextFields = new JTextField[activePlayer.size()];
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(2, 5, 10, 10));
		for(int i = 0; i < activePlayer.size(); i ++)
		{
			JPanel innerGridPanel = new JPanel();
			innerGridPanel.setLayout(new FlowLayout());
			JLabel playerNameLanel = new JLabel(activePlayer.get(i).getName()+": ");
			innerGridPanel.add(playerNameLanel);
			amountTextFields[i] = new JTextField();
			amountTextFields[i].setPreferredSize(new Dimension(25, 20));
			innerGridPanel.add(amountTextFields[i]);
			gridPanel.add(innerGridPanel);
			
		}
		gridPanel.setBorder(BorderFactory.createTitledBorder("Spieler"));
		contentPanel.add(gridPanel);
	}
	
	public void createHeaderLabel()
	{
		String header = engine.getPlayerList().get(playerID).getName()+
				" verteile "+amount+" "+(drinks ? ((amount > 1) ? "Schlücke!": "Schluck!") : ((amount > 1) ? "Shots!": "Shot!"));
		headerLaber = new JLabel(header);
		headerLaber.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(headerLaber);
	}
	
	public void createContentPanel()
	{
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
	}
	
	public void createButtonPanel()
	{
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	}
	
	public void createButton()
	{
		okButton.setActionCommand("OK");
		JDialog ref = this;
		okButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				int sum = 0;
				for(int i = 0; i < amountTextFields.length; i ++)
				{
					try 
					{
						if(!amountTextFields[i].getText().trim().equals("") && !amountTextFields[i].getText().equals(null))
						{
							sum += Integer.parseInt(amountTextFields[i].getText());
						}
					}
					catch (NumberFormatException x)
					{
						JOptionPane.showMessageDialog(ref,
							    "Bitte gib in Feld"+i+"eine Zahl ein!",
							    "Keine Zahl",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
				if(sum == amount)
				{
					calculateRoundArrays();
					ref.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(ref,
						    "Bitte verteile insgesamt "+amount+" "+(drinks ? ((amount > 1) ? "Schlücke!": "Schluck!") : ((amount > 1) ? "Shots!": "Shot!")),
						    "Zu wenig verteilt",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonPanel.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}
	
	public void calculateRoundArrays()
	{
		for(int i = 0; i < amountTextFields.length; i ++)
		{
			if(!amountTextFields[i].getText().trim().equals("") && !amountTextFields[i].getText().equals(null))
			{
				if(drinks)
				{
					int roundDrinks[] = engine.getRoundDrinks();
					int drinks = Integer.parseInt(amountTextFields[i].getText());
					roundDrinks[engine.getActivePlayers().get(i).getId()] += drinks;
					engine.setRoundDrinks(roundDrinks);
				}
				else//Shots
				{
					int roundShots[] = engine.getRoundShots();
					int shots = Integer.parseInt(amountTextFields[i].getText());
					roundShots[engine.getActivePlayers().get(i).getId()] += shots;
					engine.setRoundShots(roundShots);
				}
			}
		}
		if(drinks)
		{
			int distributeDrinks[] = engine.getRoundDrinksDistribute();
			distributeDrinks[playerID] -= amount;
			engine.setRoundDrinksDistribute(distributeDrinks);
		}
		else//Shots
		{
			int distributeShots[] = engine.getRoundShotsDistribute();
			distributeShots[playerID] -= amount;
			engine.setRoundShotsDistribute(distributeShots);
		}
		
		//Debug
		//System.out.println("Round Shots:"+Arrays.toString(engine.getRoundShots()));
		//System.out.println("Round Shots Distribute:"+Arrays.toString(engine.getRoundShotsDistribute()));
		//System.out.println("Round Drinks:"+Arrays.toString(engine.getRoundDrinks()));
		//System.out.println("Round Drinks Distribute:"+Arrays.toString(engine.getRoundDrinksDistribute()));
		//System.out.println("Round Rules:"+Arrays.toString(engine.getRoundRules()));
		//TODO parent methode wieder aufrufen
	}
}