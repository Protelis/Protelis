/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.impl;

import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;

import java8.util.stream.IntStreams;

/**
 * A dummy Protelis VM to be used for testing.
 *
 */
public final class DummyContext extends SimpleContext {
    /**
     * Test utility.
     * 
     * @return a field with populated with numbers from 0 to 99
     */
    public static Field makeTestField() {
        final Field res = DatatypeFactory.createField(100);
        IntStreams.range(0, 100).forEach(n -> res.addSample(new DeviceUID() {
            private static final long serialVersionUID = 1L;
        }, (double) n));
        return res;
    }

}
