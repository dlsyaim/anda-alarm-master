package qqhl.andaalarm.server.socket.parser;

import qqhl.andaalarm.data.message.types.Message;

import java.io.IOException;

/**
 * 消息解析器
 * @author hulang
 */
public class MessageParser {
    /* 解析消息字节 */
    public static Message parse(byte[] bytes) throws IOException {
        int type = bytes[0];
        AbstractMessageParser parser = factory(type);

        int[] unsignBytes = new int[bytes.length - 1];
        for (int i = 1; i < bytes.length; i++) {
            unsignBytes[i - 1] = bytes[i] & 0xFF;
        }
        Message message = parser.parse(unsignBytes);
        message.type = type;
        return message;
    }

    private static AbstractMessageParser factory(int type) {
        switch (type) {
            case 0x01:
                return new HostLoginRequestMessageParser();
            case 0x02:
                return new HostEventMessageParser();
            case 0X07:
                return new HostHeartbeatMessageParser();
            case 0x0a:
                return new CommandResultParser();
        }
        return null;
    }
}
