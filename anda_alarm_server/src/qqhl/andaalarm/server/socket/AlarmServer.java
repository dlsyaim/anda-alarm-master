package qqhl.andaalarm.server.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import qqhl.andaalarm.server.Config;
import qqhl.andaalarm.server.ServerContainer;
import qqhl.andaalarm.server.socket.decode.MessageDecoder;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AlarmServer {
    ServerContainer serverContainer;
    public boolean isRunning = false;
    private int port;

    Map<Channel, ChannelAttachment> channelAttachmentMap = new HashMap<>();
    Map<String, Channel> hostChannelMap = new HashMap<>();

    public AlarmServer(int port, ServerContainer serverContainer) {
        this.serverContainer = serverContainer;
        this.port = port;
    }

    public void start() throws Exception {
        isRunning = true;

        ServerBootstrap boot = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        final int idleTime = Config.getInt("host.heartbeat_timeout_ms");
        try {
            boot.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                            // 实现心跳检查
                            .addLast(new IdleStateHandler(idleTime, idleTime, idleTime, TimeUnit.MILLISECONDS))
                            // 解码消息
                            .addLast(new MessageDecoder())
                            // 处理连接和消息
                            .addLast(new MessageHandler(AlarmServer.this))
                            // 编码消息
                            .addLast(new MessageEncoder());
                        }
                    });
            ChannelFuture future = boot.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public Map<String, Object> queryHostState(String hostId) {
        Map<String, Object> ret = new HashMap<>();
        Channel channel =  hostChannelMap.get(hostId);
        ret.put("online", channel != null ? 1 : 0);
        if (channel != null) {
            ChannelAttachment attr = channelAttachmentMap.get(channel);
            if (attr != null) {
                ret.put("latestHeartbeatMessage", attr.latestHostHeartbeatMessage);
                ret.put("latestHeartbeatTime", attr.latestHostHeartbeatTime);
            }
        }

        return ret;
    }

    public int sendCommand(HostCommand hostCommand) {
        Channel channel = hostChannelMap.get(hostCommand.hostId);
        if (! (channel != null && channel.isOpen())) {
            return 1;
        }
        try {
            channel.writeAndFlush(Unpooled.copiedBuffer(new byte[]{0X11, (byte) hostCommand.cmdType}));
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }
}
