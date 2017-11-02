package org.protelis.test.infrastructure;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.ExecutionEnvironment;
import org.protelis.vm.NetworkManager;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.alchemist.model.implementations.nodes.GenericNode;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Molecule;

/**
 */
public class ProtelisNode extends GenericNode<Object> implements DeviceUID, ExecutionEnvironment {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @SuppressFBWarnings(value = "SE_BAD_FIELD", justification = "This class is not meant to be serialized.")
    private NetworkManager netmgr;

    /**
     * Builds a new {@link ProtelisNode}.
     * 
     * @param env
     *            the environment
     */
    public ProtelisNode(final Environment<?> env) {
        super(env);
    }

    @Override
    protected Object createT() {
        return null;
    }

    @Override
    public String toString() {
        return Long.toString(getId());
    }

    private static Molecule makeMol(final String id) {
        return TestIncarnation.instance().createMolecule(id);
    }

    @Override
    public boolean has(final String id) {
        return contains(makeMol(id));
    }

    @Override
    public Object get(final String id) {
        return getConcentration(makeMol(id));
    }

    @Override
    public Object get(final String id, final Object defaultValue) {
        return Optional.ofNullable(get(id)).orElse(defaultValue);
    }

    @Override
    public boolean put(final String id, final Object v) {
        setConcentration(makeMol(id), v);
        return true;
    }

    @Override
    public Object remove(final String id) {
        final Object res = get(id);
        removeConcentration(makeMol(id));
        return res;
    }

    @Override
    public void commit() {
    }

    @Override
    public void setup() {
    }

    /**
     * 
     * @param netmgr
     *            network manager
     */
    public void setNetworkManger(final NetworkManager netmgr) {
        this.netmgr = netmgr;
    }

    /**
     * 
     * @return network manager
     */
    public NetworkManager getNetworkManager() {
        return netmgr;
    }

    @Override
    public Set<String> keySet() {
        Set<String> sSet = new HashSet<String>();
        // Note: this is highly inefficient
        for (Molecule key : getContents().keySet()) {
            sSet.add(key.getName());
        }
        return Collections.unmodifiableSet(sSet);
    }

}
