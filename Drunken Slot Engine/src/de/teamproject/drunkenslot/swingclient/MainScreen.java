package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;

public class MainScreen
{
	private JPanel contentPane;
	private JLabel mainImageLabel;
	private JPanel buttonPanel;
	private JButton buttonOnline;
	private JButton buttonOffline;
	private JButton buttonRule;
	private JPanel bottomPanel;
	private JLabel bottomLabel;
	
	private DrunkenSlotGUI drunkenSlotGUI;

	/**
	 * Create the frame.
	 */
	public MainScreen(DrunkenSlotGUI drunkenSlotGUI) 
	{
		this.drunkenSlotGUI = drunkenSlotGUI;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		
		createButtonPanel();
		createButtons();
		createButtomPanel();
		
		try 
		{
			loadImage();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		contentPane.add(mainImageLabel, BorderLayout.NORTH);
	}
	
	public void createButtons()
	{
		buttonOnline.setEnabled(false);
		buttonOffline.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				drunkenSlotGUI.switchToLobbyScreen();
			}
		});
		buttonRule.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				drunkenSlotGUI.switchToRuleScreen();
			}
		});
	}
	
	public void createButtonPanel()
	{
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		Component rigidArea = Box.createRigidArea(new Dimension(0, 150));
		buttonPanel.add(rigidArea);
		
		buttonOnline = new JButton("Online");
		buttonOnline.setVerticalAlignment(SwingConstants.BOTTOM);
		buttonOnline.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel testPanel = new JPanel();
		//FlowLayout flowLayout = (FlowLayout) testPanel.getLayout();
		testPanel.add(buttonOnline);
		buttonPanel.add(testPanel);
		
		buttonOffline = new JButton("Offline");
		buttonOffline.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel testPanel2 = new JPanel();
		//FlowLayout flowLayout_1 = (FlowLayout) testPanel2.getLayout();
		testPanel2.add(buttonOffline);
		buttonPanel.add(testPanel2);
		
		buttonRule = new JButton("Regeln");
		buttonRule.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel testPanel3 = new JPanel();
		testPanel3.add(buttonRule);
		buttonPanel.add(testPanel3);
		
		JPanel buttonOuterPanel = new JPanel();
		buttonOuterPanel.add(buttonPanel);
		contentPane.add(buttonOuterPanel, BorderLayout.CENTER);
	}
	
	public void createButtomPanel()
	{
		bottomPanel = new JPanel();
		bottomLabel = new JLabel("DrunkenSlot Teamprojekt 2020 Dominik Haacke");
		bottomPanel.add(bottomLabel);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	public void loadImage() throws IOException
	{
		BufferedImage wPic = ImageIO.read(this.getClass().getResource("/TitleImagePlaceholder.png"));
		mainImageLabel = new JLabel(new ImageIcon(wPic));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	public JPanel getScreen()
	{
		return contentPane;
	}
}
