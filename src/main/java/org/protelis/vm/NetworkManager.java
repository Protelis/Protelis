package org.protelis.vm;

import java.util.Map;

import org.protelis.vm.util.CodePath;

import gnu.trove.map.TLongObjectMap;

public interface NetworkManager {

	TLongObjectMap<Map<CodePath, Object>> takeMessages();

	void sendMessage(Map<CodePath, Object> toSend);

}
