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
import java.util.concurrent.TimeUnit;
import org.gstreamer.*;
import org.gstreamer.elements.PlayBin2;

/**
 *
 * @author DrLabman
 */
public class MusicPlayerGStreamer implements MusicPlayer {
	boolean playing = false;
	boolean paused = false;
	PlayBin2 playbin = null;
		
	public MusicPlayerGStreamer(){
		playbin = new PlayBin2("AudioPlayer");
		playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
		playbin.getBus().connect(new Bus.TAG() {
			@Override
			public void tagsFound(GstObject go, TagList tl) {
//				for (String tagName : tl.getTagNames()) {
//					System.out.println("Tag: " + tagName);
//				}
			}
		});
		playbin.getBus().connect(new Bus.EOS() {
			@Override
			public void endOfStream(GstObject go) {
				System.out.println("Stream finsihed!");
				stop();
			}
		});
		playbin.getBus().connect(new Bus.BUFFERING() {
			@Override
			public void bufferingData(GstObject go, int i) {
				System.out.println("Buffering: " + go.getName() + " : " + i);
			}
		});
		playbin.getBus().connect(new Bus.INFO() {
			@Override
			public void infoMessage(GstObject go, int i, String string) {
				System.out.println(i + " : " + string);
			}
		});
        // Listen for state-changed messages
        playbin.getBus().connect(new Bus.STATE_CHANGED() {
			@Override
             public void stateChanged(GstObject source, State old, State current, State pending) {
                if (source == playbin) {
					System.out.println(source.getName() + ": Pipeline state changed from " + old + " to " + current + " with " + pending);
					switch (current){
						case PAUSED:
							paused = true;
							break;
						case PLAYING:
							playing = true;
							paused = false;
							long position = playbin.queryPosition(TimeUnit.MILLISECONDS);
							long duration = playbin.queryDuration(TimeUnit.MILLISECONDS);
							System.out.println("Pos: " + position + " / " + duration);
							break;
						case READY:
							playing = false;
							paused = false;
							break;
						case NULL:
							playing = false;
							paused = false;
							break;
					}
                } else {
					//System.out.println(source.getName() + ": Pipeline state changed from " + old + " to " + current + " with " + pending);
				}
            }
        });
		playbin.getBus().connect(new Bus.MESSAGE() {
			@Override
			public void busMessage(Bus bus, Message msg) {
				
				switch (msg.getType()){
					case TAG:
					case STATE_CHANGED:
					case STREAM_STATUS:
						// This message is posted when a streaming thread is created/destroyed or when the state changed. 
						break;
					default:
//						Structure struct = msg.getStructure();
//						System.out.println("Message: " + msg.getStructure().getName());// msg.toString());
//						System.out.println("Type: " + msg.getType());
//						int max = struct.getFields();
//						for (int i=0; i<max; i++){
//							System.out.println("MessageData: " + struct.getName(i));
//							System.out.println("MessageValue: " + struct.getValue(struct.getName(i)));
//						}
				}
			}
		});
		playbin.getBus().connect(new Bus.WARNING() {
			@Override
			public void warningMessage(GstObject go, int i, String string) {
				System.out.println(i + " : " + string);
			}
		});
	}
	
	@Override
	public void play(File musicFile) {       
        playbin.setInputFile(musicFile);
		playbin.play();
	}

	@Override
	public void pause() {
		playbin.pause();
	}

	@Override
	public void resume() {
		playbin.play();
	}

	@Override
	public void stop() {
		playbin.stop();
	}

	@Override
	public boolean isPlaying() {
		return playing;
	}

	@Override
	public boolean isPaused() {
		return paused;
	}

	@Override
	public long getSongLength() {
		return playbin.queryDuration(TimeUnit.MICROSECONDS);
	}

	@Override
	public long getSongPosition() {
		return playbin.queryPosition(TimeUnit.MICROSECONDS);
	}

	@Override
	public void setSongPosition(long position) {
		playbin.pause();
		playbin.seek(position,TimeUnit.MICROSECONDS);
		playbin.play();
	}	
}
