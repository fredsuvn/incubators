package com.cogician.quicker.binary;

/**
 * <p>
 * This interface indicates the instance is byte oder sensitive.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-22T14:30:02+08:00
 * @since 0.0.0, 2016-08-22T14:30:02+08:00
 */
public interface ByteOrderSensitive {

    /**
     * <p>
     * Returns byte order processor of this inputer.
     * </p>
     * 
     * @return byte order processor of this inputer
     * @since 0.0.0
     */
    public ByteOrderProcessor getByteOrderProcessor();
}
