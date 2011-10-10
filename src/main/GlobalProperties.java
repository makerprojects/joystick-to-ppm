package main;

import main.GUI.DeviceConfigPanel;
import main.GUI.controller.ComponentConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @author Alexandr Vorobiev
 */
public class GlobalProperties {
    private static Properties properties;
    public static Properties getProperties() {
        if (properties == null)
            properties = new Properties();
        return properties;
    }

    public static Integer checkChannelForComponent(ComponentConfig componentConfig) {
        Integer v;
        try {
         v = Integer.valueOf(properties.getProperty("DC","0"));
        } catch (Exception e) {
            return null;
        }
        for (int i = 1; i <= v; i++) {
            if (properties.containsKey("DEVICE" + i) && properties.containsKey("DCHANNEL" + i)) {
                if (componentConfig.toString().equals(properties.getProperty("DEVICE" + i))) {
                    try {
                     v = Integer.valueOf(properties.getProperty("DCHANNEL" + i,"1"));
                    } catch (Exception e) {
                        return null;
                    }
                    return v;
                }
            }
        }
        return null;
    }

    public static Integer checkChannelSVForComponent(ComponentConfig componentConfig) {
        Integer v;
        try {
         v = Integer.valueOf(properties.getProperty("DC","0"));
        } catch (Exception e) {
            return null;
        }
        for (int i = 1; i <= v; i++) {
            if (properties.containsKey("DEVICE" + i) && properties.containsKey("CHANNEL_SV" + i)) {
                if (componentConfig.toString().equals(properties.getProperty("DEVICE" + i))) {
                    try {
                     v = Integer.valueOf(properties.getProperty("CHANNEL_SV" + i,"50"));
                    } catch (Exception e) {
                        return null;
                    }
                    return v;
                }
            }
        }
        return null;
    }
    public static void loadProperties() {
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream("application.properties");
            props.load(in);
            in.close();
            properties = props;
        } catch (Exception ignore) {}
    }
    public static void saveProperties() {
        try {
            FileOutputStream out = new FileOutputStream("application.properties");
            properties.store(out, "---No Comment---");
            out.close();
        } catch (Exception ignore) {}
    }

}
