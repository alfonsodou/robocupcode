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

/**
 * @author Alfonso Dou
 *
 */
public interface ISettingsManager {
	void saveProperties();
	
	long getCpuConstant();

	void setCpuConstant(long cpuConstant);
	
	public final static String
		CPU_CONSTANT = "robocupcode.cpu.constant",
		
		MATCHFIELD_WIDTH = "robocupcode.matchField.width",
		MATCHFIELD_HEIGHT = "robocupcode.matchField.height",
		MATCH_INITIAL_POSITIONS = "robocupcode.match.initialPositions";

	int getMatchDefaultMatchfieldWidth();

	int getMatchDefaultMatchfieldHeight();
}
