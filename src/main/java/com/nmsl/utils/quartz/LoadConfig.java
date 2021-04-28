package com.nmsl.utils.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件
 * @author paracosm
 * @version 1.0
 * @date 2021/4/27 21:54
 */
public class LoadConfig {

    /**
     * 默认配置文件
     */
    private static final String DEFAULT_CONFIG_FILE = "/load.properties";

    private static Properties props;

    // TODO
    static {
        init();
    }

    private LoadConfig () {
        // should not call
    }

    private static void init() {
        props = loadConfig();
    }

    private static Properties loadConfig() {
        final Properties p = new Properties();
        loadDefaultConfig(p);
        return p;
    }

    private static void loadDefaultConfig(final Properties props) {
        final InputStream input = LoadConfig.class
                .getResourceAsStream(DEFAULT_CONFIG_FILE);
        if (input == null) {
            System.err.println("找不到默认的配置文件: "
                    + DEFAULT_CONFIG_FILE);
            System.exit(1);
        }
        try {
            props.load(input);
        } catch (final IOException e) {
            e.printStackTrace();
            System.err.println("加载默认配置文件错误: "
                    + e.getMessage());
            System.exit(1);
        } finally {
            try {
                input.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(final String key, final String defaultValue) {
        if (props == null) {
            init();
        }
        final String value = props.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return value.trim();
    }

    public static String getProperty(final String key) {
        return getProperty(key, "");
    }

    public static int getIntProperty(String key) {
        final String value = getProperty(key, "0");
        if (value.length() <= 0) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public static long getLongProperty(String key) {
        final String value = getProperty(key, "0");
        if (value.length() <= 0) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public static boolean getBooleanProperty(final String key) {
        final String value = getProperty(key, "false");
        if (value.length() <= 0) {
            return false;
        }
        return "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
    }
}
