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

import java.util.HashMap;
import java.util.List;
import org.gstreamer.TagList;

/**
 * Holds metadata information for a media file. Information is parsed from 
 * gstreamer tagList's.
 * 
 * @author DrLabman
 */
public class Metadata {
	enum MetadataType {
		TITLE("title"),
		ARTIST("artist"),
		ALBUM("album"),
		TRACK_NUMBER("track-number");
		
		
		String value;
		MetadataType(String value){
			this.value = value;
		}
		
		String getValue(){
			return value;
		}
		
		static final HashMap<String,MetadataType> lookup = new HashMap<String,MetadataType>();
		static {
			MetadataType values[] = MetadataType.values();
			
			for (MetadataType value: values){
				lookup.put(value.getValue(), value);
			}
		}
		
		/**
		 * Given a string value we find a matching Enum value.
		 * 
		 * @param value The string value of the Enum we want
		 * @return MetadataType enum object
		 */
		static MetadataType parse(String value){
			//return lookup.get(value);
			
			MetadataType types[] = MetadataType.values();
			for (MetadataType type: types){
				if (type.getValue().equals(value))
					return type;
			}
			return null;
		}
	}
	
	private String title;
	private String artist;
	private String album;
	private String trackNumber;
	public boolean hasData = false;
	private boolean parsed = false;
	private List<TagList> tagList = null;
	
	/**
	 * Extract metadata from the provided list of TagList objects
	 * 
	 * @param tagList A list of TagLists to parse for data
	 */
	public Metadata(List<TagList> tagList){
		this.tagList = tagList;
	}
	
	/**
	 * Extract metadata from a TagList object
	 * 
	 * @param tags A TagList object to parse data from
	 */
	public Metadata(TagList tags){
		parse(tags);
	}
	
	/**
	 * Parse the list
	 */
	private void parse(){
		for (TagList tags: tagList){
			parse(tags);
		}
	}
	
	/**
	 * Parse out the tag fields we want
	 * 
	 * @param tags 
	 */
	private void parse(TagList tags){
		parsed = true;
		
		for (String tagName : tags.getTagNames()) {
			MetadataType type = MetadataType.parse(tagName);
			if (type != null){
				switch (type) {
					case TITLE:
						title = data(tags.getValues(tagName));
						hasData = true;
						break;
					case ARTIST:
						artist = data(tags.getValues(tagName));
						hasData = true;
						break;
					case ALBUM:
						album = data(tags.getValues(tagName));
						hasData = true;
						break;
					case TRACK_NUMBER:
						trackNumber = data(tags.getValues(tagName));
						hasData = true;
						break;
				}
			}
		}
	}
	
	/**
	 * Concatenates multiple value tag values into one string.
	 * 
	 * @param tagDataValues A list of values
	 * @return A concatenated string of the values
	 */
	private String data(List<Object> tagDataValues){
		StringBuilder sb = new StringBuilder();
		for (Object tagData : tagDataValues) {
			sb.append(tagData);
		}
		return sb.toString();
	}

	/**
	 * Get the Title of the media
	 * 
	 * @return the title
	 */
	public String getTitle() {
		if (!parsed)
			parse();
		
		return title;
	}

	/**
	 * Get the Artist of the media
	 * 
	 * @return the artist
	 */
	public String getArtist() {
		if (!parsed)
			parse();
		
		return artist;
	}

	/**
	 * Get the Album name of the media
	 * 
	 * @return the album
	 */
	public String getAlbum() {
		if (!parsed)
			parse();
		
		return album;
	}

	/**
	 * Get the Track Number of the media
	 * 
	 * @return the trackNumber
	 */
	public String getTrackNumber() {
		if (!parsed)
			parse();
		
		return trackNumber;
	}
}
