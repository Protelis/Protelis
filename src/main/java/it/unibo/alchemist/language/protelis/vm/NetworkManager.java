package it.unibo.alchemist.language.protelis.vm;

import it.unibo.alchemist.language.protelis.util.CodePath;

import java.util.Map;

import gnu.trove.map.TLongObjectMap;

public interface NetworkManager {

	TLongObjectMap<Map<CodePath, Object>> takeMessages();

	void sendMessage(Map<CodePath, Object> toSend);

}
