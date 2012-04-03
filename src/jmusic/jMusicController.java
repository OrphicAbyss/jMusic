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
package jmusic;

import jmusic.config.DataStorage;
import jmusic.config.DataStorageProperties;
import jmusic.playback.MusicPlayer;
import jmusic.playback.MusicPlayerGStreamer;
import jmusic.playlist.Playlist;
import jmusic.ui.JDialogAdvanced;
import jmusic.ui.jMusic;
import org.gstreamer.Gst;

/**
 * Main class which coordinates the actions of the media player.
 * 
 * @author DrLabman
 */
public class jMusicController {
	public static final boolean DEBUG = false;
	public static final String PLAYER_NAME = "jMusic";
	
	static MusicPlayer musicPlayer = null;
	static Playlist playlist = null;
	static DataStorage dataStorage = null;
	static jMusic gui = null;
	
	public static MusicPlayer getMusicPlayer(){
		return musicPlayer;
	}
	
	public static Playlist getPlaylist(){
		return playlist;
	}
	
	public static DataStorage getDataStorage(){
		return dataStorage;
	}
	
	public static jMusic getGUI(){
		return gui;
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/*
		 * Set the Nimbus look and feel
		 */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("GTK+".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
				}
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					//break;
				}
			}
		} catch (ClassNotFoundException ex){
			System.err.println("Error while attempting to set the UI look and feel: " + ex.getLocalizedMessage());
		} catch (InstantiationException ex){
			System.err.println("Error while attempting to set the UI look and feel: " + ex.getLocalizedMessage());
		} catch (IllegalAccessException ex){
			System.err.println("Error while attempting to set the UI look and feel: " + ex.getLocalizedMessage());
		} catch (javax.swing.UnsupportedLookAndFeelException ex){
			System.err.println("Error while attempting to set the UI look and feel: " + ex.getLocalizedMessage());
		}
		//</editor-fold>
		
		System.setProperty("jna.nosys","true");
				
		try {
			args = Gst.init("jSocialPlayer", args);
		} catch (UnsatisfiedLinkError ex){
			//Test to see if we are on windows and using 64bit
			String os = System.getProperty("os.name");
			String arch = System.getProperty("os.arch");

			if (os.startsWith("Windows") && arch.equals("amd64")){
				JDialogAdvanced.showWindowsJava64bitError();
			} else if (os.startsWith("Windows")){
				JDialogAdvanced.showWindowsJava32bitError();
			} else {
				JDialogAdvanced.showOtherPlatformError();
			}
			
			//Exit with error
			System.exit(1);
		}
		
		musicPlayer = new MusicPlayerGStreamer();
		playlist = Playlist.getInstance();
		dataStorage = DataStorageProperties.getInstance();
		gui = new jMusic();
		
		/*
		 * Create and display the form
		 */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.setVisible(true);
			}
		});
	}
}
