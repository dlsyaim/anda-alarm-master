package qqhl.andaalarm.server.websocket.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import qqhl.andaalarm.server.socket.HostCommand;
import qqhl.andaalarm.server.websocket.WebSocketServer;

public class HostCommandListener implements DataListener<HostCommand> {
    private WebSocketServer server;

    public HostCommandListener(WebSocketServer server) {
        this.server = server;
    }

    @Override
    public void onData(SocketIOClient client, HostCommand hostCommand, AckRequest ackRequest) throws Exception {
        int ret = server.serverContainer.alarmServer.sendCommand(hostCommand);
        client.sendEvent("commandReqRet", ret);
    }
}
