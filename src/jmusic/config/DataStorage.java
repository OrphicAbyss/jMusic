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

import java.io.File;

/**
 * Interface which defines the methods needed by a data storage class for saving
 * configuration details to a file
 */
public interface DataStorage {
	/**
	 * Load a configuration file
	 * 
	 * @param file The file to load
	 */
	public void load(File file);
	/**
	 * Save a configuration file
	 * 
	 * @param file The file to save to
	 */
	public void save(File file);
	/**
	 * Set a config key to the provided value
	 * 
	 * @param key The key to set
	 * @param value The value
	 */
	public void set(String key, String value);
	/**
	 * Get the value of a config key
	 * @param key The key of the value to retrieve
	 * @return The value of the key
	 */
	public String get(String key);
	/**
	 * Returns the default filename for the implementing data storage class
	 * @return 
	 */
	public String getDefaultFilename();
}
