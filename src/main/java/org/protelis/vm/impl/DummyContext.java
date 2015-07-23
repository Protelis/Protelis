/**
 * 
 */
package org.protelis.vm.impl;

import it.unibo.alchemist.model.interfaces.IPosition;

import org.apache.commons.math3.util.Pair;
import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.impl.DeviceUIDImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A dummy Protelis VM to be used for testing.
 * 
 * @author Danilo Pianini
 *
 */
public final class DummyContext extends AbstractExecutionContext {
	
	private final Random rng = new Random(0);
	private final DeviceUID dummy = new DeviceUIDImpl(0);
	private Map<FasterString, Object> environment = new HashMap<>();
	
	
	/**
	 * 
	 */
	public DummyContext() {
		super(new DummyNetworkManager());
	}

	@Override
	public DeviceUID getDeviceUID() {
		return dummy;
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
		return new IPosition() {
			private static final long serialVersionUID = 1L;
			@Override
			public int compareTo(IPosition o) {
				return 0;
			}
			@Override
			public double getDistanceTo(IPosition p) {
				return 0;
			}
			@Override
			public int getDimensions() {
				return 2;
			}
			@Override
			public double getCoordinate(int dim) {
				return 0;
			}
			@Override
			public double[] getCartesianCoordinates() {
				return new double[]{0, 0};
			}
			@Override
			public Pair<IPosition, IPosition> buildBoundingBox(double range) {
				return Pair.create(this, this);
			}
		};
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
	protected DeviceUID deviceFromId(final long id) {
		return dummy;
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
