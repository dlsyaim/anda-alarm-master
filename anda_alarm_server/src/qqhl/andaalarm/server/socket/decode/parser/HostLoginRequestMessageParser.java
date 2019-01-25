package qqhl.andaalarm.server.socket.decode.parser;

import io.netty.buffer.ByteBuf;
import qqhl.andaalarm.data.message.types.HostLoginRequestMessage;
import qqhl.andaalarm.data.message.types.Message;

import java.math.BigInteger;

/**
 * 当电话主机
 * @author hulang
 */
public class HostLoginRequestMessageParser extends AbstractMessageParser {
    public Message parse(ByteBuf byteBuf) {
        HostLoginRequestMessage msg = new HostLoginRequestMessage();
        msg.hostDeviceType = byteBuf.readUnsignedByte();
        msg.state = byteBuf.readUnsignedByte();
        byteBuf.skipBytes(16);
        // 读取主机设备id
        byte[] hostIdBytes = new byte[8];
        byteBuf.readBytes(hostIdBytes, 0, 8);
        msg.hostId = new BigInteger(hostIdBytes).toString(16).toUpperCase();

        return msg;
    }
}