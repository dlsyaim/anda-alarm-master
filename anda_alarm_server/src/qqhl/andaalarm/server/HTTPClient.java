package qqhl.andaalarm.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
public class HTTPClient {
    private static HttpClient httpClient = HttpClients.createDefault();
    private static RequestConfig requestConfig;
    private static String forwardHttpUrl;

    static {
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000)
                .setConnectTimeout(2000).build();//设置请求和传输超时时间
        forwardHttpUrl = Config.getProperty("forward.http.url");
    }

    public static void doPost(Message message) throws Exception {
        if (!(message instanceof HostEventMessage)) {
            return;
        }
        HttpPost post = new HttpPost(forwardHttpUrl);
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
