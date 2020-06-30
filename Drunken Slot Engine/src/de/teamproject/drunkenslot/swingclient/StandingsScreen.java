package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import de.teamproject.drunkenslot.engine.Engine;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Box;
import java.awt.Dimension;

/**
 * Standings Screen, shows which player is aktive and how many drinks and shots every player had
 * @author Dominik Haacke
 *
 */
public class StandingsScreen extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTable table;
	private JLabel mainImageLabel;
	private JPanel southPanel;
	private JPanel rulePanel;
	private JPanel buttonPanel;
	private JPanel topPanel;
	private JLabel ruleDescLabel;
	private JLabel ruleLabel;
	private JButton continueButton;
	
	private Engine engine;
	private DrunkenSlotGUI drunkenSlotGUI;
	private Component rigidArea;
	/**
	 * Create the frame.
	 */
	public StandingsScreen(DrunkenSlotGUI drunkenSlotGUI) 
	{
		this.drunkenSlotGUI = drunkenSlotGUI;
		engine = drunkenSlotGUI.getEngine();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 750);
		createContentPane();
		setContentPane(contentPane);
		try 
		{
			loadImage();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		createTopPanel();
		
		createTable();
		
		createSouthPanel();
	}
	
	public void createTopPanel()
	{
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(mainImageLabel);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		topPanel.add(rigidArea);
	}
	
	public void updateTable()
	{
		String data[][] = new String[engine.getPlayerList().size()][5];
		String columnNames[] = new String[] {
				"Spieler", "Symbol", "Schl�cke", "Shots", "Aktiv"
			};
		for(int i = 0; i < engine.getPlayerList().size(); i ++)
		{
			data[i][0] = engine.getPlayerList().get(i).getName();
			data[i][1] = "Player "+(i+1);
			data[i][2] = engine.getPlayerList().get(i).getDrinks()+"";
			data[i][3] = engine.getPlayerList().get(i).getShots()+"";
			data[i][4] = engine.getPlayerList().get(i).isActive() ? "Ja" : "Nein";
		}
		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
		table.setModel(tableModel);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
		for(int i = 0; i < tableModel.getColumnCount(); i++)
	    {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	    }
	}

	public void createTable() 
	{
		table = new JTable();
		table.setBorder(new EmptyBorder(5, 5, 5, 5));
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setShowGrid(true);
		table.setFont(new Font(null, Font.BOLD, 15));
		JTableHeader header = table.getTableHeader();
		//DSTableHeaderRenderer renderer = new DSTableHeaderRenderer(table);
		header.setDefaultRenderer(new DSTableHeaderRenderer(table));
		table.setModel(new DefaultTableModel
				(
			new Object[][] 
					{
				{null, null, null},
			},
			new String[] {
				"Spieler", "Symbol", "Schl�cke", "Shots", "Aktiv"
			}
		)
			{
			/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	public void createContentPane()
	{
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
	}
	
	public void createSouthPanel()
	{
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
		
		rulePanel = new JPanel();
		
		ruleDescLabel = new JLabel("Regel:");
		ruleLabel = new JLabel();
		
		rulePanel.add(ruleDescLabel);
		rulePanel.add(ruleLabel);
		
		southPanel.add(rulePanel);
		
		buttonPanel = new JPanel();
		continueButton = new JButton("Weiter");
		continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		continueButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				drunkenSlotGUI.switchToGameScreen();
			}
		});
		
		buttonPanel.add(continueButton);
		
		southPanel.add(buttonPanel);
		
		contentPane.add(southPanel, BorderLayout.SOUTH);
	}
	
	public void loadImage() throws IOException
	{
		BufferedImage wPic = ImageIO.read(this.getClass().getResource("/DSLogoLarge.png"));
		mainImageLabel = new JLabel(new ImageIcon(wPic));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	public void updateEngine()
	{
		this.engine = drunkenSlotGUI.getEngine();
	}
	
	public JPanel getScreen()
	{
		return contentPane;
	}
}
