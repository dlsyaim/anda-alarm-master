package qqhl.andaalarm.server.socket;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import qqhl.andaalarm.data.message.types.HostLoginRequestMessage;
import qqhl.andaalarm.data.message.types.IdleStateEventMessage;
import qqhl.andaalarm.data.message.types.Message;
import qqhl.andaalarm.server.ForwardHTTPClient;

import java.util.Calendar;
import java.util.Date;


public class MessageHandler extends ChannelInboundHandlerAdapter {
    private AlarmServer alarmServer;
    private final byte[] NORMAL_RESPONSE = new byte[]{(byte)0xad, (byte)0x99, (byte)0x0d, (byte)0x0a};

    public MessageHandler(AlarmServer alarmServer) {
        this.alarmServer = alarmServer;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event =(IdleStateEvent)evt;
        switch (event.state()) {
            case READER_IDLE:
                // 如果登录之后心跳超时
                // 转发这个事件消息
                forwardIdleStateEventMessage(ctx.channel());
                // 主动关闭连接
                ctx.channel().close();//触发channelUnregistered
                break;
            case WRITER_IDLE:
            case ALL_IDLE:
                break;
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        ChannelAttachment state = new ChannelAttachment();
        state.idle = false;
        alarmServer.channelAttachmentMap.put(ctx.channel(), state);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ChannelAttachment attr = alarmServer.channelAttachmentMap.get(channel);
        alarmServer.hostChannelMap.remove(attr.hostId);
        alarmServer.channelAttachmentMap.remove(channel);
        forwardIdleStateEventMessage(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        Message message = (Message)msg;
        doResponse(message, channel);
        forwardMessage(message);
    }

    private void forwardMessage(Message message) throws Exception {
        ForwardHTTPClient.doPost(message);
        alarmServer.serverContainer.webSocketServer.sendMessageToClients(message);
    }

    private void forwardIdleStateEventMessage(Channel channel) throws Exception {
        ChannelAttachment state = alarmServer.channelAttachmentMap.get(channel);
        if (state == null || state.idle) {
            return;
        }
        state.idle = true;
        IdleStateEventMessage msg = new IdleStateEventMessage();
        msg.hostId = state.hostId;
        msg.datetime = new Date();
        forwardMessage(msg);
    }

    /* 应答 */
    private void doResponse(Message message, Channel channel) {
        byte[] reponseBytes;
        if (message instanceof HostLoginRequestMessage) {
            alarmServer.hostChannelMap.put(message.hostId, channel);
            // 告诉主机它登录成功了
            Calendar now = Calendar.getInstance();
            int y = now.get(Calendar.YEAR);
            reponseBytes = new byte[]{
                    (byte) 0xAD, (byte) 0xCC, 0x09, 0x00, 0x06, (byte) 0x99,
                    (byte) now.get(Calendar.SECOND),
                    (byte) now.get(Calendar.MINUTE),
                    (byte) now.get(Calendar.HOUR_OF_DAY),
                    (byte) now.get(Calendar.DATE),
                    (byte) (now.get(Calendar.MONTH) + 1),
                    (byte) (y % 100),
                    (byte) (y / 100),
                    0x0D, 0x0A, 0x0D, 0x0A
            };
        } else {
            reponseBytes = NORMAL_RESPONSE;
        }
        channel.writeAndFlush(Unpooled.copiedBuffer(reponseBytes));
    }

}
