package qqhl.andaalarm.server.socket.parser;

import qqhl.andaalarm.data.message.types.Message;
import qqhl.andaalarm.data.message.types.HostHeartbeatMessage;

/**
 * @author hulang
 */
public class HostHeartbeatMessageParser extends AbstractMessageParser {
    public Message parse(int[] bytes) {
        HostHeartbeatMessage msg = new HostHeartbeatMessage();
        msg.state = bytes[0];
        msg.signalIntensity = bytes[1];
        msg.supplyVoltage = BitUtils.merge(bytes[2], bytes[3]);
        msg.outputVoltage = BitUtils.merge(bytes[4], bytes[5]);
        msg.batteryVoltage = BitUtils.merge(bytes[6], bytes[7]);
        msg.defenceState =BitUtils.merge(bytes[8], bytes[9]);
        return msg;
    }
}
