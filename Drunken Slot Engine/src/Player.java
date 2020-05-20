import java.awt.Image;

public class Player 
{
	private int id;
	private String name;
	private Image image;
	private boolean active = true;
	private int shots = 0;
	private int schluecke = 0;
	
	public Player(int id, String name, Image image)
	{
		this.id = id;
		this.name = name;
		this.image = image;
	}

	public int getId() 
	{
		return id;
	}

	public String getName() 
	{
		return name;
	}

	public Image getImage() 
	{
		return image;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public String getSymbol()
	{
		return "P"+id;
	}

	public int getShots() 
	{
		return shots;
	}

	public void setShots(int shots) 
	{
		this.shots = shots;
	}

	public int getSchluecke() 
	{
		return schluecke;
	}

	public void setSchluecke(int schluecke) 
	{
		this.schluecke = schluecke;
	}
}
