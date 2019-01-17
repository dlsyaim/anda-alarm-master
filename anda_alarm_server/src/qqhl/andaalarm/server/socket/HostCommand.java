package qqhl.andaalarm.server.socket;

import lombok.Data;

@Data
public class HostCommand {
    public String hostId;
    public int cmdType;
}
