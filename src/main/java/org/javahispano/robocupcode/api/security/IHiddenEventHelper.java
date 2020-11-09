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
package org.javahispano.robocupcode.api.security;

import java.awt.Graphics2D;	

/**
 * @author Pavel Savara (original)
 */
public interface IHiddenEventHelper {
	void setDefaultPriority(Event event);
	void setPriority(Event event, int newPriority);
	void setTime(Event event, long newTime);
	boolean isCriticalEvent(Event event);
	void dispatch(Event event, IBasicRobot robot, IRobotStatics statics, Graphics2D graphics);
	byte getSerializationType(Event event);
}
