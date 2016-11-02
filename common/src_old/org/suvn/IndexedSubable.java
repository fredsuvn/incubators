package com.cogician.quicker;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>
 * This interface represents indexed {@linkplain Subable} -- of which content are accessed by index, and each part
 * accessed by index is an element.
 * </p>
 * <p>
 * Indexed sub-able is ordered and iterable. It can traverse elements one by one, or traverse elements by more than one
 * elements which are seen as one element.
 * </p>
 * <p>
 * The declared type of subtypes should be declared in type parameter {@linkplain T}, for example:
 * 
 * <pre>
 * To declare a indexed sub-able type:
 * 
 *     public class MySub implements IndexedSubable&lt;MySub&gt; {
 *         ...
 *     }
 * 
 * Then MySub can be divided into sub-portions, and each sub-portion is a MySub.
 * </pre>
 * 
 * </p>
 * <p>
 * There are two sets of methods in this interface, one in {@code int} and the other in {@code long}. Both have same
 * effect except for the range ({@code long}'s is greater than {@code int}'s).
 * </p>
 * 
 * @param <T>
 *            declared type of current instance
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-08-07 11:24:59
 * @since 0.0.0
 * @see Subable
 */
public interface IndexedSubable<T> extends Subable, Iterable<T>, Cloneable {

    public T duplicate(int index);

    public T duplicate(int startIndex, int endIndex);
}
