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

import java.awt.event.MouseAdapter;
import javax.swing.table.TableModel;
import jmusic.jMusicController;
import jmusic.playlist.Playlist;
import jmusic.playlist.table.Row;

/**
 * Handles functionality related to playlist operation.
 * 
 * @author drlabman
 */
public class JTablePlaylist extends JTableAltRows {
	
	public JTablePlaylist(){
		super();
		setModel(Playlist.getInstance().getModel());
		
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				rowClicked(evt);
			}
		});
	}
	
	/**
	 * Catch double click event to start a media track.
	 * 
	 * @param evt MouseEvent
	 */
	private void rowClicked(java.awt.event.MouseEvent evt) {                                  
		if (evt.getClickCount() == 2){
			//double click
			play();
		}
	}
	
	/**
	 * Override the getSelectedRow method to select the first row if nothing
	 * is currently selected.
	 * 
	 * @return the select row index
	 */
	public int getSelectedRowDefaulted(){
		int selected = getSelectedRow();
		if (selected == -1){
			addRowSelectionInterval(0, 0);
			selected = getSelectedRow();
		}
		return selected;
	}
	
	/**
	 * Play the currently selected song.
	 * 
	 * If no rows are selected, select the first song and play that.
	 */
	public void play(){
		int selected = getSelectedRowDefaulted();
		Row row = getModel().getRow(selected);
		row.play();
	}
	
	/**
	 * Play the previous song in the playlist.
	 * 
	 * If we are at the start of the playlist then stop.
	 */
	public void backward(){
		int selected = getSelectedRowDefaulted();
		int next = selected - 1;
		if (next < 0){
			// Stop, add wrapping of playlist once a loop setting has been added
			jMusicController.getMusicPlayer().stop();
		} else {
			this.removeRowSelectionInterval(selected, selected);
			this.addRowSelectionInterval(next, next);
			play();
		}
	}
	
	/**
	 * Play the next song in the playlist.
	 * 
	 * If we are at the end of the playlist then stop.
	 */
	public void forward(){
		boolean wasSelected = getSelectedRow() != -1;
		int selected = getSelectedRowDefaulted();
		int next = selected + 1;
		if (next >= getRowCount()){
			// Stop, add wrapping of playlist once a loop setting has been added
			jMusicController.getMusicPlayer().stop();
		} else {
			if (wasSelected){
				this.removeRowSelectionInterval(selected, selected);
				this.addRowSelectionInterval(next, next);
			}
			play();
		}
	}
	
	/**
	 * Ensure that only the MusicTableModel class is used for the model.
	 * 
	 * @param model A MusicTableModel object which holds the playlist data.
	 */
	@Override
	public void setModel(TableModel model){
		if (model instanceof Playlist)
			super.setModel(model);
//		else
//			throw new IllegalArgumentException("Only " + MusicTableModel.class.getName() + " objects are accepted.");
	}
	
	/**
	 * Get the MusicTableModel underlying the table data.
	 * 
	 * @return A MusicTableModel object which holds the playlist data.
	 */
	@Override
	public Playlist getModel(){
		return (Playlist)super.getModel();
	}
}
