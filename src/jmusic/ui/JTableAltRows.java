/*
 * Copyright (C) 2012 Chris Hallson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmusic.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Modifies the JTable object to have alternating row colours which are offset.
 * 
 * For tables with a background which is brighter than 128 the alt is -10 off
 * the background. For tables with a background darker than 128 the alt is +10
 * off the background.
 * 
 * @author DrLabman
 */
public class JTableAltRows extends JTable {
	private java.awt.Color altColour = java.awt.Color.WHITE;
	
	public JTableAltRows(){
		super();
		setAltColour(getBackground());
	}
	
	/**
	 * Work out an offset colour based on the given colour.
	 * 
	 * @param colour The colour to offset from.
	 */
	private void setAltColour(Color colour){
		int r = colour.getRed() > 128 ? colour.getRed() - 10 : colour.getRed() + 10;
		int g = colour.getGreen() > 128 ? colour.getGreen() - 10 : colour.getGreen() + 10;
		int b = colour.getBlue() > 128 ? colour.getBlue() - 10 : colour.getBlue() + 10;
		
		altColour = new java.awt.Color(r,b,g);		
	}
	
	@Override
	public void setBackground(Color bg){
		super.setBackground(bg);
		setAltColour(bg);
	}
	
	@Override
	public Component prepareRenderer (TableCellRenderer renderer,int Index_row, int Index_col) {
		java.awt.Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
		//even index, selected or not selected
		if (Index_row % 2 == 0 && !isCellSelected(Index_row, Index_col)) {
			comp.setBackground(altColour);
		} else if (!isCellSelected(Index_row, Index_col)){
			comp.setBackground(this.getBackground());
		}
		return comp;
	}
}
