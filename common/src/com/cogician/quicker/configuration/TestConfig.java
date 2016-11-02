package com.cogician.quicker.configuration;

import com.cogician.quicker.Quicker;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-16T15:06:45+08:00
 * @since 0.0.0, 2016-08-16T15:06:45+08:00
 */
public class TestConfig {

    /**
     * <p>
     * 
     * </p>
     * 
     * @param args
     * @since 0.0.0
     */
    public static void main(String[] args) {
        QConfiguration config = new PropertiesConfigMap(Quicker.getResource("/com/cogician/quicker/config.properties"));
        config.forEach((k, v) -> {
            System.out.println("key=" + k + ", value=" + v.asString());
        });
    }

}
