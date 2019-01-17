package qqhl.andaalarm.server.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.apache.commons.lang3.ArrayUtils;
import qqhl.andaalarm.data.message.types.*;
import qqhl.andaalarm.server.ServerContainer;
import qqhl.andaalarm.server.socket.HostCommand;
import qqhl.andaalarm.server.websocket.listeners.HostCommandListener;
import qqhl.andaalarm.server.websocket.listeners.MessageSubscribeListener;


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
        if (message instanceof HostLoginRequestMessage) {
            return;
        }
        this.getAllClients().forEach(client -> {
            boolean send = false;
            ClientSubscription subscription = (ClientSubscription)client.get("subscription");
            if (subscription != null) {
                if (ArrayUtils.contains(subscription.messageTypes, message.type)) {
                    if (message instanceof HostEventMessage) {
                        if (ArrayUtils.contains(subscription.subTypes, ((HostEventMessage) message).eventType)) {
                            send = true;
                        }
                    } else {
                        send = true;
                    }
                }
            }

            if (send) {
                client.sendEvent("message", message);
            }
        });
    }
}

