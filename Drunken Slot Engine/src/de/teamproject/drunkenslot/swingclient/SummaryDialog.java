package de.teamproject.drunkenslot.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import de.teamproject.drunkenslot.engine.Engine;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class SummaryDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private DrunkenSlotGUI drunkenSlotGUI;
	private Engine engine;
	private JTable table;
	private JButton okButton;
	
	public SummaryDialog(DrunkenSlotGUI drunkenSlotGUI, Engine engine) 
	{
		super(drunkenSlotGUI.getMainFrame());
		this.drunkenSlotGUI = drunkenSlotGUI;
		this.engine = engine;
		setTitle("Rundenzusammenfassung");
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		//setPreferredSize(new Dimension(300, 250));
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		createButtonPanel();
		createButton();
		createTablePanel();
		pack();
		positionieren(this, 0, 0);
	}
	
	public void createButtonPanel()
	{
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		okButton = new JButton("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}
	
	public void createButton()
	{
		JDialog ref = this;
		okButton.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				drunkenSlotGUI.getGameScreen().setShownSummary(true);
				drunkenSlotGUI.getGameScreen().showDialogs();
				ref.dispose();
			}
		});
	}
	
	public void updateTable()
	{
		table = new JTable();
		int numOfVisibleRows = 0;
		for(int i = 0; i < engine.getPlayerList().size(); i ++)
		{
			if((engine.getRoundDrinks()[i]+engine.getRoundShots()[i]) > 0)
			{
				if(engine.getPlayerList().get(i).isActive())
				{
					numOfVisibleRows ++;
				}
			}
		}
		String data[][] = new String[numOfVisibleRows][3];
		String columnNames[] = new String[] {
				"Spieler", "Schlücke", "Shots"
			};
		int index = 0;
		for(int i = 0; i < engine.getPlayerList().size(); i ++)
		{
			if((engine.getRoundDrinks()[i]+engine.getRoundShots()[i]) > 0)
			{
				if(engine.getPlayerList().get(i).isActive())
				{
					data[index][0] = engine.getPlayerList().get(i).getName();
					data[index][1] = engine.getRoundDrinks()[i]+"";
					data[index][2] = engine.getRoundShots()[i]+"";
					index++;
				}
			}
		}
		table.setFont(new Font(null, Font.BOLD, 15));
		JTableHeader header = table.getTableHeader();
		//DSTableHeaderRenderer renderer = new DSTableHeaderRenderer(table);
		header.setDefaultRenderer(new DSTableHeaderRenderer(table));
		
		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
		table.setModel(tableModel);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
		for(int i = 0; i < tableModel.getColumnCount(); i++)
	    {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	    }
		int cols = table.getColumnModel().getTotalColumnWidth();
		int rows = table.getRowHeight() * numOfVisibleRows;
		Dimension d = new Dimension( cols, rows );
		table.setPreferredScrollableViewportSize( d );
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	}
	
	public void createTablePanel()
	{
		updateTable();
		JPanel outerTabelPanel = new JPanel();
		outerTabelPanel.add(new JScrollPane(table));
		getContentPane().add(outerTabelPanel, BorderLayout.CENTER);
	}
	
	public void positionieren(Component component, int x, int y)
    {
        double lXKoordinate = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2.0 - component.getWidth() / 2.0;
        double lYKoordinate = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2.0 - component.getHeight() / 2.0;
        component.setLocation((int)lXKoordinate + x, (int)lYKoordinate + y);
    }
}
