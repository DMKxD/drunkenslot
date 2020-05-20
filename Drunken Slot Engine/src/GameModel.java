import java.awt.Image;

public interface GameModel 
{
	public abstract void createPlayer(int id, String name, Image image);
	public abstract void createGame();
}