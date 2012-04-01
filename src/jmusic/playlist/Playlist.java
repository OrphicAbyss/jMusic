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
package jmusic.playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import jmusic.config.DataStorage;
import jmusic.jMusicController;
import jmusic.playlist.table.MusicTableModel;
import jmusic.playlist.table.Row;
import jmusic.util.FileCompareName;
import jmusic.util.FileFinder;
import jmusic.util.MusicFileFilter;

/**
 * Holds the playlist data
 * 
 * @author DrLabman
 */
public class Playlist extends AbstractTableModel {
	private ArrayList<Row> dataRows = new ArrayList<Row>();
	private MusicTableModel model = null;
	
	private Playlist() {
		model = new MusicTableModel();
	}
	
	public static Playlist getInstance() {
		return PlaylistHolder.INSTANCE;
	}
	
	private static class PlaylistHolder {
		private static final Playlist INSTANCE = new Playlist();
	}
	
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
	 * Get the specified row from the playlist
	 * 
	 * @param index the row wanted
	 * @return the row object
	 */
	public Row getRow(int index){
		return dataRows.get(index);
	}
		
	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return Row.getColumnNames();
	}
	
	/**
	 * Removes all rows of the playlist
	 */
	public void clear(){
		dataRows.clear();
		fireTableDataChanged();
	}

	/**
	 * Get the playlist table model
	 * @return 
	 */
	public AbstractTableModel getModel(){
		return this;
	}
	
	/**
	 * Finds all files (recursive) under the given directory which are music
	 * files and creates a list of them.
	 * 
	 * Specifically looks for .mp3 files.
	 * 
	 * @param dir The top level directory of the music library
	 */
	public void loadPlaylist(File dir){
		// Ensure that it is a valid folder
		if (!dir.exists()){
			JOptionPane.showMessageDialog(jMusicController.getGUI(), "Music folder does not exist. Please change the music folder.");
			return;
		}
		// Update the settings file
		savePlaylistData(dir);
		// Get the full list of files
		List<File> files = FileFinder.findAllFiles(dir,new FileCompareName(),new MusicFileFilter());
		// Conver to a list of rows
		List<Row> rows = convertFileArrayToRowArray(files);
		// Fill playlist with filenames
		dataRows.addAll(rows);
		// Fire that we have set new data in the model
		fireTableDataChanged();
		// Load actual metadata in background
		loadMetadata();
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
	
	/**
	 * Save the new music folder to our settings file
	 */
	private void savePlaylistData(File dir){
		// Save this folder as the new music folder
		DataStorage ds = jMusicController.getDataStorage();
		ds.set("MusicFolder", dir.getPath());
		ds.save(new File(jMusicController.getDataStorage().getDefaultFilename()));
	}
	
	/**
	 * Converts an ArrayList of files to an array of Rows
	 * 
	 * @param files A list of File objects
	 * @return A list of of Row objects
	 */
	private List<Row> convertFileArrayToRowArray(List<File> files){
		ArrayList<Row> rows = new ArrayList<Row>(files.size());
		for (File file: files){
			rows.add(new Row(file));
		}
		return rows;
	}
}
