package de.teamproject.drunkenslot.swingclient;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class DSTableHeaderRenderer implements TableCellRenderer 
{

	DefaultTableCellRenderer renderer;

	public DSTableHeaderRenderer(JTable table) 
	{
		renderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
	    renderer.setHorizontalAlignment(JLabel.CENTER);
		renderer.setFont(new Font(null, Font.BOLD, 15));
	}

	@Override
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) 
	{
		return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	}

}
