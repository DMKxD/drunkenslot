package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.Box;
import java.awt.Dimension;

public class StandingsScreen extends JFrame {

	private JPanel contentPane;
	private JLabel mainImageLabel;
	private JTable table;

	/**
	 * Create the frame.
	 */
	public StandingsScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		try 
		{
			loadImage();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		contentPane.add(mainImageLabel, BorderLayout.NORTH);
		
		JPanel mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		mainPanel.add(rigidArea);
		
		JPanel freegamesPanel = new JPanel();
		freegamesPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		mainPanel.add(freegamesPanel);
		freegamesPanel.setLayout(new BoxLayout(freegamesPanel, BoxLayout.X_AXIS));
		
		table = new JTable();
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setShowGrid(false);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
			},
			new String[] {
				"Spieler", "Drinks", "Shots"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		mainPanel.add(table);
		
		JPanel rulePanel = new JPanel();
		rulePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(rulePanel);
		rulePanel.setLayout(new BoxLayout(rulePanel, BoxLayout.X_AXIS));
		
		JLabel ruleDescLabel = new JLabel("Rule:");
		ruleDescLabel.setHorizontalAlignment(SwingConstants.LEFT);
		ruleDescLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		rulePanel.add(ruleDescLabel);
		
		JLabel ruleLabel = new JLabel("");
		ruleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		rulePanel.add(ruleLabel);
		
		JPanel ButtonPanel = new JPanel();
		contentPane.add(ButtonPanel, BorderLayout.SOUTH);
	}
	
	public void loadImage() throws IOException
	{
		BufferedImage wPic = ImageIO.read(this.getClass().getResource("/TitleImagePlaceholder.png"));
		mainImageLabel = new JLabel(new ImageIcon(wPic));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

}
