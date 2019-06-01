package qqhl.andaalarm.server.socket;

import qqhl.andaalarm.data.message.types.HostHeartbeatMessage;

public class ChannelAttachment {
    public String hostId;
    public long latestHostHeartbeatTime;
    public HostHeartbeatMessage latestHostHeartbeatMessage;
    public boolean idle = false;
}
