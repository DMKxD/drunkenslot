package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.teamproject.drunkenslot.engine.Engine;
import de.teamproject.drunkenslot.engine.GameConfig;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RuleDialog extends JDialog 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPanel buttonPanel;
	private JPanel rulePanel;
	private JLabel headerLabel;
	private Engine engine;
	private final int playerID;
	private JButton okButton;
	
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
			int roundRules[] = {0, 0, 1, 0, 0, 0, 0, 0, 0, 0,};
			engine.setRoundRules(roundRules);
			
			RuleDialog dialog = new RuleDialog(new DrunkenSlotGUI(), engine, 0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RuleDialog(DrunkenSlotGUI parentGUI, Engine engine, int playerID) 
	{
		this.engine = engine;
		this.playerID = playerID;
		setBounds(100, 100, 500, 130);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		createContentPanel();
		createHeaderLabel();
		createRulePanel();
		createButtonPanel();
		createButton();
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void createRulePanel()
	{
		rulePanel = new JPanel();
		contentPanel.add(rulePanel);
		JLabel ruleDescLabel = new JLabel("Regel:");
		ruleDescLabel.setHorizontalAlignment(SwingConstants.LEFT);
		rulePanel.add(ruleDescLabel);
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setColumns(35);
		rulePanel.add(textField);
	}
	
	public void createHeaderLabel()
	{
		String header = engine.getPlayerList().get(playerID).getName()+" bestimme eine neue Regel.";
		headerLabel = new JLabel(header);
		headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(headerLabel);
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
		okButton = new JButton("Bestätigen");
		okButton.setActionCommand("OK");
		JDialog ref = this;
		okButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(!textField.getText().trim().equals("") && !textField.getText().equals(null))
				{
					String rule = textField.getText();
					updateRule(rule);
					ref.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(ref,
						    "Bitte gib eine Regel ein.",
						    "Keine Regel",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonPanel.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}
	
	public void updateRule(String rule)
	{
		engine.setRule(rule);
		int[] roundRules = engine.getRoundRules();
		for(int i = 0; i < roundRules.length; i ++)
		{
			roundRules[i] = 0;
		}
		engine.setRoundRules(roundRules);
	}
}