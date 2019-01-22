package qqhl.andaalarm.server.socket.decode.parser;

import io.netty.buffer.ByteBuf;
import qqhl.andaalarm.data.message.types.Message;

/**
 * 抽象报警消息解析器，将字节解释成信息
 * @author hulang
 */
public abstract class AbstractMessageParser {
    abstract Message parse(ByteBuf byteBuf);
}
