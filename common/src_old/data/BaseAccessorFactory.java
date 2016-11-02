package com.cogician.quicker.binary.data;

import com.cogician.quicker.Bitsss;
import com.cogician.quicker.annotation.Base;
import com.cogician.quicker.binary.Bits;

/**
 * <p>
 * This interface represents a factory of {@linkplain Bits}.
 * </p>
 * <p>
 * This is a {@linkplain Base} interface, the parameters of methods of this
 * interface will not be checked.
 * </p>
 * <p>
 * This interface should not be used directly as a common public API, but only
 * called for creating {@linkplain Bitsss}.
 * </p>
 * <p>
 * <b>Note</b> the parameters of methods of this interface will not be checked.
 * Methods will throw exceptions or return wrong results if given parameters
 * don't meet the requirements. The details of exceptions and wrong results
 * depend on implementations.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-05-11 09:57:19
 * @since 0.0.0
 * @see Bits
 * @see Bitsss
 */
@Base
public interface BaseAccessorFactory {

    /**
     * <p>
     * Creates with given length in bits.
     * </p>
     *
     * @param lengthInbits
     *            length in bits, [1, Integer.MAX_VALUE * 64L]
     * @return instance of given length in bits
     * @since 0.0.0
     */
    public Bits create(long lengthInbits);

    /**
     * <p>
     * Creates with given length in bytes.
     * </p>
     *
     * @param lengthInByte
     *            length in bytes, [1, Integer.MAX_VALUE]
     * @return instance of given length in bytes
     * @since 0.0.0
     */
    public Bits create(int lengthInByte);

    /**
     * <p>
     * Wraps part of given array to be a base accessor.
     * </p>
     *
     * @param array
     *            given array, not null
     * @param startIndex
     *            start index inclusive in bounds
     * @param endIndex
     *            end index exclusive in bounds
     * @return instance after wrapping
     * @since 0.0.0
     */
    public Bits wrap(boolean[] array, int startIndex, int endIndex);

    /**
     * <p>
     * Wraps part of given array to be a base accessor.
     * </p>
     *
     * @param array
     *            given array, not null
     * @param startIndex
     *            start index inclusive in bounds
     * @param startOffset
     *            bit offset of start index in bounds
     * @param endIndex
     *            end index inclusive in bounds
     * @param endOffset
     *            bit offset of end index in bounds
     * @return instance after wrapping
     * @since 0.0.0
     */
    public Bits wrap(byte[] array, int startIndex, int startOffset,
            int endIndex, int endOffset);

    /**
     * <p>
     * Wraps part of given array to be a base accessor.
     * </p>
     *
     * @param array
     *            given array, not null
     * @param startIndex
     *            start index inclusive in bounds
     * @param startOffset
     *            bit offset of start index in bounds
     * @param endIndex
     *            end index inclusive in bounds
     * @param endOffset
     *            bit offset of end index in bounds
     * @return instance after wrapping
     * @since 0.0.0
     */
    public Bits wrap(short[] array, int startIndex, int startOffset,
            int endIndex, int endOffset);

    /**
     * <p>
     * Wraps part of given array to be a base accessor.
     * </p>
     *
     * @param array
     *            given array, not null
     * @param startIndex
     *            start index inclusive in bounds
     * @param startOffset
     *            bit offset of start index in bounds
     * @param endIndex
     *            end index inclusive in bounds
     * @param endOffset
     *            bit offset of end index in bounds
     * @return instance after wrapping
     * @since 0.0.0
     */
    public Bits wrap(char[] array, int startIndex, int startOffset,
            int endIndex, int endOffset);

    /**
     * <p>
     * Wraps part of given array to be a base accessor.
     * </p>
     *
     * @param array
     *            given array, not null
     * @param startIndex
     *            start index inclusive in bounds
     * @param startOffset
     *            bit offset of start index in bounds
     * @param endIndex
     *            end index inclusive in bounds
     * @param endOffset
     *            bit offset of end index in bounds
     * @return instance after wrapping
     * @since 0.0.0
     */
    public Bits wrap(int[] array, int startIndex, int startOffset,
            int endIndex, int endOffset);

    /**
     * <p>
     * Wraps part of given array to be a base accessor.
     * </p>
     *
     * @param array
     *            given array, not null
     * @param startIndex
     *            start index inclusive in bounds
     * @param startOffset
     *            bit offset of start index in bounds
     * @param endIndex
     *            end index inclusive in bounds
     * @param endOffset
     *            bit offset of end index in bounds
     * @return instance after wrapping
     * @since 0.0.0
     */
    public Bits wrap(float[] array, int startIndex, int startOffset,
            int endIndex, int endOffset);

    /**
     * <p>
     * Wraps part of given array to be a base accessor.
     * </p>
     *
     * @param array
     *            given array, not null
     * @param startIndex
     *            start index inclusive in bounds
     * @param startOffset
     *            bit offset of start index in bounds
     * @param endIndex
     *            end index inclusive in bounds
     * @param endOffset
     *            bit offset of end index in bounds
     * @return instance after wrapping
     * @since 0.0.0
     */
    public Bits wrap(long[] array, int startIndex, int startOffset,
            int endIndex, int endOffset);

    /**
     * <p>
     * Wraps part of given array to be a base accessor.
     * </p>
     *
     * @param array
     *            given array, not null
     * @param startIndex
     *            start index inclusive in bounds
     * @param startOffset
     *            bit offset of start index in bounds
     * @param endIndex
     *            end index inclusive in bounds
     * @param endOffset
     *            bit offset of end index in bounds
     * @return instance after wrapping
     * @since 0.0.0
     */
    public Bits wrap(double[] array, int startIndex, int startOffset,
            int endIndex, int endOffset);
}
