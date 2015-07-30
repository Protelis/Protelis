package org.protelis.vm;

import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.util.CodePath;

public interface NetworkManager {

	Map<DeviceUID, Map<CodePath, Object>> takeMessages();

	void sendMessage(Map<CodePath, Object> toSend);

}
