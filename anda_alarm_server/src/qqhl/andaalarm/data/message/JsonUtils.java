package qqhl.andaalarm.data.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import qqhl.andaalarm.data.message.types.Message;

public class JsonUtils {
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public static String messageJson(Message message) {
        JsonObject o = (JsonObject)gson.toJsonTree(message);
        //解决父类属性
        o.addProperty("type", message.type);
        o.addProperty("hostId", message.hostId);
        return o.toString();
    }
}
