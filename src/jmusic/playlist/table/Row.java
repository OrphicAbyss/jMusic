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
import jmusic.Metadata;

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
	
	public static int getColumnCount(){
		return 4;
	}
	
	public static String getColumnName(int column){
		return Column.values()[column].getTitle();
	}
	
	public static String[] getColumnNames(){
		Column[] columns = Column.values();
		String[] columnNames = new String[columns.length];
		for (int i=0; i<columns.length; i++){
			columnNames[i] = columns[i].getTitle();
		}
		return columnNames;
	}
}
