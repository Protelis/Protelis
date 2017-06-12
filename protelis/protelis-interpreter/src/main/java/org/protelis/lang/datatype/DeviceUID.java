/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype;

import java.io.Serializable;

/**
 * The DeviceUID class can be implemented however is appropriate for a given system.
 * What is important is just that no two devices should ever have the same UID.
 * If two devices do have the same UID, then the behavior of Protelis is undefined.
 * 
 * UIDs are often useful as tiebreakers, and many library functions use the
 * DeviceUID in this manner.  It is thus strongly RECOMMENDED that DeviceUID
 * implementations also implement Comparable.
 */
public interface DeviceUID extends Serializable {
}
