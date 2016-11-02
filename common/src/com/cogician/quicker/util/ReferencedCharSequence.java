package com.cogician.quicker.util;

import java.io.Serializable;

import com.cogician.quicker.Checker;

/**
 * <p>
 * An implementation of {@linkplain CharSequence} implemented by referencing and holding given source of char sequence.
 * Its {@linkplain #subSequence(int, int)} will return an instance of this implementation holds the original given
 * source of char sequence.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-31T13:16:48+08:00
 * @since 0.0.0, 2016-07-31T13:16:48+08:00
 */
public class ReferencedCharSequence implements Serializable, CharSequence {

    private static final long serialVersionUID = 1L;

    private final CharSequence source;

    private final int startIndex;

    private final int endIndex;

    /**
     * <p>
     * Constructs a instance with given source char sequence.
     * </p>
     * 
     * @param source
     *            given source char sequence
     * @throws NullPointerException
     *             if given source char sequence is null
     * @since 0.0.0
     */
    public ReferencedCharSequence(CharSequence source) throws NullPointerException {
        Checker.checkNull(source);
        this.source = source;
        this.startIndex = 0;
        this.endIndex = source.length();
    }

    /**
     * <p>
     * Constructs a instance with given source char sequence, start index and end index.
     * </p>
     * 
     * @param source
     *            given source char sequence
     * @param startIndex
     *            given start index
     * @param endIndex
     *            given end index
     * @throws NullPointerException
     *             if given source char sequence is null
     * @throws IllegalArgumentException
     *             if if startIndex > endIndex
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds
     * @since 0.0.0
     */
    public ReferencedCharSequence(CharSequence source, int startIndex, int endIndex)
            throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
        Checker.checkNull(source);
        Checker.checkRangeIndexes(startIndex, endIndex, source.length());
        this.source = source;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public int length() {
        return endIndex - startIndex;
    }

    @Override
    public char charAt(int index) {
        return source.charAt(index + startIndex);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new ReferencedCharSequence(source, start + startIndex, end + startIndex);
    }

    @Override
    public String toString() {
        return source.subSequence(startIndex, endIndex).toString();
    }
}
