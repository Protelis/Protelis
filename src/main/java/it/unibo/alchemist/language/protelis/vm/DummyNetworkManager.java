/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import it.unibo.alchemist.language.protelis.util.CodePath;

import java.util.Map;

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
