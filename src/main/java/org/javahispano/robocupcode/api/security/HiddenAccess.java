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

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.javahispano.robocupcode.api.io.Logger;
import org.javahispano.robocupcode.core.ContainerBase;

import robocupcode.control.events.IMatchListener;


/**
 * Helpers for accessing hidden methods on events.
 *
 * @author Pavel Savara (original)
 */
public class HiddenAccess {
	private static IHiddenEventHelper eventHelper;
	private static IHiddenBallHelper ballHelper;
	private static IHiddenSpecificationHelper specificationHelper;
	private static IHiddenStatusHelper statusHelper;
	private static IHiddenRulesHelper rulesHelper;
	private static Method initContainer;
	private static Method initContainerRe;
	private static Method cleanup;
	private static Method robocupcodeMain;
	private static boolean initialized;
	private static boolean foundCore = false;

	public static void init() {
		if (initialized) {
			return;
		}
		Method method;

		try {
			method = Event.class.getDeclaredMethod("createHiddenHelper");
			method.setAccessible(true);
			eventHelper = (IHiddenEventHelper) method.invoke(null);
			method.setAccessible(false);

			method = Bullet.class.getDeclaredMethod("createHiddenHelper");
			method.setAccessible(true);
			ballHelper = (IHiddenBallHelper) method.invoke(null);
			method.setAccessible(false);

			method = RobotSpecification.class.getDeclaredMethod("createHiddenHelper");
			method.setAccessible(true);
			specificationHelper = (IHiddenSpecificationHelper) method.invoke(null);
			method.setAccessible(false);

			method = RobotStatus.class.getDeclaredMethod("createHiddenSerializer");
			method.setAccessible(true);
			statusHelper = (IHiddenStatusHelper) method.invoke(null);
			method.setAccessible(false);

			method = BattleRules.class.getDeclaredMethod("createHiddenHelper");
			method.setAccessible(true);
			rulesHelper = (IHiddenRulesHelper) method.invoke(null);
			method.setAccessible(false);

			ClassLoader loader = getClassLoader();
			Class<?> main = loader.loadClass("org.javahispano.robocupcode.core.RobocupcodeMainBase");

			initContainer = main.getDeclaredMethod("initContainer");
			initContainer.setAccessible(true);

			initContainerRe = main.getDeclaredMethod("initContainerForRobocupcodeEngine", File.class, IMatchListener.class);
			initContainerRe.setAccessible(true);

			cleanup = main.getDeclaredMethod("cleanupForRobocupcodeEngine");
			cleanup.setAccessible(true);

			robocupcodeMain = main.getDeclaredMethod("robocupcodeMain", Object.class);
			robocupcodeMain.setAccessible(true);

			initialized = true;
		} catch (NoSuchMethodException e) {
			Logger.logError(e);
		} catch (InvocationTargetException e) {
			Logger.logError(e);
		} catch (IllegalAccessException e) {
			Logger.logError(e);
		} catch (ClassNotFoundException e) {
			Logger.logError(e);
			if (!foundCore) {
				Logger.logError("Can't find robocupcode.core-1.x.jar module near to robocupcode.jar");
				Logger.logError("Class path: " + System.getProperty("robocupcode.class.path", null));
			}
			System.exit(-1);
		} catch (MalformedURLException e) {
			Logger.logError(e);
		} catch (Error e) {
			Logger.logError(e);
			throw e;
		}

	}

	private static ClassLoader getClassLoader() throws MalformedURLException {
		// if other modules are .jar next to robocode.jar on same path, we will create classloader which will load them
		// otherwise we rely on that they are already on classpath
		StringBuilder classPath = new StringBuilder(System.getProperty("java.class.path", null));
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		String path = HiddenAccess.class.getProtectionDomain().getCodeSource().getLocation().getPath();

		try {
			path = URLDecoder.decode(path, "UCS2");
		} catch (UnsupportedEncodingException e) {
			path = new File(".", "libs/robocupcode.jar").toString();
		}
		final int i = path.lastIndexOf("robocupcode.jar");

		if (i > 0) {
			loader = createClassLoader(classPath, loader, path.substring(0, i));
		}
		System.setProperty("robocupcode.class.path", classPath.toString());
		return loader;
	}

	private static ClassLoader createClassLoader(StringBuilder classPath, ClassLoader loader, String dir) throws MalformedURLException {

		File dirf = new File(dir);
		ArrayList<URL> urls = new ArrayList<URL>();

		final File[] files = dirf.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				final String test = name.toLowerCase();

				return test.endsWith(".jar") && !test.endsWith("robocupcode.jar");
			}
		});

		if (files != null) {
			for (File file : files) {
				final String name = file.toString().toLowerCase();
				final URL url = file.toURI().toURL();
				
				if (name.contains("robocupcode.core")) { // Robocode core
					foundCore = true;
					urls.add(url);
				}
				if (name.contains("picocontainer")) { // Picocontainer used for modularization
					urls.add(url);
				}
				if (name.contains("codesize")) { // Codesize tool
					urls.add(url);
				}
				if (name.contains("bcel")) { // BCEL used by Codesize
					urls.add(url);
				}
				if (name.contains("kotlin-stdlib")) { // Kotlin standard library
					urls.add(url);
				}
				classPath.append(File.pathSeparator);
				classPath.append(file.toString());
			}
		}
		return new URLClassLoader(urls.toArray(new URL[urls.size()]), loader);
	}

	public static boolean isCriticalEvent(Event e) {
		return eventHelper.isCriticalEvent(e);
	}

	public static void setEventTime(Event e, long newTime) {
		eventHelper.setTime(e, newTime);
	}

	public static void setEventPriority(Event e, int newPriority) {
		eventHelper.setPriority(e, newPriority);
	}

	public static void dispatch(Event event, IBasicRobot robot, IRobotStatics statics, Graphics2D graphics) {
		eventHelper.dispatch(event, robot, statics, graphics);
	}

	public static void setDefaultPriority(Event e) {
		eventHelper.setDefaultPriority(e);
	}

	public static byte getSerializationType(Event e) {
		return eventHelper.getSerializationType(e);
	}

	public static void update(Ball ball, double x, double y, double z) {
		ballHelper.update(ball, x, y, z);
	}

	public static RobotSpecification createSpecification(Object fileSpecification, String name, String author, String webpage, String version, String robocodeVersion, String jarFile, String fullClassName, String description) {
		return specificationHelper.createSpecification(fileSpecification, name, author, webpage, version,
				robocodeVersion, jarFile, fullClassName, description);
	}

	public static Object getFileSpecification(RobotSpecification specification) {
		return specificationHelper.getFileSpecification(specification);
	}

	public static String getRobotTeamName(RobotSpecification specification) {
		return specificationHelper.getTeamName(specification);
	}

	public static void setTeamName(RobotSpecification specification, String teamName) {
		specificationHelper.setTeamName(specification, teamName);
	}

	public static RobotStatus createStatus(double energy, double x, double y, double bodyHeading, double gunHeading, double radarHeading, double velocity,
			double bodyTurnRemaining, double radarTurnRemaining, double gunTurnRemaining, double distanceRemaining, double gunHeat, int others,
			int numSentries, int roundNum, int numRounds, long time) {
		return statusHelper.createStatus(energy, x, y, bodyHeading, gunHeading, radarHeading, velocity,
				bodyTurnRemaining, radarTurnRemaining, gunTurnRemaining, distanceRemaining, gunHeat, others, numSentries,
				roundNum, numRounds, time);
	}

	public static MatchRules createRules(int matchfieldWidth, int matchfieldHeight, int numRounds, double gunCoolingRate, long inactivityTime, boolean hideEnemyNames, int sentryBorderSize) {
		return rulesHelper.createRules(matchfieldWidth, matchfieldHeight, numRounds, gunCoolingRate, inactivityTime,
				hideEnemyNames, sentryBorderSize);
	}

	public static boolean isSafeThread() {
		final IThreadManagerBase threadManager = ContainerBase.getComponent(IThreadManagerBase.class);

		return threadManager != null && threadManager.isSafeThread();
	}

	public static void initContainerForRobotEngine(File robocupcodeHome, IMatchListener listener) {
		init();
		try {
			initContainerRe.invoke(null, robocupcodeHome, listener);
		} catch (IllegalAccessException e) {
			Logger.logError(e);
		} catch (InvocationTargetException e) {
			Logger.logError(e.getCause());
			Logger.logError(e);
		}
	}

	public static void initContainer() {
		init();
		try {
			initContainer.invoke(null);
		} catch (IllegalAccessException e) {
			Logger.logError(e);
		} catch (InvocationTargetException e) {
			Logger.logError(e.getCause());
			Logger.logError(e);
		}
	}

	public static void cleanup() {
		init();
		try {
			cleanup.invoke(null);
		} catch (IllegalAccessException e) {
			Logger.logError(e);
		} catch (InvocationTargetException e) {
			Logger.logError(e.getCause());
			Logger.logError(e);
		}
	}

	public static void robocupcodeMain(final String[] args) {
		init();
		try {
			robocupcodeMain.invoke(null, (Object) args);
		} catch (IllegalAccessException e) {
			Logger.logError(e);
		} catch (InvocationTargetException e) {
			Logger.logError(e.getCause());
			Logger.logError(e);
		}
	}

}