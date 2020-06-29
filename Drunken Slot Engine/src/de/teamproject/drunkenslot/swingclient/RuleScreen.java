package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RuleScreen extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel mainImageLabel;
	private JPanel buttonPanel;
	private DrunkenSlotGUI drunkenSlotGUI;
	private BufferedImage[] slotImages;
	private BufferedImage[] lineImages;
	private JPanel rulePanel, ruleInfoPanel, ruleDifficultyPanel, ruleLinePanel, ruleSymbolPanel, ruleFreePanel;
	/**
	 * Create the frame.
	 */
	public RuleScreen(DrunkenSlotGUI drunkenSlotGUI) 
	{
		this.drunkenSlotGUI = drunkenSlotGUI;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
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
		
		createRulePanel();
		createSouthPanel();
		
	}
	
	public void createRulePanel()
	{
		rulePanel = new JPanel();
		rulePanel.setLayout(new BoxLayout(rulePanel, BoxLayout.Y_AXIS));
		
		ruleInfoPanel = new JPanel();
		ruleInfoPanel.setBorder(BorderFactory.createTitledBorder("Generelle Informationen"));
		JLabel infoText = new JLabel("<html>DrunkenSlot verbindet ein Slotmachine mit einem Trinkspiel.<br>" + 
				"Pro Runde ist jeder Spieler einmal dran, zu seinem Zug kann<br>" + 
				"ein Spieler aufgeben und spielt ab da nicht mehr mit.<br>" + 
				"Nachdem man gedreht hat wird ein zufälliges SlotImage generiert,<br>" + 
				"auf fünf Walzen mit jeweils drei Plätzen werden also insgesammt<br>" + 
				"15 verschiedene Symbole angezeigt.<br>" + 
				"Eine Gewinnlinie ist eine Kombination aus Symbolen die zusammen<br>" + 
				"einen Gewinn ergeben. Das sind entweder drei gleiche Symbole, eine<br>" + 
				"Kombination auch gleichen Symbolen mit Wild Symbolen und einem Player<br>" + 
				"oder AllPlayer Symbol.</html>");
		ruleInfoPanel.add(infoText);
		
		rulePanel.add(ruleInfoPanel);
		
		ruleDifficultyPanel = new JPanel();
		ruleDifficultyPanel.setBorder(BorderFactory.createTitledBorder("Schwierigkeitsgrade"));
		JLabel ruleDifficultyText = new JLabel("<html>Es gibt drei verschiedene Schwierigkeitsgrade.\r\n<br>" + 
				"1. Alkoholiker: Gewinnlinen zählen ab 3 Symbolen, 3 gleiche Symbole\r\n<br>" + 
				"&nbsp;&nbsp;&nbsp;&nbsp;sind Symboleffekt x 1, 4 gleiche sind Symboleffekt x 2 \r\n<br>" + 
				"&nbsp;&nbsp;&nbsp;&nbsp;und 5 Symbole sind Symboleffekt x 3.\r\n<br>" + 
				"2. Kneipengänger: Gewinnlinen zählen ab 4 Symbolen, 4 gleiche sind Symboleffekt x 1 \r\n<br>" + 
				"&nbsp;&nbsp;&nbsp;&nbsp;und 5 Symbole sind Symboleffekt x 2.\r\n<br>" + 
				"3. Abendtrinker: Gewinnlinen zählen ab 5 Symbolen, 5 gleiche sind Symboleffekt x 1.</html>");
		ruleDifficultyPanel.add(ruleDifficultyText);
		
		rulePanel.add(ruleDifficultyPanel);
		
		ruleLinePanel = new JPanel();
		ruleLinePanel.setLayout(new BoxLayout(ruleLinePanel, BoxLayout.Y_AXIS));
		ruleLinePanel.setBorder(BorderFactory.createTitledBorder("Gewinnlinien"));
		for(int i = 0; i < 9; i ++)
		{
			JPanel linePanel = new JPanel();
			linePanel.add(new JLabel("Line "+(i+1)+":"));
			linePanel.add(new JLabel(new ImageIcon(lineImages[i])));
			ruleLinePanel.add(linePanel);
		}
		
		rulePanel.add(ruleLinePanel);
		
		ruleSymbolPanel = new JPanel();
		ruleSymbolPanel.setLayout(new BoxLayout(ruleSymbolPanel, BoxLayout.Y_AXIS));
		ruleSymbolPanel.setBorder(BorderFactory.createTitledBorder("Symbole"));
		JLabel ruleSymbolText = new JLabel("<html>Mit Drink ist ein Schluck von einem Bier oder\r\n<br>" + 
												"ähnlichem gemeint, ein Shot ist hier ein Kurzer.<html>");
		ruleSymbolPanel.add(ruleSymbolText);
		for(int i = 0; i < 12; i ++)
		{
			JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel symbolLabel = null;
			JLabel symbolTextLabel = null;
			boolean add = false;
			switch(i)
			{
				case 0:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Verteile Shot: Der Spieler verteilt die gegebene Anzahl an Shots auf andere Spieler oder sich selbst.");
					add = true;
					break;
				case 1:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Verteile Drink: Der Spieler verteilt die gegebene Anzahl an Drinks auf andere Spieler oder sich selbst.");
					add = true;
					break;
				case 2:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Trinke Shot: Der Spieler trinkt die gegebene Anzahl an Shots.");
					add = true;
					break;
				case 3:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Trinke Drink: Der Spieler Trinkt die gegebene Anzahl an Drinks.");
					add = true;
					break;
				case 4:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("<html>Regel: Nur bei einer 5 Symbol Regel Gewinnlinie darf eine neue Regel bestimmt werden.\r\n<br>" + 
													"			Eine Regel ist z.B. einem vor jedem Trinken etwas sagen oder tun. Eine Regel\r\n<br>" + 
													"			gilt so lange, bis eine neue Regel bestimmt wird.</html>");
					add = true;
					break;
				case 5:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Scatter: Ab 3 Scattern irgendwo im SlotImage werden Freispiele ausgelöst.");
					add = true;
					break;
				case 6:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("<html>Wild: Joker - wird in der Gewinnlinie als entsprechendes Symbol gezählt, dass zum\r\n<br>" + 
													"			Gewinn führt.</html>");
					add = true;
					break;
				case 7:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("<html>Alle Spieler: Linie zählt für alle Spieler dieses Symbol zählt als Wild, ist aber nicht mit anderen \r\n<br>" + 
												"			Spieler Symbolen kombinierbar.</html>");
					add = true;
					break;
				case 8:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Niete: Ein Nieten Symbol bricht eine Gewinnliene und zählt als kein Gewinn.");
					add = true;
					break;
				case 11:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));//TODO durch blank player symbol ersetzen
					symbolTextLabel = new JLabel("<html>Spieler Symbole:Überschreibt die jeweilige Gewinnliene an den Spieler. Zählt als Wild, ist aber\r\n<br>" + 
													"			nicht mit anderen Spieler Symbolen oder dem Alle Spieler Symbol kombinierbar.</html>");
					add = true;
					break;
				
			}
			if(add)
			{
				symbolPanel.add(symbolLabel);
				symbolPanel.add(symbolTextLabel);
				
				ruleSymbolPanel.add(symbolPanel);
			}
		}
		
		rulePanel.add(ruleSymbolPanel);
		
		ruleFreePanel = new JPanel();
		ruleFreePanel.setBorder(BorderFactory.createTitledBorder("Freispiele"));
		
		JLabel ruleFreeText = new JLabel("<html>Bei 3 oder mehr Scatter Symbolen werden Freispiele ausgelöst. Man gewinnt 5 Freispiele. Wenn während den Freispielen\r\n<br>" + 
				"erneut 3 oder mehr Scatter Symbole in einem SlotImage sind, erhöhen sich die Freispiele um 5. Während den Freispielen\r\n<br>" + 
				"bleibt der gleiche Spieler am Zug. Alle Spieler Symbole werden durch Wild Symbole ersetzt.</html>");
		ruleFreePanel.add(ruleFreeText);
		rulePanel.add(ruleFreePanel);
		
		JScrollPane scrollBar = new JScrollPane(rulePanel,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollBar, BorderLayout.CENTER);
	}
	
	public void createSouthPanel()
	{
		buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		JButton mainMenuButton = new JButton("Hauptmen\u00FC");
		mainMenuButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				drunkenSlotGUI.switchToMainScreen();
			}
		});
		buttonPanel.add(mainMenuButton);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void loadImage() throws IOException
	{
		BufferedImage wPic = ImageIO.read(this.getClass().getResource("/DSLogoV1.png"));
		mainImageLabel = new JLabel(new ImageIcon(wPic));
		mainImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		slotImages = new BufferedImage[21];
		for(int i = 0; i < 21; i ++)
		{
			slotImages[i] = ImageIO.read(this.getClass().getResource("/slotImages/slotSymbol"+i+".png"));
		}

		lineImages = new BufferedImage[9];
		for(int i = 1; i < 10; i ++)
		{
			lineImages[i-1] = ImageIO.read(this.getClass().getResource("/lineImages/Line"+i+".png"));
		}
	}
	
	public JPanel getScreen()
	{
		return contentPane;
	}
}
