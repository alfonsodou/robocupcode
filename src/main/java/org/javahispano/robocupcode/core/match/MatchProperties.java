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
package org.javahispano.robocupcode.core.match;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import org.javahispano.robocupcode.core.settings.ISettingsManager;

/**
 * @author Alfonso Dou
 *
 */
public class MatchProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final static String
			MATCHFIELD_WIDTH = "robocupcode.matchField.width",
			MATCHFIELD_HEIGHT = "robocupcode.matchField.height",
			MATCH_INITIAL_POSITIONS = "robocupcode.match.initialPositions";
	
	private int matchfieldWidth = 800;
	private int matchfieldHeight = 600;
	private String initialPositions;
	
	private final Properties props = new Properties();
	
	public MatchProperties() {
	}
	
	public MatchProperties(ISettingsManager properties) {
		matchfieldWidth = properties.getMatchDefaultMatchfieldWidth();
		matchfieldHeight = properties.getMatchDefaultMatchfieldHeight();
	}

	/**
	 * Get the matchfieldWidth
	 * 
	 * @return the matchfieldWidth
	 */
	public int getMatchfieldWidth() {
		return matchfieldWidth;
	}

	/**
	 * Set the matchfieldWidth
	 * 
	 * @param matchfieldWidth the matchfieldWidth to set
	 */
	public void setMatchfieldWidth(int matchfieldWidth) {
		this.matchfieldWidth = matchfieldWidth;
	}

	/**
	 * Get the matchfieldHeight
	 * 
	 * @return the matchfieldHeight
	 */
	public int getMatchfieldHeight() {
		return matchfieldHeight;
	}

	/**
	 * Set the matchfieldHeight
	 * 
	 * @param matchfieldHeight the matchfieldHeight to set
	 */
	public void setMatchfieldHeight(int matchfieldHeight) {
		this.matchfieldHeight = matchfieldHeight;
	}

	/**
	 * Get the initialPositions
	 * 
	 * @return the initialPositions
	 */
	public String getInitialPositions() {
		return initialPositions;
	}

	/**
	 * Set the initialPositions
	 * 
	 * @param initialPositions the initialPositions to set
	 */
	public void setInitialPositions(String initialPositions) {
		this.initialPositions = initialPositions;
	}
	
	public void store(FileOutputStream out, String desc) throws IOException {
		props.store(out, desc);
	}

	public void load(FileInputStream in) throws IOException {
		props.load(in);
		matchfieldWidth = Integer.parseInt(props.getProperty(MATCHFIELD_WIDTH));
		matchfieldHeight = Integer.parseInt(props.getProperty(MATCHFIELD_HEIGHT));
		initialPositions = props.getProperty(MATCH_INITIAL_POSITIONS);
	}
}
