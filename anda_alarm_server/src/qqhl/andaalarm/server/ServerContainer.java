package qqhl.andaalarm.server;

import com.corundumstudio.socketio.Configuration;
import qqhl.andaalarm.server.socket.AlarmServer;
import qqhl.andaalarm.server.websocket.WebSocketServer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class ServerContainer {
    public AlarmServer alarmServer;
    public WebSocketServer webSocketServer;

    public void init() throws Exception {
        System.out.println();
        Config.print();
        System.out.println("server started.");

        Configuration config = new Configuration();
        config.setHostname(getLocalIP());
        config.setPort(Config.getInt("webSocketServer.port"));
        webSocketServer = new WebSocketServer(config, this);
        webSocketServer.start();

        alarmServer = new AlarmServer(Config.getInt("server.port"),this);
        alarmServer.start();
    }

    public static void main(String[] args) throws Exception {
        ServerContainer serverContainer = new ServerContainer();
        serverContainer.init();
    }

    private static String getLocalIP() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        if (isWindowsOS) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            String ip = "";
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                    NetworkInterface intf = en.nextElement();
                    String name = intf.getName();
                    if (!name.contains("docker") && !name.contains("lo")) {
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress()) {
                                String ipaddress = inetAddress.getHostAddress().toString();
                                if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                    ip = ipaddress;
                                }
                            }
                        }
                    }
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
            return ip;
        }
    }
}
