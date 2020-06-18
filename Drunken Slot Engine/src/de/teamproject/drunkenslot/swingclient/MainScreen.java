package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.GridLayout;

public class MainScreen extends JFrame 
{
	
	private JPanel contentPane;
	private JLabel mainImageLabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run() {
				try {
					MainScreen frame = new MainScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainScreen() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton buttonOnline = new JButton("Online");
		buttonOnline.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(buttonOnline);
		
		Component rigidButtonArea = Box.createRigidArea(new Dimension(20, 20));
		panel.add(rigidButtonArea);
		
		JButton buttonOffline = new JButton("Offline");
		buttonOffline.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(buttonOffline);
		contentPane.add(panel, BorderLayout.CENTER);
		
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
	}

}
