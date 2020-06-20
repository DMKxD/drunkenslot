package de.teamproject.drunkenslot.swingclient;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrunkenSlotGUI 
{
	private JFrame mainFrame;
	private JPanel contentPane;
	
	public DrunkenSlotGUI()
	{
		createMainFrame();
	}
	
	private void createMainFrame() 
	{
		mainFrame = new JFrame("Drunken Slot");
		contentPane = new JPanel();
		mainFrame.setContentPane(contentPane);
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
}
