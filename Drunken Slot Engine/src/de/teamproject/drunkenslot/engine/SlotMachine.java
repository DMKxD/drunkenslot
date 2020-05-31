package de.teamproject.drunkenslot.engine;

import java.util.ArrayList;
import java.util.Random;

public class SlotMachine 
{
	private ArrayList<Integer> slotSymbols;
	
	public SlotMachine(ArrayList<Integer> slotSymbols)
	{
		this.slotSymbols = slotSymbols;
	}
	
	public SlotImage generateRandom()
	{
		int slotValues[][] = new int[5][3];
		Random randomizer = new Random();
		for(int i = 0; i < slotValues.length; i ++)
		{
			for (int j = 0; j < slotValues[0].length; j++)
			{
				slotValues[i][j] = slotSymbols.get(randomizer.nextInt((slotSymbols.size())));
			}
		}
		return new SlotImage(slotValues);
	}
}
