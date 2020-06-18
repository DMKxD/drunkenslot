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

public class StandingsScreen extends JFrame {

	private JPanel contentPane;
	private JLabel mainImageLabel;

	/**
	 * Create the frame.
	 */
	public StandingsScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
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
	}
	
	public void loadImage() throws IOException
	{
		BufferedImage wPic = ImageIO.read(this.getClass().getResource("/TitleImagePlaceholder.png"));
		mainImageLabel = new JLabel(new ImageIcon(wPic));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

}
