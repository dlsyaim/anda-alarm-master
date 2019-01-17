package qqhl.andaalarm.server.websocket;

public class ClientSubscription {
    public int[] messageTypes;
    public int[] subTypes;

    public void setMessageTypes(String csv) {
        String[] ss = csv.split(",");
        fillInts(messageTypes = new int[ss.length], ss);
    }

    public void setSubTypes(String csv) {
        String[] ss = csv.split(",");
        fillInts(subTypes = new int[ss.length], ss);
    }

    private void fillInts(int[] arr, String[] ss) {
        int i = 0;
        for (String s : ss)
            arr[i++] = Integer.parseInt(s);
    }
}
