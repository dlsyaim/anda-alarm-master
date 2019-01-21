package qqhl.andaalarm.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import qqhl.andaalarm.data.message.JsonUtils;
import qqhl.andaalarm.data.message.types.HostEventMessage;
import qqhl.andaalarm.data.message.types.Message;

/**
 * @author hulang
 */
public class ForwardHTTPClient {
    private static HttpClient httpClient = HttpClients.createDefault();
    private static RequestConfig requestConfig;
    static {
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000)
                .setConnectTimeout(2000).build();//设置请求和传输超时时间
    }
    private static String forwardUrl = Config.getProperty("forward.url");

    public static void doPost(Message message) throws Exception {
        if (!(message instanceof HostEventMessage)) {
            return;
        }
        HttpPost post = new HttpPost(forwardUrl);
        post.setEntity(EntityBuilder.create()
            .setContentEncoding("utf-8")
            .setContentType(ContentType.APPLICATION_JSON)
            .setText(JsonUtils.messageJson(message)).build());
        try {
            post.setConfig(requestConfig);
            HttpResponse resp = httpClient.execute(post);
            resp.getEntity().getContent().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
