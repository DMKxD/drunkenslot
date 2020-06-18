package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;

public class GameScreen extends JFrame {

	private JPanel contentPane;
	private JTable slotTable;
	private JLabel mainImageLabel;
	
	/**
	 * Create the frame.
	 */
	public GameScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel mainImagePanel = new JPanel();
		contentPane.add(mainImagePanel, BorderLayout.SOUTH);
		mainImagePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel tablePanel = new JPanel();
		contentPane.add(tablePanel, BorderLayout.CENTER);
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		
		slotTable = new JTable();
		slotTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		slotTable.setRowSelectionAllowed(false);
		slotTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		slotTable.getColumnModel().getColumn(0).setResizable(false);
		slotTable.getColumnModel().getColumn(0).setMaxWidth(80);
		slotTable.getColumnModel().getColumn(1).setMaxWidth(80);
		slotTable.getColumnModel().getColumn(2).setMaxWidth(80);
		slotTable.getColumnModel().getColumn(3).setMaxWidth(80);
		slotTable.getColumnModel().getColumn(4).setMaxWidth(80);
		tablePanel.add(slotTable);
		
		JTextArea winLineTextArea = new JTextArea();
		tablePanel.add(winLineTextArea);
		
		JPanel buttonPanel = new JPanel();
		FlowLayout fl_buttonPanel = (FlowLayout) buttonPanel.getLayout();
		fl_buttonPanel.setAlignment(FlowLayout.RIGHT);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		JButton buttonSpin = new JButton("Spin");
		buttonSpin.setHorizontalAlignment(SwingConstants.RIGHT);
		buttonPanel.add(buttonSpin);
		
		JButton buttonSurrender = new JButton("Surrender");
		buttonPanel.add(buttonSurrender);
		
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
	
	public void loadImage() throws IOException
	{
		BufferedImage wPic = ImageIO.read(this.getClass().getResource("/TitleImagePlaceholder.png"));
		mainImageLabel = new JLabel(new ImageIcon(wPic));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

}
