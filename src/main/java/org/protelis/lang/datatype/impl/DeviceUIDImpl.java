/**
 * 
 */
package org.protelis.lang.datatype.impl;

import org.danilopianini.lang.HashUtils;
import org.protelis.lang.datatype.DeviceUID;

/**
 * @author Danilo Pianini
 *
 */
public class DeviceUIDImpl implements DeviceUID {

	private static final long serialVersionUID = -6848816124562370071L;
	private final long id;
	private transient String string;
	private transient int hash;
	
	/**
	 * @param localId the id of the {@link DeviceUID}
	 */
	public DeviceUIDImpl(final long localId) {
		id = localId;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		if (string == null) {
			string = Long.toString(id);
		}
		return string;
	}
	
	@Override
	public boolean equals(final Object obj) {
		return obj instanceof DeviceUID && id == ((DeviceUID) obj).getId();
	}
	
	@Override
	public int hashCode() {
		if (hash == 0) {
			hash = HashUtils.hash32(id);
		}
		return hash;
	}

}
