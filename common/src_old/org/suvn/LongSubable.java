package com.cogician.quicker;

import java.util.stream.Stream;

import com.cogician.quicker.function.LongObjConsumer;

/**
 * <p>
 * This interface indicates implementation can be sub-split by specified long
 * type indexes, using {@linkplain IndexedSubable} for int type indexes.
 * </p>
 * <p>
 * The sub-instance's content may be shallow cloned or deep cloned from parent.
 * Content of sub-instance shallow cloned is one and shared with parent's, but
 * deep clone is different two and independent.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-12-10 17:02:12
 * @since 0.0.0
 * @see IndexedSubable
 */
public interface LongSubable<T> {

    /**
     * <p>
     * Returns sub-object from specified index inclusive to end. Content of the
     * sub-object is shallow cloned, that is, sub-object's content and specified
     * part of this instance's content are same one.
     * </p>
     * 
     * @param index
     *            specified index inclusive in bounds
     * @return sub-object from specified index inclusive to end, not null
     * @throws OutOfBoundsException
     *             if specified index out of bounds
     * @since 0.0.0
     */
    public T shallowSub(long index);

    /**
     * <p>
     * Returns sub-object from specified start index inclusive to specified end
     * index exclusive. Content of the sub-object is shallow cloned, that is,
     * sub-object's content and specified part of this instance's content are
     * same one.
     * </p>
     * 
     * @param startIndex
     *            specified start index inclusive in bounds
     * @param endIndex
     *            specified end index exclusive in bounds
     * @return sub-object from specified start index inclusive to specified end
     *         index exclusive, not null
     * @throws OutOfBoundsException
     *             if specified indexes out of bounds
     * @since 0.0.0
     */
    public T shallowSub(long startIndex, long endIndex);

    /**
     * <p>
     * Returns sub-object from specified index inclusive to end. Content of the
     * sub-object is deep cloned.
     * </p>
     * 
     * @param index
     *            specified index inclusive in bounds
     * @return sub-object from specified index inclusive to end, not null
     * @throws OutOfBoundsException
     *             if specified index out of bounds
     * @since 0.0.0
     */
    public T deepSub(long index);

    /**
     * <p>
     * Returns sub-object from specified start index inclusive to specified end
     * index exclusive. Content of the sub-object is deep cloned.
     * </p>
     * 
     * @param startIndex
     *            specified start index inclusive in bounds
     * @param endIndex
     *            specified end index exclusive in bounds
     * @return sub-object from specified start index inclusive to specified end
     *         index exclusive, not null
     * @throws OutOfBoundsException
     *             if specified indexes out of bounds
     * @since 0.0.0
     */
    public T deepSub(long startIndex, long endIndex);

    /**
     * <p>
     * For-each operation for this instance. Each for-each body is split from
     * this instance for specified length long. If the last body's length is not
     * enough, rest all will be put into the last body.
     * </p>
     * <p>
     * Each body will be passed into a action to process. For each action, there
     * are two parameters, a reference to reference body, and a long value to
     * count the order of body start from 0.
     * </p>
     * <p>
     * Like "shallow clone", bodies' content and this instance's content are
     * same one.
     * </p>
     * 
     * @param length
     *            specified length, positive
     * @param action
     *            action of for-each operation, not null
     * @throws NullPointerException
     *             if exists null action
     * @since 0.0.0
     */
    public void forEach(long length, LongObjConsumer<T> action);

    /**
     * <p>
     * Returns a stream of which elements are split from this instance for
     * specified length long. If the last element's length is not enough, rest
     * all will be put into the last element.
     * </p>
     * <p>
     * Like "shallow clone", elements' content and this instance's content are
     * same one.
     * </p>
     * 
     * @param length
     *            specified length, positive
     * @return a stream of which elements are split by specified length, not
     *         null
     * @throws IllegalArgumentException
     *             if specified length is 0 or negative
     * @since 0.0.0
     */
    public Stream<T> stream(long length);

    /**
     * <p>
     * Returns a parallel stream of which elements are split from this instance
     * for specified length long. If the last element's length is not enough,
     * rest all will be put into the last element.
     * </p>
     * <p>
     * Like "shallow clone", elements' content and this instance's content are
     * same one.
     * </p>
     * 
     * @param length
     *            specified length, positive
     * @return a parallel stream of which elements are split by specified
     *         length, not null
     * @throws IllegalArgumentException
     *             if specified length is 0 or negative
     * @since 0.0.0
     */
    public Stream<T> parallelStream(long length);
}
