import java.awt.Image;
import java.util.ArrayList;

public class Engine implements GameModel
{
	private static int id = 0;
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
	
	public void scanWinLines()//TODO codierung zum hin und her senden
	{
		
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
