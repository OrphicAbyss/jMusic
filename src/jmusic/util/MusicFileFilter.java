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
package jmusic.util;

import java.io.File;
import java.io.FileFilter;
import jmusic.util.FileTypes.Types;

/**
 * Accepts audio files or directories as valid files.
 * 
 * @author DrLabman
 */
public class MusicFileFilter implements FileFilter {
	/** Set of audio file extensions supported by gstreamer */
	FileTypes audioFileTypes = null;
	
	public MusicFileFilter(){
		audioFileTypes = FileTypes.getInstance(Types.AUDIO);
	}
	
	@Override
	public boolean accept(File pathname) {

		int extStart = pathname.getName().lastIndexOf(".");
		if (extStart != -1){
			String ext = pathname.getName().substring(extStart + 1).toLowerCase();
			return pathname.isDirectory() || audioFileTypes.isValid(ext);
		} else
			return pathname.isDirectory();
	}
}
