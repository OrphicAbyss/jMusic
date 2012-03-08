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
package jmusic.config;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Load and store data for the music player
 */
public class DataStorageProperties implements DataStorage {
	private Properties propFile;
	
	public static DataStorageProperties getInstance() {
		return DataStorageHolder.INSTANCE;
	}

	private static class DataStorageHolder {
		private static final DataStorageProperties INSTANCE = new DataStorageProperties();
	}
	
	private DataStorageProperties(){
		this.load(new File(this.getDefaultFilename()));
	}
	
	@Override
	public void load(File file) {
		propFile = new Properties();
		if (file.exists()){
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				propFile.load(fis);
			} catch (FileNotFoundException ex) {
				Logger.getLogger(DataStorageProperties.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(DataStorageProperties.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				try {
					fis.close();
				} catch (IOException ex) {
					Logger.getLogger(DataStorageProperties.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	@Override
	public void save(File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			propFile.store(fos, "jMusic config file");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(DataStorageProperties.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(DataStorageProperties.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fos.close();
			} catch (IOException ex) {
				Logger.getLogger(DataStorageProperties.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	@Override
	public String get(String key){
		return propFile.getProperty(key);
	}
	
	@Override
	public void set(String key, String value){
		propFile.setProperty(key, value);
	}
	
	@Override
	public String getDefaultFilename() {
		return "jmusic.properties";
	}
}
