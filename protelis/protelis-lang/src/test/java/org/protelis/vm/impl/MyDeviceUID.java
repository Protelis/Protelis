package org.protelis.vm.impl;

import org.protelis.lang.datatype.DeviceUID;

public class MyDeviceUID implements DeviceUID {
    private static final long serialVersionUID = 1L;
    private long id;

    public MyDeviceUID(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + "";
    }

    public long getId() {
        return id;
    }
}
