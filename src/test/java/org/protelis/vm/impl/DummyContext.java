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

import it.unibo.alchemist.model.interfaces.IPosition;

import org.apache.commons.math3.util.Pair;
import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.datatype.DeviceUID;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
	public double distanceTo(final DeviceUID target) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public IPosition getDevicePosition() {
		return new DummyPosition();
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
	
	private static class DummyPosition implements IPosition {
		private static final long serialVersionUID = 1L;
		@Override
		public int compareTo(final IPosition o) {
			return 0;
		}
		@Override
		public double getDistanceTo(final IPosition p) {
			return 0;
		}
		@Override
		public int getDimensions() {
			return 2;
		}
		@Override
		public double getCoordinate(final int dim) {
			return 0;
		}
		@Override
		public double[] getCartesianCoordinates() {
			return new double[]{0, 0};
		}
		@Override
		public Pair<IPosition, IPosition> buildBoundingBox(final double range) {
			return Pair.create(this, this);
		}
		@Override
		public boolean equals(final Object obj) {
			return obj instanceof DummyPosition;
		}
		@Override
		public int hashCode() {
			return 0;
		}
	}


}
