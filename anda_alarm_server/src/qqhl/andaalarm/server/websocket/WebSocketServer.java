package qqhl.andaalarm.server.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import qqhl.andaalarm.data.message.types.*;
import qqhl.andaalarm.server.ChannelQuery;
import qqhl.andaalarm.server.ServerContainer;
import qqhl.andaalarm.server.socket.HostCommand;
import qqhl.andaalarm.server.websocket.listeners.HostCommandListener;
import qqhl.andaalarm.server.websocket.listeners.MessageSubscribeListener;
import qqhl.andaalarm.server.websocket.listeners.StateQueryListener;


/**
 * web socket server
 * @author hulang
 */
public class WebSocketServer extends SocketIOServer {
    private boolean isRunning;
    private boolean inited = false;
    public ServerContainer serverContainer;

    public WebSocketServer(Configuration config, ServerContainer serverContainer) {
        super(config);
        this.serverContainer = serverContainer;
    }

    public void start() {
        if (isRunning)
            return;
        if (!inited) {
            inited = true;
        }
        super.start();
        this.addEventListener("stateQuery", String.class, new StateQueryListener(this));
        this.addEventListener("hostCommand", HostCommand.class, new HostCommandListener(this));
        this.addEventListener("messageSubscribe", ClientSubscription.class, new MessageSubscribeListener(this));
        isRunning = true;
    }

    public void stop() {
        if (!isRunning)
            return;
        super.stop();
        isRunning = false;
    }

    public void sendMessageToClients(Message message) {
        String channel = null;
        for (SocketIOClient client : this.getAllClients()) {
            boolean send = false;
            ClientSubscription subscription = client.get("subscription");
            if (subscription != null) {
                if (StringUtils.isNotEmpty(subscription.getHostId())) {
                    if (message.getHostId().equals(subscription.getHostId())) {
                        send = true;
                    }
                } else if (StringUtils.isNotEmpty(subscription.getChannel())) {
                    if (subscription.getChannel().equals("0")) {
                        send = true;
                    } else {
                        if (channel == null)
                            channel = ChannelQuery.getChannelByHostId(message.getHostId());
                        if (channel != null && channel.equals(subscription.getChannel())) {
                            send = true;
                        }
                    }
                }

                if (send) {
                    send = false;
                    if (ArrayUtils.contains(subscription.getMessageTypes(), message.type)) {
                        if (message instanceof HostEventMessage) {
                            if (ArrayUtils.contains(subscription.getSubTypes(), ((HostEventMessage) message).eventType)) {
                                send = true;
                            }
                        } else {
                            send = true;
                        }
                    }
                }

            }

            if (send) {
                client.sendEvent("message", message);
            }
        }
    }
}

