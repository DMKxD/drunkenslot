
public class DSEngineMain 
{

	public static void main(String[] args) 
	{
		Engine engine = new Engine();
		engine.createPlayer(engine.getID(), "Dominik", null);
		engine.createPlayer(engine.getID(), "Jonas", null);
		engine.createSlotMachine();
		engine.printSlot(engine.roll());
	}

}
