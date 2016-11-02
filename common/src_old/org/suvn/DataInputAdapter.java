package com.cogician.quicker;

import java.util.stream.Stream;

import com.cogician.quicker.util.reflect.ReflectionException;

/**
 * This class is an adapter to read data as a data block from given type, the
 * type may be an input stream or other object which can read data.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 14:05:28
 * @since 0.0.0
 */
public interface DataInputAdapter<T> extends Adapter<T> {
    /**
     * Reads data of special length in bits as a data block, the actual read
     * block may be less than special length. The max length in bits can be read
     * is {@linkplain Integer#MAX_VALUE} bytes.
     *
     * @param lengthInBits
     *            length in bits, positive
     * @return data of special length in bits as a data block, not null
     * @throws IllegalArgumentException
     *             thrown if length in bits is 0, negative or more than
     *             {@linkplain Integer#MAX_VALUE} bytes
     * @throws ReflectionException
     *             thrown if a data error occurs
     */
    public Bitsss read(long lengthInBits);

    /**
     * Reads data into special data block, the actual read data may less than
     * length of special block, the actual read data's length in bits returned.
     *
     * @param data
     *            special data block, not null
     * @return the actual read data's length in bits
     * @throws NullPointerException
     *             thrown if special block is null
     * @throws ReflectionException
     *             thrown if a data error occurs
     */
    public long read(Bitsss data);

    /**
     * Returns a {@linkplain Stream} with {@link Bitsss} of special length in
     * bits, the stream will output data block until end of available data or an
     * error occurs. The max length in bits can be split as an element of stream
     * is {@linkplain Integer#MAX_VALUE} bytes.
     *
     * @param lengthInBits
     *            special length in bits, positive
     * @return a {@linkplain Stream} with {@link Bitsss} of special length in
     *         bits, not null
     * @throws IllegalArgumentException
     *             thrown if length in bits is 0, negative or more than
     * @throws ReflectionException
     *             thrown if a data error occurs
     */
    public Stream<Bitsss> stream(long lengthInBits);

}
