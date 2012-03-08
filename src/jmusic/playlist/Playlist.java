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
public class Playlist {
	MusicTableModel model = null;
	
	private Playlist() {
		model = new MusicTableModel();
	}
	
	public static Playlist getInstance() {
		return PlaylistHolder.INSTANCE;
	}
	
	private static class PlaylistHolder {
		private static final Playlist INSTANCE = new Playlist();
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
		saveNewMusicFolder(dir);
		// Get the full list of files
		List<File> files = FileFinder.findAllFiles(dir,new FileCompareName(),new MusicFileFilter());
		// Conver to a list of rows
		List<Row> rows = convertFileArrayToRowArray(files);
		// Fill playlist with filenames
		model.addRows(rows);
		// Fire that we have set new data in the model
		model.fireTableDataChanged();
		// Load actual metadata in background
		model.loadMetadata();
	}
	
	/**
	 * Get the playlist table model
	 * @return 
	 */
	public MusicTableModel getModel(){
		return model;
	}
	
	/**
	 * Save the new music folder to our settings file
	 */
	private void saveNewMusicFolder(File dir){
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
