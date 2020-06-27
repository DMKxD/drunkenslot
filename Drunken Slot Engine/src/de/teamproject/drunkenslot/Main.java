package de.teamproject.drunkenslot;

import java.awt.EventQueue;

import javax.swing.UIManager;

import de.teamproject.drunkenslot.cmdclient.DSCmdClient;
import de.teamproject.drunkenslot.swingclient.DrunkenSlotGUI;

public class Main 
{
	public static void main(String[] args) 
	{
		String args0 = (String)args[0];
		if(args0.equals("-cmd"))
		{
			DSCmdClient cmdClient = new DSCmdClient();
		}
		else if(args0.equals("-swing"))
		{
			EventQueue.invokeLater(new Runnable() 
			{
				public void run() 
				{
					try 
					{
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						DrunkenSlotGUI gui = new DrunkenSlotGUI();
						gui.getMainFrame().setVisible(true);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
			});
		}
	}
}
