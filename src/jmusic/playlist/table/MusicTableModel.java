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
package jmusic.playlist.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import jmusic.playlist.Metadata;
import jmusic.playlist.MetadataLoader;

/**
 * This holds our play list data
 */
public class MusicTableModel extends AbstractTableModel  {
	private ArrayList<Row> dataRows = new ArrayList<Row>();
	
	@Override
	public int getRowCount() {
		return dataRows.size();
	}

	@Override
	public int getColumnCount() {
		return Row.getColumnCount();
	}

	@Override
	public String getColumnName(int column) {
		return Row.getColumnName(column);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return dataRows.get(rowIndex).get(columnIndex);
	}

	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return Row.getColumnNames();
	}

	/**
	 * Add a row to the playlist
	 * 
	 * @param row 
	 */
	public void addRow(Row row){
		dataRows.add(row);
	}
	
	/**
	 * Add a list of rows to the playlist
	 * 
	 * @param rows 
	 */
	public void addRows(List<Row> rows){
		dataRows.addAll(rows);
	}
	
	/**
	 * Get the specified row from the playlist
	 * 
	 * @param index the row wanted
	 * @return the row object
	 */
	public Row getRow(int index){
		return dataRows.get(index);
	}
	
	/**
	 * Removes all rows of the playlist
	 */
	public void clear(){
		dataRows.clear();
		fireTableDataChanged();
	}
	
	/**
	 * Load the metadata for all the files in our list
	 */
	public void loadMetadata(){
		Thread loader = new Thread(new Runnable(){
			static final int updateVal = 19;
			
			@Override
			public void run() {
				MetadataLoader loader = MetadataLoader.getInstance();

				int lastFired = 0;
				for (int i=0; i<dataRows.size(); i++){
					Row row = dataRows.get(i);
					Metadata md = loader.loadMetadata(row.getFile());
					row.setData(md);
					if (i % (updateVal + 1) == updateVal){
						//System.out.println("Firing update");
						fireTableRowsUpdated(i - updateVal, i);
						lastFired = i;
					}
				}
				// Update for the last lot since we last fired an update of the table model
				if (lastFired < dataRows.size()){
					fireTableRowsUpdated(lastFired+1,dataRows.size()-1);
				}
				//System.out.println("Updated!!");
			}
		});
		loader.start();
	}
}
