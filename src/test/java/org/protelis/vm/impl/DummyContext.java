/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
/**
 * 
 */
package org.protelis.vm.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.datatype.DeviceUID;

/**
 * A dummy Protelis VM to be used for testing.
 *
 */
public final class DummyContext extends AbstractExecutionContext {
	
	private final Random rng = new Random(0);
	private static final DeviceUID DUMMYUID = new DeviceUID() {
		private static final long serialVersionUID = 2306021805006825289L;
	};
	private Map<FasterString, Object> environment = new HashMap<>();
	
	
	/**
	 * 
	 */
	public DummyContext() {
		super(new DummyNetworkManager());
	}

	@Override
	public DeviceUID getDeviceUID() {
		return DUMMYUID;
	}

	@Override
	public Number getCurrentTime() {
		return System.currentTimeMillis() / 1000d;
	}

	@Override
	public double nextRandomDouble() {
		return rng.nextDouble();
	}

	@Override
	protected AbstractExecutionContext instance() {
		return new DummyContext();
	}

	@Override
	protected Map<FasterString, Object> currentEnvironment() {
		return environment;
	}

	@Override
	protected void setEnvironment(final Map<FasterString, Object> newEnvironment) {
		environment = newEnvironment;
	}
}
