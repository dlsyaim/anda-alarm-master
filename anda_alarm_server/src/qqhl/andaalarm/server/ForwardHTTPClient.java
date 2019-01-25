package qqhl.andaalarm.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import qqhl.andaalarm.data.message.JsonUtils;
import qqhl.andaalarm.data.message.types.HostEventMessage;
import qqhl.andaalarm.data.message.types.Message;

/**
 * @author hulang
 */
public class ForwardHTTPClient  {
    private static HttpClient httpClient = AlarmHTTPClient.get().httpClient;
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
            HttpResponse resp = httpClient.execute(post);
            resp.getEntity().getContent().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
