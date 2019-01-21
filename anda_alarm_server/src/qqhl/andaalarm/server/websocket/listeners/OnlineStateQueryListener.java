package qqhl.andaalarm.server.websocket.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import qqhl.andaalarm.server.websocket.WebSocketServer;

public class OnlineStateQueryListener implements DataListener<String> {
    private WebSocketServer server;

    public OnlineStateQueryListener(WebSocketServer server) {
        this.server = server;
    }

    @Override
    public void onData(SocketIOClient client, String hostId, AckRequest ackRequest) throws Exception {
        System.out.println(hostId);
        int ret = server.serverContainer.alarmServer.queryHostOnlineState(hostId);
        client.sendEvent("onlineState", ret);
    }
}
