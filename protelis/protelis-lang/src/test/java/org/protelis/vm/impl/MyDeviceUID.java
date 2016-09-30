package org.protelis.vm.impl;

/**
 * {@link TestDeviceUID} implementation.
 */
public class MyDeviceUID implements TestDeviceUID {
    private static final long serialVersionUID = 1L;
    private final Integer id;

    /**
     * 
     * @param id
     *            device id
     */
    public MyDeviceUID(final Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    /**
     * @return return the UID as an {@link Integer}
     */
    public Integer getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(final Object obj) {
        return id == ((MyDeviceUID) obj).getId();
    }
}
