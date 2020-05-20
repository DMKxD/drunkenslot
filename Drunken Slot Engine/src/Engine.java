import java.awt.Image;
import java.util.ArrayList;

public class Engine implements GameModel
{
	private static int id = 0;
	private int currentPlayerSybol = 8;//8 is player 0;
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private SlotMachine slotMachine;
	
	@Override
	public void createPlayer(int id, String name, Image image) 
	{
		playerList.add(new Player(id, name, image));
	}

	@Override
	public void createGame() 
	{
		
	}
	
	public static int getID()
	{
		return id++;
	}
	
	public void createSlotMachine()
	{
		ArrayList<Integer> symbols = new ArrayList<Integer>();
		
		for(int i = 0; i < (8 + playerList.size()); i++)
		{
			//0 = Shot Verteilen
			//1 = Schluck Verteilen
			//2 = Shot Trinken
			//3 = Schluck Trinken
			//4 = Regel
			//5 = Scatter
			//6 = Wild
			//7 = Wild für alle Spieler
			//8-n = Player 1-n
			symbols.add(i);
		}
		slotMachine = new SlotMachine(symbols);
	}
	
	public SlotImage roll()//TODO Schwierigkeiten
	{
		return slotMachine.generateRandom();
		
	}
	
	public void scanWinLines(SlotImage si)//TODO codierung zum hin und her senden
	{
		WinLine winline[] = new WinLine[9];
		winline[0] = checkWinLine1(si);
		winline[1] = checkWinLine2(si);
		winline[2] = checkWinLine3(si);
		winline[3] = checkWinLine4(si);
		winline[4] = checkWinLine5(si);
		winline[5] = checkWinLine6(si);
		winline[6] = checkWinLine7(si);
		winline[7] = checkWinLine8(si);
		winline[8] = checkWinLine9(si);
		for(int i = 0; i < winline.length; i ++)
		{
			System.out.print(winline[i].winLineText());
		}
	}
	
	public void testWinLine()//TODO Falls ein fehler auftritt hiermit den Fehler rekonstruieren und testen
	{
		WinLine line = new WinLine(currentPlayerSybol, 1);
		line.setSymbol(2);
		line.setSymbol(9);
		line.setSymbol(9);
		line.setSymbol(7);
		line.setSymbol(6);
		System.out.print(line.winLineText());
	}
	
	public WinLine checkWinLine1(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 1);
		int y = 0;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	public WinLine checkWinLine2(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 2);
		int y = 1;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	public WinLine checkWinLine3(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 3);
		int y = 2;
		for(int x = 0; x < si.getLengthX(); x ++)
		{
			line.setSymbol(si.get(x, y));
		}
		return line;
	}
	
	public WinLine checkWinLine4(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 4);
		line.setSymbol(si.get(0, 1));
		line.setSymbol(si.get(1, 0));
		line.setSymbol(si.get(2, 0));
		line.setSymbol(si.get(3, 0));
		line.setSymbol(si.get(4, 1));
		return line;
	}
	
	public WinLine checkWinLine5(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 5);
		line.setSymbol(si.get(0, 1));
		line.setSymbol(si.get(1, 2));
		line.setSymbol(si.get(2, 2));
		line.setSymbol(si.get(3, 2));
		line.setSymbol(si.get(4, 1));
		return line;
	}
	
	public WinLine checkWinLine6(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 6);
		line.setSymbol(si.get(0, 0));
		line.setSymbol(si.get(1, 1));
		line.setSymbol(si.get(2, 2));
		line.setSymbol(si.get(3, 1));
		line.setSymbol(si.get(4, 0));
		return line;
	}
	
	public WinLine checkWinLine7(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 7);
		line.setSymbol(si.get(0, 2));
		line.setSymbol(si.get(1, 1));
		line.setSymbol(si.get(2, 0));
		line.setSymbol(si.get(3, 1));
		line.setSymbol(si.get(4, 2));
		return line;
	}
	
	public WinLine checkWinLine8(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 8);
		line.setSymbol(si.get(0, 0));
		line.setSymbol(si.get(1, 0));
		line.setSymbol(si.get(2, 1));
		line.setSymbol(si.get(3, 2));
		line.setSymbol(si.get(4, 2));
		return line;
	}
	
	public WinLine checkWinLine9(SlotImage si)
	{
		WinLine line = new WinLine(currentPlayerSybol, 9);
		line.setSymbol(si.get(0, 2));
		line.setSymbol(si.get(1, 2));
		line.setSymbol(si.get(2, 1));
		line.setSymbol(si.get(3, 0));
		line.setSymbol(si.get(4, 0));
		return line;
	}
	
	public boolean isFreeGames(SlotImage si)
	{
		int scatter = 0;
		for(int y = 0; y < si.getLengthY(); y ++)
		{
			for(int x = 0; x < si.getLengthX(); x ++)
			{
				if(si.get(x, y) == 5)
				{
					scatter ++;
				}
			}
		}
		if(scatter == 3)
		{
			return true;
		}
		return false;
	}
	
	public void printSlot(SlotImage si)
	{
		System.out.println("\n");
		for(int y = 0; y < si.getLengthY(); y ++)
		{
			String line = "[";
			for(int x = 0; x < si.getLengthX(); x ++)
			{
				if(x < (si.getLengthX() - 1))
				{
					line += si.get(x, y)+" ";
				}
				else
				{
					line += si.get(x, y);
				}
			}
			line +="]";
			System.out.println(line);
		}
	}
}
