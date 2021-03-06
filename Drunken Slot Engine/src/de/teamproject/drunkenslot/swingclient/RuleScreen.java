package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Rule Screen shows and explains all rules
 * @author Dominik Haacke
 *
 */
public class RuleScreen
{
	private JPanel contentPane;
	private JLabel mainImageLabel;
	private JPanel buttonPanel;
	private DrunkenSlotGUI drunkenSlotGUI;
	private BufferedImage[] slotImages;
	private BufferedImage[] lineImages;
	private JPanel rulePanel, ruleInfoPanel, ruleDifficultyPanel, ruleLinePanel, ruleSymbolPanel, ruleFreePanel, ruleLoggingPanel, ruleSurrenderPanel, rulePlayerPanel;
	/**
	 * Create the frame.
	 */
	public RuleScreen(DrunkenSlotGUI drunkenSlotGUI) 
	{
		this.drunkenSlotGUI = drunkenSlotGUI;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		
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
		TitledBorder ruleInfoBorder = BorderFactory.createTitledBorder("Generelle Informationen");
		ruleInfoBorder.setTitleFont(new Font(ruleInfoBorder.getTitleFont().getName(), Font.BOLD, 15));
		ruleInfoPanel.setBorder(ruleInfoBorder);
		JLabel infoText = new JLabel("<html>DrunkenSlot verbindet ein Slotmachine mit einem Trinkspiel.<br>" + 
				"Pro Runde ist jeder Spieler einmal dran, zu seinem Zug kann<br>" + 
				"ein Spieler aufgeben und spielt ab da nicht mehr mit.<br>" + 
				"Nachdem man gedreht hat wird ein zuf�lliges SlotImage generiert,<br>" + 
				"auf f�nf Walzen mit jeweils drei Pl�tzen werden also insgesammt<br>" + 
				"15 verschiedene Symbole angezeigt.<br>" + 
				"Eine Gewinnlinie ist eine Kombination aus Symbolen die zusammen<br>" + 
				"einen Gewinn ergeben. Das sind entweder drei gleiche Symbole, eine<br>" + 
				"Kombination auch gleichen Symbolen mit Wild Symbolen und einem Player<br>" + 
				"oder AllPlayer Symbol.</html>");
		infoText.setFont(new Font(infoText.getFont().getName(), Font.BOLD, 15)); 
		ruleInfoPanel.add(infoText);
		
		rulePanel.add(ruleInfoPanel);
		
		ruleDifficultyPanel = new JPanel();
		TitledBorder ruleDifficultyBorder = BorderFactory.createTitledBorder("Schwierigkeitsgrade");
		ruleDifficultyBorder.setTitleFont(new Font(ruleDifficultyBorder.getTitleFont().getName(), Font.BOLD, 15));
		ruleDifficultyPanel.setBorder(ruleDifficultyBorder);
		JLabel ruleDifficultyText = new JLabel("<html>Es gibt drei verschiedene Schwierigkeitsgrade.\r\n<br>" + 
				"1. Alkoholiker: Gewinnlinen z�hlen ab 3 Symbolen, 3 gleiche Symbole\r\n<br>" + 
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sind Symboleffekt x 1, 4 gleiche sind Symboleffekt x 2 \r\n<br>" + 
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;und 5 Symbole sind Symboleffekt x 3.\r\n<br>" + 
				"2. Kneipeng�nger: Gewinnlinen z�hlen ab 4 Symbolen, 4 gleiche sind Symboleffekt x 1 \r\n<br>" + 
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;und 5 Symbole sind Symboleffekt x 2.\r\n<br>" + 
				"3. Abendtrinker: Gewinnlinen z�hlen ab 5 Symbolen, 5 gleiche sind Symboleffekt x 1.</html>");
		ruleDifficultyText.setFont(new Font(ruleDifficultyText.getFont().getName(), Font.BOLD, 15));
		ruleDifficultyPanel.add(ruleDifficultyText);
		
		rulePanel.add(ruleDifficultyPanel);
		
		ruleLinePanel = new JPanel();
		ruleLinePanel.setLayout(new BoxLayout(ruleLinePanel, BoxLayout.Y_AXIS));
		TitledBorder ruleLineBorder = BorderFactory.createTitledBorder("Gewinnlinien");
		ruleLineBorder.setTitleFont(new Font(ruleLineBorder.getTitleFont().getName(), Font.BOLD, 15));
		ruleLinePanel.setBorder(ruleLineBorder);
		for(int i = 0; i < 9; i ++)
		{
			JPanel linePanel = new JPanel();
			JLabel lineTextLabel = new JLabel("Line "+(i+1)+":");
			lineTextLabel.setFont(new Font(ruleDifficultyPanel.getFont().getName(), Font.BOLD, 15));
			linePanel.add(lineTextLabel);
			linePanel.add(new JLabel(new ImageIcon(lineImages[i])));
			ruleLinePanel.add(linePanel);
		}
		
		rulePanel.add(ruleLinePanel);
		
		ruleSymbolPanel = new JPanel();
		ruleSymbolPanel.setLayout(new BoxLayout(ruleSymbolPanel, BoxLayout.Y_AXIS));
		TitledBorder ruleSymbolBorder = BorderFactory.createTitledBorder("Symbole");
		ruleSymbolBorder.setTitleFont(new Font(ruleSymbolBorder.getTitleFont().getName(), Font.BOLD, 15));
		ruleSymbolPanel.setBorder(ruleSymbolBorder);
		JLabel ruleSymbolText = new JLabel("<html>Mit Drink ist ein Schluck von einem Bier oder\r\n<br>" + 
												"�hnlichem gemeint, ein Shot ist hier ein Kurzer.<html>");
		ruleSymbolText.setFont(new Font(ruleSymbolText.getFont().getName(), Font.BOLD, 15));
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
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 1:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Verteile Drink: Der Spieler verteilt die gegebene Anzahl an Drinks auf andere Spieler oder sich selbst.");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 2:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Trinke Shot: Der Spieler trinkt die gegebene Anzahl an Shots.");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 3:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Trinke Drink: Der Spieler Trinkt die gegebene Anzahl an Drinks.");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 4:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("<html>Regel: Nur bei einer 5 Symbol Regel Gewinnlinie darf eine neue Regel bestimmt werden.\r\n<br>" + 
													"			Eine Regel ist z.B. einem vor jedem Trinken etwas sagen oder tun. Eine Regel\r\n<br>" + 
													"			gilt so lange, bis eine neue Regel bestimmt wird.</html>");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 5:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Scatter: Ab 3 Scattern irgendwo im SlotImage werden Freispiele ausgel�st.");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 6:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("<html>Wild: Joker - wird in der Gewinnlinie als entsprechendes Symbol gez�hlt, dass zum\r\n<br>" + 
													"			Gewinn f�hrt.</html>");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 7:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("<html>Alle Spieler: Linie z�hlt f�r alle Spieler dieses Symbol z�hlt als Wild, ist aber nicht mit anderen \r\n<br>" + 
												"			Spieler Symbolen kombinierbar.</html>");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 8:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));
					symbolTextLabel = new JLabel("Niete: Ein Nieten Symbol bricht eine Gewinnliene und z�hlt als kein Gewinn.");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
					add = true;
					break;
				case 11:
					symbolLabel = new JLabel(new ImageIcon(slotImages[i]));//TODO durch blank player symbol ersetzen
					symbolTextLabel = new JLabel("<html>Spieler Symbole:�berschreibt die jeweilige Gewinnliene an den Spieler. Z�hlt als Wild, ist aber\r\n<br>" + 
													"			nicht mit anderen Spieler Symbolen oder dem Alle Spieler Symbol kombinierbar.</html>");
					symbolTextLabel.setFont(new Font(symbolTextLabel.getFont().getName(), Font.BOLD, 15));
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
		TitledBorder ruleFreeBorder = BorderFactory.createTitledBorder("Freispiele");
		ruleFreeBorder.setTitleFont(new Font(ruleFreeBorder.getTitleFont().getName(), Font.BOLD, 15));
		ruleFreePanel.setBorder(ruleFreeBorder);
		JLabel ruleFreeText = new JLabel("<html>Bei 3 oder mehr Scatter Symbolen werden Freispiele ausgel�st. Man gewinnt 5 Freispiele. Wenn w�hrend den Freispielen\r\n<br>" + 
				"erneut 3 oder mehr Scatter Symbole in einem SlotImage sind, erh�hen sich die Freispiele um 5. W�hrend den Freispielen\r\n<br>" + 
				"bleibt der gleiche Spieler am Zug. Alle Spieler Symbole werden durch Wild Symbole ersetzt.</html>");
		ruleFreeText.setFont(new Font(ruleFreeText.getFont().getName(), Font.BOLD, 15));
		ruleFreePanel.add(ruleFreeText);
		rulePanel.add(ruleFreePanel);
		
		ruleLoggingPanel = new JPanel();
		TitledBorder ruleLoggingBorder = BorderFactory.createTitledBorder("Logging");
		ruleLoggingBorder.setTitleFont(new Font(ruleLoggingBorder.getTitleFont().getName(), Font.BOLD, 15));
		ruleLoggingPanel.setBorder(ruleLoggingBorder);
		JLabel ruleLoggingText = new JLabel("<html>Ist logging aktiviert werden immer Dialoge zum verteilen der Shots und Drinks und eine Zwischenstandstabelle angezeigt.\r\n<br>" + 
				"Deaktiviert man es gibt es so zusagen ein schnelleres Spiel, wo jeder Spieler sofort nacheinander dran kommt ohne immer\r\n<br>" + 
				"Dialoge auszuf�llen und es wird auch kein Zwischenstand angezeigt.</html>");
		ruleLoggingText.setFont(new Font(ruleLoggingText.getFont().getName(), Font.BOLD, 15));
		ruleLoggingPanel.add(ruleLoggingText);
		rulePanel.add(ruleLoggingPanel);
		
		ruleSurrenderPanel = new JPanel();
		TitledBorder ruleSurrenderBorder = BorderFactory.createTitledBorder("Aufgeben");
		ruleSurrenderBorder.setTitleFont(new Font(ruleSurrenderBorder.getTitleFont().getName(), Font.BOLD, 15));
		ruleSurrenderPanel.setBorder(ruleSurrenderBorder);
		JLabel ruleSurrenderText = new JLabel("<html>Sobald ein Spieler aufgibt, wird er bei keinem Gewinn mehr mit einbezogen. Das Symbol des Spielers bleibt, ist aber\r\n<br>" + 
				"dann so zusagen auch ein Nieten Symbol. Ebenfalls kann er beim verteilen von Drinks oder Shots nicht mehr ausgew�hlt\r\n<br>" + 
				"werden. Geben alle Spieler bis auf einen auf, hat dieser gewonnen.</html>");
		ruleSurrenderText.setFont(new Font(ruleSurrenderText.getFont().getName(), Font.BOLD, 15));
		ruleSurrenderPanel.add(ruleSurrenderText);
		rulePanel.add(ruleSurrenderPanel);
		
		rulePlayerPanel = new JPanel();
		TitledBorder rulePlayerBorder = BorderFactory.createTitledBorder("Spieleranzahl");
		rulePlayerBorder.setTitleFont(new Font(rulePlayerBorder.getTitleFont().getName(), Font.BOLD, 15));
		rulePlayerPanel.setBorder(rulePlayerBorder);
		JLabel rulePlayerText = new JLabel("Es m�ssen mindestens zwei Spieler mitspielen. Das Maximum liegt bei 10 Spielern.");
		rulePlayerText.setFont(new Font(rulePlayerText.getFont().getName(), Font.BOLD, 15));
		rulePlayerPanel.add(rulePlayerText);
		rulePanel.add(rulePlayerPanel);
		
		
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
