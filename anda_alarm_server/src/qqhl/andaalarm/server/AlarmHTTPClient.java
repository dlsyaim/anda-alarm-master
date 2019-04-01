package qqhl.andaalarm.server;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/**
 * @author hulang
 */
public class AlarmHTTPClient {
    public static CloseableHttpClient httpClient = null;
    static {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(10000)
            .setConnectTimeout(10000)
            .setSocketTimeout(10000).build();
         httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }
    private static AlarmHTTPClient instance = new AlarmHTTPClient();

    private AlarmHTTPClient() {}

    public static AlarmHTTPClient get() { return instance; }
}
