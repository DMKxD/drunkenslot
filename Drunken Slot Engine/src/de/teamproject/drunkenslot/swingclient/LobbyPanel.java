package de.teamproject.drunkenslot.swingclient;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * 
 * @author Dominik Haacke
 *
 */
public class LobbyPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel readyLabel;
	private JRadioButton readyButton;
	private JButton removeButton;
	
	public LobbyPanel(JPanel parent)
	{
		nameLabel = new JLabel("Name:");
		add(nameLabel);
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(200, 24));
		add(nameField);
		readyLabel = new JLabel("Bereit");
		add(readyLabel);
		readyButton = new JRadioButton();
		add(readyButton);
		removeButton = new JButton("X");//TODO red x icon
		JPanel ref = this;
		removeButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				parent.remove(ref);
			}
		});
		add(removeButton);
	}
	
	public String getName()
	{
		return nameField.getText();
	}
	
	public boolean isReady()
	{
		return readyButton.isSelected();
	}
	
}
