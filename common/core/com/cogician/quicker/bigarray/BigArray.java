package com.cogician.quicker.bigarray;

import java.io.Serializable;

import com.cogician.quicker.QuickerProperties;

/**
 * <p>
 * Big arrays is type of array of a long length, accessed by index of <b>long</b> type, extension for traditional array.
 * For example:
 * 
 * <pre>
 * IntArray array = new IntArray(1024 * 1024 * 1024);
 * array.set(0L, 1);
 * array.get(0L);
 * ...
 * </pre>
 * </p>
 * <p>
 * The theoretical length of big array can reach to the {@linkplain java.lang.Long#MAX_VALUE}.
 * </p>
 * <h2>Backed by multidimensional array</h2>
 * <p>
 * Big array is backed by array or multidimensional array. A big array backed by byte[4][5][6] is equivalent to an
 * traditional array byte[120]. The size of each dimension called block size is limited to {@linkplain #BLOCK_SIZE}, and
 * it also can be specified. Actual backed array will be resized according to the length. Note that the max dimensions
 * value of an array is 255 so enough block size specifying is necessary.
 * </p>
 * <h2>Lazy allocating</h2>
 * <p>
 * An one-dimensional backed big array allocates all space when initializes, but multidimensional only allocates its
 * first dimension by default -- it is lazy. It allocates required space when needed. It can also allocate all space
 * like an one-dimensional if specified non-lazy.
 * </p>
 * <h2>Access by index of int type</h2>
 * <p>
 * Big array is accessed by index of long type, but it reserves access methods of <b>int</b> type index. On one hand,
 * using index of int type conform to same behavior of traditional array; on the other hand, using int is a little bit
 * faster than long for one-dimensional array in some JVM. Both two kinds of those methods have same functionality
 * except the range of parameter.
 * </p>
 * <h2>Iterable</h2>
 * <p>
 * Big array is iterable, it can use for-each directly.
 * </p>
 * <h2>Cloneable</h2>
 * <p>
 * Big array is cloneable, its clone-behavior is same as traditional java array.
 * </p>
 * <h2>Thread-safe</h2>
 * <p>
 * Read operation for big array is thread-safe but write operation is not, that is, using read-write lock can ensure
 * thread-safe for big array.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-03-17T10:29:47+08:00
 * @since 0.0.0, 2016-03-17T10:29:47+08:00
 */
public abstract class BigArray implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Default block size.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final int BLOCK_SIZE = QuickerProperties.get("bigArray.block.size").asInt();

    /**
     * <p>
     * A size indicates that if remainder elements of a spliterator of big array is less than this size, it cannot try
     * split again.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final int UNSPLITERATED_SIZE = QuickerProperties.get("bigArray.unspliterated.size").asInt();

    /**
     * <p>
     * Returns length of this array.
     * </p>
     * 
     * @return length of this array
     * @since 0.0.0
     */
    public abstract long length();

    /**
     * <p>
     * Deep copy this array, all settings and options inherited.
     * </p>
     * 
     * @return deep copied new instance
     * @since 0.0.0
     */
    @Override
    public abstract BigArray clone();
}
