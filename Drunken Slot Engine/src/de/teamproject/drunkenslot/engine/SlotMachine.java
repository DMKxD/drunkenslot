package de.teamproject.drunkenslot.engine;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * SlotMachine class that can generate a SlotImage for the game.
 * @author Dominik Haacke
 *
 */
public class SlotMachine 
{
	private ArrayList<Integer> slotSymbols;
	private int symbolOffset  = 8;
	
	/**
	 * Create a SlotMachine with the given symbols of integers
	 * @param slotSymbols ArrayList<Integer> of the slot Symbols
	 */
	public SlotMachine(ArrayList<Integer> slotSymbols, int symbolOffset)
	{
		this.symbolOffset = symbolOffset;
		this.slotSymbols = slotSymbols;
	}
	
	public SlotImage generateRandom()
	{
		int slotValues[][] = new int[5][3];
		for(int i = 0; i < slotValues.length; i ++)
		{
			for (int j = 0; j < slotValues[0].length; j++)
			{
				slotValues[i][j] = slotSymbols.get(ThreadLocalRandom.current().nextInt(0, slotSymbols.size()));
			}
		}
		return new SlotImage(slotValues);
	}
	
	public void setSymbolOffset(int symbolOffset)
	{
		this.symbolOffset = symbolOffset;
	}
	
	public int getSymbolOffset()
	{
		return symbolOffset;
	}
}
