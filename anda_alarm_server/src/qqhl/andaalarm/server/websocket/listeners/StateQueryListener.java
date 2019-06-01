package qqhl.andaalarm.server.websocket.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import qqhl.andaalarm.server.websocket.WebSocketServer;

import java.util.Map;

public class StateQueryListener implements DataListener<String> {
    private WebSocketServer server;

    public StateQueryListener(WebSocketServer server) {
        this.server = server;
    }

    @Override
    public void onData(SocketIOClient client, String hostId, AckRequest ackRequest) {
        Map<String, Object> ret = server.serverContainer.alarmServer.queryHostState(hostId);
        client.sendEvent("state_query_result", ret);
    }
}
