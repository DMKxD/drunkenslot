public class SlotImage 
{
	private int values[][];
	
	public SlotImage(int values[][])
	{
		this.values = values;
	}
	
	public int get(int x, int y)
	{
		if(x < values.length)
		{
			if(y < values[0].length)
			{
				return values[x][y];
			}
			return -1;
		}
		return -1;
	}
}