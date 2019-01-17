package qqhl.andaalarm.data.message;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import qqhl.andaalarm.data.message.types.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author hulang
 */
public class MapToMessage {
    public static Message toMessage(Map<String, Object> mapMessage) {
        int type = (Integer)mapMessage.get("type");
        Message message = newMessageByType(type);
        try {
            BeanUtils.populate(message, mapMessage);
        } catch (Exception e) {
            message = null;
        }
        return message;
    }

    private static Message newMessageByType(int type) {
        switch (type) {
            case 0x01:
                return new HostLoginRequestMessage();
            case 0x02:
                return new HostEventMessage();
            case 0X07:
                return new HostHeartbeatMessage();
            case 0x0a:
                return new CommandResult();
        }
        return null;
    }

    static {
        ConvertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return simpleDateFormat.parse(value.toString());
                } catch (Exception e) { return null; }
            }
        }, Date.class);
    }
}
