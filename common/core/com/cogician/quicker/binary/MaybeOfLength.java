package com.cogician.quicker.binary;

import java.util.function.LongSupplier;

import com.cogician.quicker.Quicker;

/**
 * <p>
 * This interface represent an in/out-put stream maybe of length. For example, A file input or buffer stream has a clear
 * length, but a socket stream has not.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-24T13:48:46+08:00
 * @since 0.0.0, 2016-08-24T13:48:46+08:00
 */
public interface MaybeOfLength {

    /**
     * <p>
     * Returns whether this in/out-put has reached to the end and never can be read/write. This method is valid if
     * length of this in/out-put can be estimated by {@linkplain #length()}; otherwise, it always return false.
     * </p>
     * 
     * @return whether this in/out-put has reached to the end and never can be read/write
     * @since 0.0.0
     */
    default boolean end() {
        return remainderLength() == 0;
    }

    /**
     * <p>
     * Returns length of this in/out-put in bytes if this stream is of length, or -1 if its length can not be estimated.
     * </p>
     * 
     * @return length of this in/out-put
     * @since 0.0.0
     */
    public long length();

    /**
     * <p>
     * Returns remainder length of this in/out-put between current position in bytes and end if this stream is of
     * length, or -1 if its length can not be estimated.
     * </p>
     * 
     * @return remainder length of this in/out-put
     * @since 0.0.0
     */
    public long remainderLength();

    /**
     * <p>
     * A position pointer for a stream.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-08-29T13:51:59+08:00
     * @since 0.0.0, 2016-08-29T13:51:59+08:00
     */
    public static interface StreamPointer extends MaybeOfLength {

        /**
         * <p>
         * Returns a stream pointer with specified length. If length is negative, return an unlimited pointer.
         * </p>
         * 
         * @param length
         *            specified length
         * @return a stream pointer with specified length
         * @since 0.0.0
         */
        public static StreamPointer ofLength(long length) {
            return length < 0 ? unlimited() : new LimitedStreamPointer(length);
        }

        /**
         * <p>
         * Returns a stream pointer with specified length supplier.
         * </p>
         * 
         * @param lengthSupplier
         *            specified length supplier
         * @return a stream pointer with specified length supplier
         * @throws NullPointerException
         *             if specified length supplier is null
         * @since 0.0.0
         */
        public static StreamPointer ofLength(LongSupplier lengthSupplier) throws NullPointerException {
            return new VariableStreamPointer(lengthSupplier);
        }

        /**
         * <p>
         * Returns a stream pointer of which length is unlimited.
         * </p>
         * 
         * @return a stream pointer of which length is unlimited
         * @since 0.0.0
         */
        public static StreamPointer unlimited() {
            return UnlimitedStreamPointer.SINGLETON;
        }

        /**
         * <p>
         * Returns current read/write position of this stream. Return -1 if this stream is unlimited.
         * </p>
         * 
         * @return current read/write position of this stream
         * @since 0.0.0
         */
        public long currentPosition();

        /**
         * <p>
         * Increase position of this stream.
         * </p>
         * 
         * @param offset
         *            offset of increasing
         * @since 0.0.0
         */
        public void increase(int offset);
    }

    /**
     * <p>
     * A {@linkplain StreamPointer} of witch length is limited.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-08-29T13:58:06+08:00
     * @since 0.0.0, 2016-08-29T13:58:06+08:00
     */
    public static class LimitedStreamPointer implements StreamPointer {

        private final long length;

        private long current = 0;

        /**
         * <p>
         * Constructs with specified length.
         * </p>
         * 
         * @param length
         *            specified length
         * @throws IllegalArgumentException
         *             if specified length is negative
         * @since 0.0.0
         */
        public LimitedStreamPointer(long length) throws IllegalArgumentException {
            this.length = length;
        }

        @Override
        public long currentPosition() {
            return current;
        }

        @Override
        public void increase(int offset) {
            current += offset;
        }

        @Override
        public long length() {
            return length;
        }

        @Override
        public long remainderLength() {
            return length - current;
        }
    }

    /**
     * <p>
     * A {@linkplain StreamPointer} of witch length is unlimited.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-08-29T13:58:53+08:00
     * @since 0.0.0, 2016-08-29T13:58:53+08:00
     */
    public static class UnlimitedStreamPointer implements StreamPointer {

        public static UnlimitedStreamPointer SINGLETON = new UnlimitedStreamPointer();

        private UnlimitedStreamPointer() {
        }

        @Override
        public long currentPosition() {
            return -1;
        }

        @Override
        public void increase(int offset) {

        }

        @Override
        public long length() {
            return -1;
        }

        @Override
        public long remainderLength() {
            return -1;
        }
    }

    /**
     * <p>
     * A {@linkplain StreamPointer} of witch length is variable.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-08-31T16:41:52+08:00
     * @since 0.0.0, 2016-08-31T16:41:52+08:00
     */
    public static class VariableStreamPointer implements StreamPointer {

        private long current = 0;

        private final LongSupplier lengthSupplier;

        /**
         * <p>
         * Constructs with specified variable length supplier.
         * </p>
         * 
         * @param lengthSupplier
         *            specified variable length supplier
         * @throws NullPointerException
         *             if specified variable length supplier is null
         * @since 0.0.0
         */
        public VariableStreamPointer(LongSupplier lengthSupplier) throws NullPointerException {
            this.lengthSupplier = Quicker.require(lengthSupplier);
        }

        @Override
        public long currentPosition() {
            return current;
        }

        @Override
        public void increase(int offset) {
            current += offset;
        }

        @Override
        public long length() {
            return lengthSupplier.getAsLong();
        }

        @Override
        public long remainderLength() {
            long remainder = length() - current;
            return remainder >= 0 ? remainder : 0;
        }
    }
}
