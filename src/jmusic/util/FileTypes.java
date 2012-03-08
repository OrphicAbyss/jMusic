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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.gstreamer.TypeFindFactory;

/**
 * Contains different file type lists to be used when looking for files.
 * 
 * @author DrLabman
 */
public class FileTypes {
	public enum Types {
		AUDIO("audio"), VIDEO("video"), IMAGE("image");
		
		String search;
		Types(String search){
			this.search = search;
		}
		
		public String getSearchValue(){
			return search;
		}
	}
	public static final boolean DEBUG = true;
	private HashSet<String> extensions;
	
	private FileTypes(Types type){
		String searchStart = type.getSearchValue();
		
		//Build supported extension list for audio
		extensions = new HashSet<String>();
		List<TypeFindFactory> list = TypeFindFactory.getList();
		for (TypeFindFactory factory: list){
			String extensionStrs[] = factory.getExtentions();
			if (extensionStrs != null){
				for (String ext: extensionStrs){
					if (factory.getName().startsWith(searchStart)){
						extensions.add(ext);
						if (DEBUG)
							System.out.println(factory.getName() + ": " + ext);
					}
				}
			}
		}
	}
	
	private static HashMap<Types,FileTypes> instances = new HashMap<Types,FileTypes>();
	
	public static FileTypes getInstance(Types type) {
		if (!instances.containsKey(type)){
			instances.put(type, new FileTypes(type));
		}
		return instances.get(type);
	}
		
	/**
	 * Tests a given file extension against our list of valid extensions which
	 * can be loaded by gstreamer.
	 * 
	 * @param ext The filetype extension to test
	 * @return true if it is a valid extension
	 */
	public boolean isValid(String ext){
		return extensions.contains(ext);
	}
}
