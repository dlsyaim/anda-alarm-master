package qqhl.andaalarm.server.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import qqhl.andaalarm.server.Config;
import qqhl.andaalarm.server.ServerContainer;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AlarmServer {
    ServerContainer serverContainer;
    public boolean isRunning = false;
    private int port;

    Map<Channel, ChannelAttachment> channelAttachmentMap = new HashMap<Channel, ChannelAttachment>();
    Map<String, Channel> hostChannelMap = new HashMap<String, Channel>();

    public AlarmServer(int port, ServerContainer serverContainer) {
        this.serverContainer = serverContainer;
        this.port = port;
    }

    public void start() throws Exception {
        isRunning = true;

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        int idleTime = Config.getInt("host.heartbeat_timeout_ms");
                        socketChannel.pipeline()
                                // 实现心跳检查事件
                                .addLast(new IdleStateHandler(idleTime, idleTime, idleTime, TimeUnit.MILLISECONDS))
                                // 解码消息
                                .addLast(new MessageDecoder(AlarmServer.this))
                                // 处理连接和消息
                                .addLast(new MessageHandler(AlarmServer.this));
                    }
                }).bind(port);
    }

    public int sendCommand(HostCommand hostCommand) {
        Channel channel = hostChannelMap.get(hostCommand.hostId);
        if (! (channel != null && channel.isOpen())) {
            return 1;
        }
        try {
            channel.write(Unpooled.copiedBuffer(new byte[]{ 0X11, (byte) hostCommand.cmdType }));
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }
}
