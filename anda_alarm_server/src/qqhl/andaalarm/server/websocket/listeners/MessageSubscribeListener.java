package qqhl.andaalarm.server.websocket.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import qqhl.andaalarm.server.websocket.ClientSubscription;
import qqhl.andaalarm.server.websocket.WebSocketServer;


public class MessageSubscribeListener implements DataListener<ClientSubscription> {
    private WebSocketServer server;

    public MessageSubscribeListener(WebSocketServer server) {
        this.server = server;
    }

    @Override
    public void onData(SocketIOClient client, ClientSubscription subscription, AckRequest ackRequest) {
        client.set("subscription", subscription);
    }
}
