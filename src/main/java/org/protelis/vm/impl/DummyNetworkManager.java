/**
 * 
 */
package org.protelis.vm.impl;

import java.util.Collections;
import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.util.CodePath;

/**
 * @author Danilo Pianini
 *
 */
public class DummyNetworkManager implements NetworkManager {

	@Override
	public Map<DeviceUID, Map<CodePath, Object>> takeMessages() {
		return Collections.emptyMap();
	}

	@Override
	public void sendMessage(final Map<CodePath, Object> toSend) {
	}

}
