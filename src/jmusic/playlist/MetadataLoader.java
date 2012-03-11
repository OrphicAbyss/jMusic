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
import org.gstreamer.*;
import org.gstreamer.elements.PlayBin2;

/**
 * Singleton class which loads metadata for a given file.
 * 
 * @author DrLabman
 */
public class MetadataLoader {
	ArrayList<TagList> tags = null;
	PlayBin2 metadataPlaybin;
	boolean ready = false;

	private MetadataLoader() {
		metadataPlaybin = new PlayBin2("Metadata");
		metadataPlaybin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
		metadataPlaybin.setAudioSink(ElementFactory.make("fakesink", "audiosink"));
		metadataPlaybin.getBus().connect(new Bus.TAG() {
			@Override
			public void tagsFound(GstObject source, TagList tagList) {
//				System.out.println("Tag");
				tags.add(tagList);
			}
		});
		metadataPlaybin.getBus().connect(new Bus.STATE_CHANGED() {
			@Override
			public void stateChanged(GstObject go, State oldState, State newState, State pendingState) {
				if (go.getName().equals("Metadata")){
//					System.out.println("Metadata State: " + newState);
					ready = true;
				}
			}
		});
	}
	
	public static MetadataLoader getInstance() {
		return MetadataLoader.MetadataLoaderHolder.INSTANCE;
	}
	
	private static class MetadataLoaderHolder {
		private static final MetadataLoader INSTANCE = new MetadataLoader();
	}
	
	public Metadata loadMetadata(File file) {
		tags = new ArrayList<>();
        metadataPlaybin.setInputFile(file);
		metadataPlaybin.setState(State.PLAYING);
		ready = false;
		StateChangeReturn type = metadataPlaybin.setState(State.NULL);
//		System.out.println("Changed State: " + type.name());
		// This ensures that we always wait for bus messages to be flushed
		// continuing. Bus messages should be flushed automatically, but
		// it appears not to happen correctly sometimes
		while (!ready){
			try {
				Thread.sleep(20);
			} catch (InterruptedException ex) {}
		}
//		printTagDebug();
		Metadata data = new Metadata(tags);
		return data;
	}
	
	private void printTagDebug(){
		System.out.println("Tag objects: " + tags.size());
		int i = 0;
		for (TagList tagList: tags){
			System.out.println("Tag Group: " + ++i);
			for (String tagName : tagList.getTagNames()) {
				for (Object tagData : tagList.getValues(tagName)) {
					System.out.printf("Tag [%s]=%s\n", tagName, tagData);
				}
			}
		}		
	}
}
