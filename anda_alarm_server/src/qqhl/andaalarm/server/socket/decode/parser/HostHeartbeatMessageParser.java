package qqhl.andaalarm.server.socket.decode.parser;

import io.netty.buffer.ByteBuf;
import qqhl.andaalarm.data.message.types.Message;
import qqhl.andaalarm.data.message.types.HostHeartbeatMessage;

import java.util.Arrays;

/**
 * @author hulang
 */
public class HostHeartbeatMessageParser extends AbstractMessageParser {
    public Message parse(ByteBuf byteBuf) {
        HostHeartbeatMessage msg = new HostHeartbeatMessage();
        msg.state = byteBuf.readUnsignedByte();
        msg.signalIntensity = byteBuf.readUnsignedByte();
        msg.supplyVoltage = byteBuf.readUnsignedShortLE();
        msg.outputVoltage = byteBuf.readUnsignedShortLE();
        msg.batteryVoltage = byteBuf.readUnsignedShortLE();
        msg.defenceState = byteBuf.readUnsignedShortLE();
        return msg;
    }
}
