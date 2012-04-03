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
import java.util.ArrayList;
import sun.misc.Sort;

/**
 *
 * @author DrLabman
 */
public class FileFinder {
	/**
	 * Recurses the given folder and finds any file with an extension in our
	 * list of supported extensions
	 * 
	 * @param dir folder to search for files
	 * @param sortCompare the compare object to use to sort the files
	 * @param filter defines which files are acceptable in this list
	 * @return ArrayList of File objects representing music 
	 */
	public static ArrayList<File> findAllFiles(File dir, FileCompare sortCompare, FileFilter filter){
		// Get all the files from the passed in directory
		File fileArray[] = dir.listFiles(filter);
		
		// create an output list
		ArrayList<File> fullList = new ArrayList<File>();
			
		if (fileArray != null){
			// Sort the files into alphabetical order
			Sort.quicksort(fileArray, sortCompare);
			
			// for each file
			for (File file: fileArray){
				if (file.isDirectory()){
					// If it's a directory recurse
					fullList.addAll(findAllFiles(file, sortCompare, filter));
				} else {
					// It's a music file (list is prefiltered
					fullList.add(file);
				}
			}
		}
		// return out list
		return fullList;
	}
}
