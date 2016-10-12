package org.protelis.test;

import java.util.Random;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Tuple;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.ProtelisVM;
import org.protelis.vm.impl.AbstractExecutionContext;
import org.protelis.vm.impl.SimpleExecutionEnvironment;

/**
 * A simple implementation of a Protelis-based device, encapsulating a
 * ProtelisVM and a network interface.
 */
public class SimpleDevice extends AbstractExecutionContext implements SpatiallyEmbeddedDevice {
    private final Random r;
    /** Device numerical identifier */
    private final IntegerUID uid;
    /** The Protelis VM to be executed by the device */
    private final ProtelisVM vm;
    private Position position;

    /**
     * Standard constructor.
     * 
     * @param program
     *            program to be executed
     * @param uid
     *            universal identifier
     * @param position
     *            position
     */
    public SimpleDevice(final ProtelisProgram program, final int uid, final Position position) {
        super(new SimpleExecutionEnvironment(), new CachingNetworkManager());
        this.uid = new IntegerUID(uid);
        this.position = position;
        r = new Random(0);
        vm = new ProtelisVM(program, this);
    }

    /**
     * Internal-only lightweight constructor to support "instance"
     * 
     * @param uid
     *            universal identifier
     */
    private SimpleDevice(final IntegerUID uid) {
        super(new SimpleExecutionEnvironment(), new CachingNetworkManager());
        this.uid = uid;
        r = new Random(0);
        vm = null;
    }

    /**
     * @return virtual machine, to allow external execution triggering
     */
    public ProtelisVM getVM() {
        return vm;
    }

    /**
     * Test actuator that dumps a string message to the output.
     * 
     * @param message
     *            to be announced
     */
    public void announce(final String message) {
        System.out.println(message);
    }

    /**
     * Move in a direction specified by the 3-tuple vector in meters Uses a
     * kludge vector in which +X = East, +Y = North.
     * 
     * @param vector
     *            move the device of the given vector
     */
    public void move(final Tuple vector) {
        position = position.addVector((Double) vector.get(0), (Double) vector.get(1), (Double) vector.get(2));
    }

    /**
     * @return return the device position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Expose the network manager, to allow external simulation of network For
     * real devices, the NetworkManager usually runs autonomously in its own
     * thread(s).
     * 
     * @return network manager
     */
    public CachingNetworkManager accessNetworkManager() {
        return (CachingNetworkManager) super.getNetworkManager();
    }

    @Override
    public DeviceUID getDeviceUID() {
        return uid;
    }

    @Override
    public Number getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Return delta time.
     * 
     * @return delta time
     */
    public double dt() {
        return 1;
    }

    @Override
    protected AbstractExecutionContext instance() {
        return new SimpleDevice(uid);
    }

    /**
     * Note: this should be going away in the future, to be replaced by standard
     * Java random.
     */
    @Override
    public double nextRandomDouble() {
        return r.nextDouble();
    }

    @Override
    public Field nbrRange() {
        return buildField(position -> this.position.distanceTo(position), position);
    }
}
