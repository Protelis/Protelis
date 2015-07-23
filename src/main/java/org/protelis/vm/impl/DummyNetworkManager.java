/**
 * 
 */
package org.protelis.vm.impl;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.Map;

import org.protelis.vm.NetworkManager;
import org.protelis.vm.util.CodePath;

/**
 * @author Danilo Pianini
 *
 */
public class DummyNetworkManager implements NetworkManager {

	@Override
	public TLongObjectMap<Map<CodePath, Object>> takeMessages() {
		return new TLongObjectHashMap<>();
	}

	@Override
	public void sendMessage(final Map<CodePath, Object> toSend) {
	}

}
