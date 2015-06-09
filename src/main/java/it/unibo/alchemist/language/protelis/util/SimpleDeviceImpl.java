/**
 * 
 */
package it.unibo.alchemist.language.protelis.util;

/**
 * @author Danilo Pianini
 *
 */
public class SimpleDeviceImpl implements DeviceUID {

	private static final long serialVersionUID = -6848816124562370071L;
	private final long id;
	
	/**
	 * @param localId the id of the {@link DeviceUID}
	 */
	public SimpleDeviceImpl(final long localId) {
		id = localId;
	}
	@Override
	public long getId() {
		return id;
	}

}
