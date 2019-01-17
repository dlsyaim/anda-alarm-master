package qqhl.andaalarm.server.socket.parser;

/**
 * @author hulang
 */
public class BitUtils {
    /**
     * @param low 作为结果中的低字节
     * @param high 作为结果中的高字节
     */
    public static int merge(int low, int high) {
        return (high << 8) | low;
    }

    /*
     * @param bytes 字节数组（最大长度8），高字节索引小，低字节索引大
     */
    public static long mergeToLong(int[] bytes, int from, int to) {
        long ret = 0;
        int shift = 8 * (to - from - 1);
        for (int i = from; i < to; i++) {
            ret = ret | (long)bytes[i] << shift;
            shift -= 8;
        }
        return ret;
    }
}
