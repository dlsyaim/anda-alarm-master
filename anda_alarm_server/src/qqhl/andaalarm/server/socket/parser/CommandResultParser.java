package qqhl.andaalarm.server.socket.parser;

import qqhl.andaalarm.data.message.types.Message;
import qqhl.andaalarm.data.message.types.CommandResult;

/**
 * @author hulang
 */
public class CommandResultParser extends AbstractMessageParser {
    public Message parse(int[] bytes) {
        CommandResult msg = new CommandResult();
        msg.cmdType = bytes[1];
        msg.resultCode = bytes[2];
        return msg;
    }
}
