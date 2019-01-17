package qqhl.andaalarm.server.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import qqhl.andaalarm.server.socket.parser.BitUtils;
import qqhl.andaalarm.server.socket.parser.MessageParser;
import qqhl.andaalarm.data.message.types.HostLoginRequestMessage;
import qqhl.andaalarm.data.message.types.Message;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    private AlarmServer alarmServer;

    public MessageDecoder(AlarmServer alarmServer) {
        this.alarmServer = alarmServer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf inByteBuf, List<Object> out) throws Exception {
        if (inByteBuf == null || inByteBuf.readableBytes() == 0)
            return;
        /*
        // 读消息头字节
        if (!((inByteBuf.readByte() & 0xFF) == 0xad && (inByteBuf.readByte() & 0xFF) == 0xcc)) {
            return;
        }*/
        inByteBuf.skipBytes(2);

        int length = BitUtils.merge(inByteBuf.readByte() & 0xFF, inByteBuf.readByte() & 0xFF);
        byte[] msgBytes = new byte[length];
        inByteBuf.readBytes(msgBytes);
        inByteBuf.skipBytes(2);

        Message message = MessageParser.parse(msgBytes);

        ChannelAttachment channelAttachment = alarmServer.channelAttachmentMap.get(ctx.channel());
        if (message instanceof HostLoginRequestMessage) {
            // 记录登录主机设备标识
            channelAttachment.hostId = ((HostLoginRequestMessage) message).hostId;
        }

        message.hostId = channelAttachment.hostId;

        out.add(message);
    }
}