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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import jmusic.jMusicController;
import jmusic.playlist.Metadata;
import jmusic.util.FileCompareSize;
import jmusic.util.FileFinder;
import jmusic.util.ImageFileFilter;

/**
 * Represents a row in the playlist model.
 * 
 * @author DrLabman
 */
public class Row {
	public enum Column {
		ARTIST("Artist"), ALBUM("Album"), TRACK_NO("Track #"), TITLE("Title");
		
		String title;
		
		Column(String title){
			this.title = title;
		}
		
		public String getTitle(){
			return title;
		}
	}
	
	/** Music file this row represents */	
	private File file;
	private String artist;
	private String album;
	private String trackNo;
	private String title;
	
	public Row(File file){
		this.file = file;
		artist = file.getName();
	}
	
	public void play(){
		jMusicController.getMusicPlayer().play(file);
	}
	
	public File getFile(){
		return file;
	}
	
	public void setData(Metadata metadata){
		artist = metadata.getArtist();
		album = metadata.getAlbum();
		trackNo = metadata.getTrackNumber();
		title = metadata.getTitle();
	}
	
	public Object get(int columnVal){
		Column column = Column.values()[columnVal];
		switch (column){
			case ARTIST:
				return artist;
			case ALBUM:
				return album;
			case TRACK_NO:
				return trackNo;
			case TITLE:
				return title;
			default:
				return "Bad column number";
		}
	}

	/**
	 * Creates a 'html' markup string to have bold labels and normal values for
	 * UI components.
	 * 
	 * @param label The label text
	 * @param value The value text
	 * @return A string with html markup to bold the label
	 */
	private String boldLabel(String label, String value){
		return "<html><b>" + label + ":</b> " + value + "</html>";
	}
	
	/**
	 * Gets an array of strings listing metadata values.
	 * 
	 * @return string array containing readable metadata
	 */
	public String[] getMetadataArray(){
		String[] data = new String[]{boldLabel("Artist", artist), boldLabel("Album",album), boldLabel("Title",title)};
		return data;
	}
	
	public String getMetadataString(){
		return "[" + artist + " - " + album + " - " + title + "]";
	}
	
	/**
	 * Find the largest image file in the same or a sub-folder as the media file.
	 * 
	 * @return An image file or null if no files are found
	 */
	public File getImageFile(){
		String path = file.getParent();
		// Get all image files in the same folder (or sub-folder) as the media
		List<File> files = FileFinder.findAllFiles(new File(path),new FileCompareSize(),new ImageFileFilter());
		// DEBUG: Print image files found
		for (File imageFile: files){
			System.out.println("Image file: " + imageFile.getName());
		}
		// Find the largest image and use it (files are sorted by size)
		if (files.size() > 0){
			return files.get(files.size() - 1);
		}
		return null;
	}
	
	/**
	 * Get the number of columns
	 * 
	 * @return number of columns
	 */
	public static int getColumnCount(){
		return 4;
	}
	
	/**
	 * The column name for the given column index
	 * 
	 * @param column the column index
	 * @return the column name
	 */
	public static String getColumnName(int column){
		return Column.values()[column].getTitle();
	}
	
	/**
	 * Get an array of column names in index order
	 * 
	 * @return A string array of column names in index order
	 */
	public static String[] getColumnNames(){
		Column[] columns = Column.values();
		String[] columnNames = new String[columns.length];
		for (int i=0; i<columns.length; i++){
			columnNames[i] = columns[i].getTitle();
		}
		return columnNames;
	}
	
	/**
	 * Converts an ArrayList of files to an array of Rows
	 * 
	 * @param files A list of File objects
	 * @return A list of of Row objects
	 */
	public static List<Row> convertFileArrayToRowArray(List<File> files){
		ArrayList<Row> rows = new ArrayList<Row>(files.size());
		for (File file: files){
			rows.add(new Row(file));
		}
		return rows;
	}
}
