package qqhl.andaalarm.server.socket.parser;

import qqhl.andaalarm.data.message.types.HostEventMessage;
import qqhl.andaalarm.data.message.types.Message;

import java.util.Calendar;

/**
 * @author hulang
 */
public class HostEventMessageParser extends AbstractMessageParser {
    public Message parse(int[] bytes) {
        HostEventMessage msg = new HostEventMessage();
        msg.state = bytes[0];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, bytes[1]);
        cal.set(Calendar.MINUTE, bytes[2]);
        cal.set(Calendar.HOUR_OF_DAY, bytes[3]);
        cal.set(Calendar.DATE, bytes[4]);
        cal.set(Calendar.MONTH, bytes[5] - 1);
        cal.set(Calendar.YEAR, bytes[7] * 100 + bytes[6]);
        msg.datetime = cal.getTime();

        msg.eventType = BitUtils.merge(bytes[9], bytes[8]);//big-endian
        msg.operator = bytes[10];
        msg.defenceArea = BitUtils.merge(bytes[11], bytes[12]);
        return msg;
    }
}
