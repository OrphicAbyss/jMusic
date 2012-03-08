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
package jmusic.playback;

import java.io.File;

/**
 * Implementing classes control the playback of music.
 */
public interface MusicPlayer {
	/**
	 * Load and start playback of a file
	 * 
	 * @param musicFile The file to play back
	 */
	public void play(File musicFile);
	
	/**
	 * Pause playback
	 */
	public void pause();
	
	/**
	 * Resume playback
	 */
	public void resume();
	
	/**
	 * Stop playback
	 */
	public void stop();
	
	/**
	 * Returns true if music is playing
	 * 
	 * @return true if music is playing, false if music has finished
	 */
	public boolean isPlaying();
	
	/**
	 * Returns true if music is paused
	 * 
	 * @return true if music has been paused
	 */
	public boolean isPaused();
	
	/**
	 * Returns the total song time for the current music file
	 * 
	 * @return length of time in microseconds of the length of the song
	 */
	public long getSongLength();
	
	/**
	 * Return the current position in the current music file
	 * 
	 * @return number of microseconds elapsed since the start of the song
	 */
	public long getSongPosition();
	
	/**
	 * Set the song playback position in the current music file
	 * 
	 * @param position number of microseconds from the start of the song
	 */
	public void setSongPosition(long position);
}
