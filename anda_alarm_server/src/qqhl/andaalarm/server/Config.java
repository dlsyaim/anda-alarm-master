package qqhl.andaalarm.server;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author hulang
 */
public class Config {
    private static Properties props;
    static {
        props = new Properties();
        InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void print() {
        System.out.printf("config:\n  %s\n", props.toString());
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
}
