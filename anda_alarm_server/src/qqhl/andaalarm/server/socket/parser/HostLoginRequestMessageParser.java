package qqhl.andaalarm.server.socket.parser;

import qqhl.andaalarm.data.message.types.HostLoginRequestMessage;
import qqhl.andaalarm.data.message.types.Message;

import java.math.BigInteger;

/**
 * 当电话主机
 * @author hulang
 */
public class HostLoginRequestMessageParser extends AbstractMessageParser {
    public Message parse(int[] bytes) {
        HostLoginRequestMessage msg = new HostLoginRequestMessage();
        msg.hostDeviceType = bytes[0];
        msg.state = bytes[1];
        // 读取主机设备id
        byte[] hostIdBytes = new byte[8];
        for (int j = 18, i = 0; j < 26; j++, i++) {
            hostIdBytes[i] = (byte)bytes[j];
        }
        msg.hostId = new BigInteger(hostIdBytes).toString(16).toUpperCase();

        return msg;
    }
}
