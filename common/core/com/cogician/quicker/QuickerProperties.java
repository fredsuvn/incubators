package com.cogician.quicker;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.cogician.quicker.annotation.Nonpublic;
import com.cogician.quicker.configuration.QConfiguration;
import com.cogician.quicker.configuration.PropertiesConfigMap;
import com.cogician.quicker.log.QuickLogger;
import com.cogician.quicker.struct.QuickValue;

/**
 * <p>
 * Provides arguments from <a href = "config.properties">config.properties</a>.
 * </p>
 * <p>
 * This class is not opened in javadoc API, only used by quicker library and its developers, for providing arguments and
 * debugging. That is, the file config.properties should not be modified and this class should not be used by public
 * developer after releasing.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-11T14:42:10+08:00
 * @since 0.0.0, 2015-10-11T14:42:10+08:00
 */
@Nonpublic
public class QuickerProperties {

    private static final QConfiguration config;

    static {
        try {
            config = new PropertiesConfigMap(
                    Object.class.getResource("/com/cogician/quicker/config.properties").toURI());
        } catch (Throwable e) {
            throw new ReadException(e);
        }
        builtin();
    }

    private static void builtin() {
        config.set("quicker.domain", "quicker.cogician.com");
        config.set("quicker.founder", "孙谦 (Fred Suvn)");// \u5b59\u8c26
        config.set("quicker.contact.mobile", "+86 15251897368");
        config.set("quicker.contact.qq", "2510701977");
        config.set("quicker.egg", "");
    }

    /**
     * <p>
     * Returns all properties. Returned config map is read-only.
     * </p>
     * 
     * @return all properties
     * @since 0.0.0
     */
    public static QConfiguration getProperties() {
        return config.toReadOnly();
    }

    /**
     * <p>
     * Returns value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @return value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @since 0.0.0
     */
    public static QuickValue<?> get(String key) throws NullPointerException {
        return config.get(key);
    }

    public static final QuickLogger LOG = sysLogger();

    private static QuickLogger sysLogger() {
        String logOut = QuickerProperties.get("log.out").asString();
        String[] outs = logOut.split(",");
        List<QuickLogger> list = new LinkedList<>();
        QuickLogger.Builder builder = new QuickLogger.Builder().setLevel(config.getInt("log.level"))
                .setFormat(config.getString("log.format"));
        for (int i = 0; i < outs.length; i++) {
            String eachOut = outs[i].trim();
            OutputStream out = null;
            if ("sysout".equalsIgnoreCase(eachOut)) {
                out = System.out;
            } else if ("null".equalsIgnoreCase(eachOut)) {
                // out = null;
            } else {
                try {
                    out = new BufferedOutputStream(new FileOutputStream(eachOut, true));
                } catch (Throwable e) {
                    throw new ReadException(e);
                }
            }
            list.add(builder.setOut(out).build());
        }
        return QuickLogger.multiLogger(list);
    }
}
