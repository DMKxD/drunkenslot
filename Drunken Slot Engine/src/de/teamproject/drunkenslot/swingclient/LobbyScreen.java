package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.teamproject.drunkenslot.engine.Engine;
import de.teamproject.drunkenslot.engine.GameConfig;

import java.awt.FlowLayout;

public class LobbyScreen extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel playerPanel;
	private JPanel buttonPanel;
	private JButton addPlayerButton;
	private JButton backButton;
	private JButton startButton;
	private JLabel mainImageLabel;
	
	private DrunkenSlotGUI drunkenSlotGUI;
	/**
	 * Create the frame.
	 */
	public LobbyScreen(DrunkenSlotGUI drunkenSlotGUI) 
	{
		this.drunkenSlotGUI = drunkenSlotGUI;
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
		createPlayerPanel();
		createButtonpanel();
		createButtons();
	}
	
	public void createPlayerPanel()
	{
		playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
		playerPanel.add(new LobbyPanel(playerPanel));
		
		playerPanel.addContainerListener(new ContainerListener() {
			
			@Override
			public void componentRemoved(ContainerEvent e) 
			{
				playerPanel.revalidate();
				playerPanel.repaint();
			}
			
			@Override
			public void componentAdded(ContainerEvent e) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		addPlayerButton = new JButton("+");
		addPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addPlayerButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(playerPanel.getComponentCount() < 11)
				{
					playerPanel.remove(addPlayerButton);
					playerPanel.add(new LobbyPanel(playerPanel));
					playerPanel.add(addPlayerButton);
					playerPanel.validate();
					playerPanel.repaint();
				}
				else
				{
					JOptionPane.showMessageDialog(drunkenSlotGUI.getMainFrame(),
						    "Das Spielerlimit liegt bei 10 Spielern.",
						    "Spielerlimit erreicht",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		playerPanel.add(addPlayerButton);
		contentPane.add(playerPanel, BorderLayout.CENTER);
	}
	
	public void createButtonpanel()
	{
		buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		backButton = new JButton("Zurück");
		startButton = new JButton("Start");
		buttonPanel.add(startButton);
		buttonPanel.add(backButton);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void createButtons()
	{
		startButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				int count = 0;
				boolean broken = false;
				for(int i = 0; i < playerPanel.getComponentCount(); i ++)
				{
					if(playerPanel.getComponent(i) instanceof LobbyPanel)
					{
						LobbyPanel lobbyPanel = (LobbyPanel)playerPanel.getComponent(i);
						if(lobbyPanel.getName().equals(null) || lobbyPanel.getName().trim().equals(""))
						{
							broken = true;
							JOptionPane.showMessageDialog(drunkenSlotGUI.getMainFrame(),
								    "Bitte gib einen Namen in Feld "+i+" ein.",
								    "Eingabe Fehler",
								    JOptionPane.ERROR_MESSAGE);
							break;
						}
						if(!lobbyPanel.isReady())
						{
							broken = true;
							JOptionPane.showMessageDialog(drunkenSlotGUI.getMainFrame(),
								    "Spieler "+ lobbyPanel.getName()+" ist noch nicht bereit.",
								    "Nicht bereit",
								    JOptionPane.ERROR_MESSAGE);
							break;
						}
						count ++;
					}
				}
				
				if(count <= 10 && !broken && count >= 2)
				{
					GameConfig config = new GameConfig();
					for(int i = 0; i < playerPanel.getComponentCount(); i ++)
					{
						if(playerPanel.getComponent(i) instanceof LobbyPanel)
						{
							LobbyPanel lobbyPanel = (LobbyPanel)playerPanel.getComponent(i);
							config.createPlayer(i, lobbyPanel.getName(), null);//TODO image selection or print name on image
						}
					}
					drunkenSlotGUI.setEngine(new Engine(config));
					drunkenSlotGUI.getEngine().createGame();
					drunkenSlotGUI.switchToStandingsScreen();
					
				}
				else if(count <= 2 && !broken)
				{
					JOptionPane.showMessageDialog(drunkenSlotGUI.getMainFrame(),
						    "Mindestens zwei Spieler müssen pro Runde mitspielen.",
						    "Zu wenig Spieler",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		backButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				drunkenSlotGUI.switchToMainScreen();
			}
		});
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
