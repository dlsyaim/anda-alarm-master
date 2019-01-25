package qqhl.andaalarm.server;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/**
 * @author hulang
 */
public class AlarmHTTPClient {
    private static PoolingClientConnectionManager connectionManager = null;
    public static HttpClient httpClient = null;
    static {
        connectionManager = new PoolingClientConnectionManager();
        connectionManager.setMaxTotal(1);
        httpClient = new DefaultHttpClient(connectionManager);
        httpClient.getParams().setParameter("http.socket.timeout",10000);
        httpClient.getParams().setParameter("http.connection.timeout",10000);
        httpClient.getParams().setParameter("http.connection-manager.timeout",100000000L);

    }
    private static AlarmHTTPClient instance = new AlarmHTTPClient();

    private AlarmHTTPClient() {}

    public static AlarmHTTPClient get() { return instance; }
}
