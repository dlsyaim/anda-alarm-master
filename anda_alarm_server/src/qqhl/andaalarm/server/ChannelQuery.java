package qqhl.andaalarm.server;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import qqhl.andaalarm.data.message.types.HostHeartbeatMessage;
import qqhl.andaalarm.data.message.types.Message;

import java.io.InputStream;

public class ChannelQuery {
    private static HttpClient httpClient = AlarmHTTPClient.get().httpClient;
    private static String channelQueryUrl = Config.getProperty("channelQuery.url") + "?hostId=";

    public static String getChannelByHostId(String hostId) {
        HttpGet get = new HttpGet(channelQueryUrl + hostId);
        try {
            HttpResponse resp = httpClient.execute(get);
            InputStream in = resp.getEntity().getContent();
            String channel = IOUtils.toString(in, "UTF-8");
            in.close();
            return channel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
