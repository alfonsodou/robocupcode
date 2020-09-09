/**
 * Robocode - Copyright (c) 2001-2020 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */

/**
 * Robocupcode (Fork of Robocode) - Copyright (c) 2020 Alfonso Dou
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package org.javahispano.robocupcode.core.settings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.javahispano.robocupcode.api.io.Logger;

/**
 * @author Alfonso Dou
 *
 */
public class SettingsManager implements ISettingsManager {
	// Robocupcode internals
	private long cpuConstant = -1;
	
	// Match default settings
	private int matchDefaultMatchfieldWidth = 800;
	private int matchDefaultMAtchfieldHeight = 600;

	private final Properties props = new SortedProperties();

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
	
	private final List<ISettingsListener> listeners = new ArrayList<ISettingsListener>();
	
	@Override
	public void saveProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	public long getCpuConstant() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCpuConstant(long cpuConstant) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public int getMatchDefaultMatchfieldWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMatchDefaultMatchfieldHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void notifyPropertyChanged(String name) {
		for (ISettingsListener listener : listeners) {
			try {
				listener.settingChanged(name);
			} catch (Exception e) {
				Logger.logError(e);
			}
		}
	}
	
	/**
	 * Sorted properties used for sorting the keys for the properties file.
	 *
	 * @author Flemming N. Larsen
	 */
	private class SortedProperties extends Properties {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Enumeration<Object> keys() {
			Enumeration<Object> keysEnum = super.keys();

			Vector<String> keyList = new Vector<String>();

			while (keysEnum.hasMoreElements()) {
				keyList.add((String) keysEnum.nextElement());
			}

			Collections.sort(keyList);

			// noinspection RedundantCast
			return (Enumeration) keyList.elements();
		}

		@Override
		public synchronized Object setProperty(String key, String value) {
			final String old = super.getProperty(key, null);
			boolean notify = (old == null && value != null) || (old != null && !old.equals(value));
			final Object res = super.setProperty(key, value);

			if (notify) {
				notifyPropertyChanged(key);
			}
			return res;
		}
	}	
}
