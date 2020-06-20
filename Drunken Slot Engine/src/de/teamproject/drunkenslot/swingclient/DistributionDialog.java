package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import de.teamproject.drunkenslot.engine.*;

public class DistributionDialog extends JDialog 
{
	
	private final JPanel contentPanel = new JPanel();
	private JPanel gridPanel;
	private JPanel buttonPane;
	private JLabel headerLaber;
	private JTextField[] amountTextFields;
	private JButton okButton = new JButton("Best\u00E4tigen");
	private JFrame parent;
	private final int amount;
	private Engine engine;
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
			
			int testAmount = 15;
			
			String testHeader = "Dominik bitte verteile 15 Shots";
			
			DistributionDialog dialog = new DistributionDialog(testAmount, engine, testHeader, new JFrame());
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			dialog.setVisible(true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public DistributionDialog(int amount, Engine engine, String header, JFrame parent) 
	{
		this.amount = amount;
		this.parent = parent;
		this.engine = engine;
		
		setBounds(100, 100, 600, 180);
		getContentPane().setLayout(new BorderLayout());
		
		
		createContentPanel();
		createHeaderLabel(header);
		createGridPanel(engine.getActivePlayers());
		createWindowListener();
		createButtonPanel();
		createButton();
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
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
		contentPanel.add(gridPanel);
	}
	
	public void createHeaderLabel(String header)
	{
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
		buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
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
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}
	
	public void calculateRoundArrays()
	{
		
	}
	
	public void createWindowListener()
	{
		this.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				for(int i = 0; i < amountTextFields.length; i ++)
				{
					
				}
			}
		});
	}
}
