package com.cogician.quicker.bigarray;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * Object array is type of {@linkplain BigArray} of a long length, accessed by index of <b>long</b> type, extension for
 * traditional array. Its component is Object. For example:
 * 
 * <pre>
 * ObjectArray&#60;String&#62; array = new ObjectArray&#60;&#62(1024 * 1024 * 1024);
 * array.set(0L, "1");
 * array.get(0L);
 * ...
 * </pre>
 * </p>
 *
 * @param <T>
 *            type of component
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2016-03-17T14:43:39+08:00
 * @since 0.0.0, 2016-03-17T14:43:39+08:00
 * @see BigArray
 */
public class ObjectArray<T> extends BigArray implements Iterable<T>, Cloneable {

    private static final long serialVersionUID = 1L;

    private static <T> Data<T> createData(long length, int[] dimensions, T initial, boolean lazy) {
        if (1 == dimensions.length) {
            return new OfOneDimension<>((int)length, initial);
        } else if (2 == dimensions.length) {
            return new OfTwoDimensions<>(length, dimensions, initial, lazy);
        } else {
            return new OfMultiDimensions<>(length, dimensions, initial, lazy);
        }
    }

    private ObjectArray(Data<T> data) {
        this.data = data;
    }

    /**
     * <p>
     * Constructs with specified length.
     * </p>
     * 
     * @param length
     *            specified length
     * @throws IllegalArgumentException
     *             if specified length < 0
     * @since 0.0.0
     */
    public ObjectArray(long length) throws IllegalArgumentException {
        this(length, null);
    }

    /**
     * <p>
     * Constructs with specified length and initial value.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @param length
     *            specified length
     * @param initial
     *            initial value
     * @throws IllegalArgumentException
     *             if specified length < 0
     * @since 0.0.0
     */
    public ObjectArray(long length, T initial) throws IllegalArgumentException {
        this(length, BigArray.BLOCK_SIZE, initial, true);
    }

    /**
     * <p>
     * Constructs with specified length, specified block size, initial value and whether this instance is lazy.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @param length
     *            specified length
     * @param blockSize
     *            specified block size
     * @param initial
     *            initial value
     * @param lazy
     *            whether this instance is lazy
     * @throws IllegalArgumentException
     *             if specified length < 0 or specified block size <= 0
     * @since 0.0.0
     */
    public ObjectArray(long length, int blockSize, T initial, boolean lazy) throws IllegalArgumentException {
        Checker.checkLength(length);
        Checker.checkPositive(blockSize);
        this.data = createData(length, BigArrayUtil.caculateDimensions(length, blockSize), initial, lazy);
    }

    /**
     * <p>
     * Constructs a big array wraps specified array.
     * </p>
     * 
     * @param <T>
     *            type of array element
     * @param array
     *            specified array
     * @throws NullPointerException
     *             if specified array is null
     * @since 0.0.0
     */
    public ObjectArray(T[] array) throws NullPointerException {
        Checker.checkNull(array);
        this.data = new ArrayWrapper<T>(array, 0, array.length);
    }

    /**
     * <p>
     * Constructs a big array wraps specified array between from index inclusive and to index exclusive.
     * </p>
     * 
     * @param <T>
     *            type of array element
     * @param array
     *            specified array
     * @param from
     *            from index inclusive
     * @param to
     *            to index exclusive
     * @throws NullPointerException
     *             if specified array is null
     * @throws IllegalArgumentException
     *             if from index > to index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public ObjectArray(T[] array, int from, int to)
            throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
        Checker.checkNull(array);
        Checker.checkRangeIndexes(from, to, array.length);
        this.data = new ArrayWrapper<T>(array, from, to);
    }

    private final Data<T> data;

    @Override
    public long length() {
        return data.length();
    }

    /**
     * <p>
     * Gets value at specified index.
     * </p>
     * 
     * @param index
     *            specified index in bounds
     * @return value at specified index
     * @throws IndexOutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public T get(int index) throws IndexOutOfBoundsException {
        return data.get(index);
    }

    /**
     * <p>
     * Sets value at specified index.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @param index
     *            index specified index in bounds
     * @param value
     *            value to be set
     * @throws IndexOutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void set(int index, T value) throws IndexOutOfBoundsException {
        data.set(index, value);
    }

    /**
     * <p>
     * Gets value at specified index.
     * </p>
     * 
     * @param index
     *            specified index in bounds
     * @return value at specified index
     * @throws IndexOutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public T get(long index) throws IndexOutOfBoundsException {
        return data.get(index);
    }

    /**
     * <p>
     * Sets value at specified index.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @param index
     *            index specified index in bounds
     * @param value
     *            value to be set
     * @throws IndexOutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void set(long index, T value) throws IndexOutOfBoundsException {
        data.set(index, value);
    }

    /**
     * <p>
     * Fills elements of this array with specified value.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @param value
     *            specified value
     * @since 0.0.0
     */
    public void fill(T value) {
        data.fill(value);
    }

    /**
     * <p>
     * Copies data of this array into destination array. Copied length is minimum length of two array.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @param dest
     *            destination array, not null
     * @throws NullPointerException
     *             if destination array is null
     * @since 0.0.0
     */
    public void copy(ObjectArray<T> dest) throws NullPointerException {
        copy(0L, dest, 0L, Math.min(length(), dest.length()));
    }

    /**
     * <p>
     * Copies data of this array into destination array.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @param srcPos
     *            source start index in bounds
     * @param dest
     *            destination array, not null
     * @param destPos
     *            destination start index in bounds
     * @param length
     *            specified length, >= 0
     * @throws NullPointerException
     *             if destination array is null
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds
     * @throws IllegalArgumentException
     *             if specified length is negative
     *
     * @since 0.0.0
     */
    public void copy(long srcPos, ObjectArray<T> dest, long destPos, long length)
            throws NullPointerException, IndexOutOfBoundsException {
        Checker.checkIndex(srcPos, length());
        Checker.checkIndex(destPos, length());
        Checker.checkLength(length);
        data.copy(srcPos, dest.data, destPos, length);
    }

    /**
     * <p>
     * Clones this array, all settings and options inherited.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @return duplication of this array
     * @since 0.0.0
     */
    @Override
    public ObjectArray<T> clone() {
        return new ObjectArray<>(data.clone());
    }

    /**
     * <p>
     * Converts this array into a java array with speicifed type. If length of this array is greater than
     * {@linkplain Integer#MAX_VALUE} , return first {@linkplain Integer#MAX_VALUE} elements.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @param type
     *            specified type, not null
     * @return a java array converted from this array
     * @throws NullPointerException
     *             if specified type is null
     * @since 0.0.0
     */
    public T[] toArray(Class<?> type) throws NullPointerException {
        return data.toArray(type);
    }

    /**
     * <p>
     * COnverts this array to an object array. If length of this array is greater than {@linkplain Integer#MAX_VALUE},
     * return first {@linkplain Integer#MAX_VALUE} elements.
     * </p>
     * 
     * @return an object array
     * @since 0.0.0
     */
    public Object[] toArray() {
        return data.toArray(Object.class);
    }

    /**
     * <p>
     * Returns an iterator to traverse this array.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @return an iterator to traverse this array
     * @since 0.0.0
     */
    @Override
    public Iterator<T> iterator() {
        return new ObjectArrayIterator<>(this);
    }

    /**
     * <p>
     * Returns a spliterator to traverse this array.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @return a spliterator to traverse this array
     * @since 0.0.0
     */
    @Override
    public Spliterator<T> spliterator() {
        return new ObjectArraySpliterator<T>(this, 0, length());
    }

    /**
     * <p>
     * Returns a stream to traverse this array.
     * </p>
     * 
     * @param <T>
     *            type of component
     * @return a stream to traverse this array
     * @since 0.0.0
     */
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    private static class ObjectArrayIterator<T> implements Iterator<T> {

        private ObjectArray<T> array;

        private long cur = 0;

        public ObjectArrayIterator(ObjectArray<T> array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return cur < array.length();
        }

        @Override
        public T next() {
            if (cur >= array.length()) {
                throw new NoSuchElementException();
            }
            return array.get(cur++);
        }
    }

    private static class ObjectArraySpliterator<T> implements Spliterator<T> {

        private ObjectArray<T> array;

        private long end;

        private long cur;

        public ObjectArraySpliterator(ObjectArray<T> array, long start, long end) {
            this.array = array;
            this.cur = start;
            this.end = end;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (cur >= array.length()) {
                return false;
            }
            Quicker.require(action).accept(array.get(cur++));
            return true;
        }

        @Override
        public Spliterator<T> trySplit() {
            if (end - cur > BigArray.UNSPLITERATED_SIZE) {
                return null;
            }
            long newStart = (end - cur) / 2;
            this.cur = newStart;
            return new ObjectArraySpliterator<T>(array, cur, newStart);
        }

        @Override
        public long estimateSize() {
            return end - cur;
        }

        @Override
        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }

    }

    private static abstract class Data<T> implements Serializable, Cloneable {

        private static final long serialVersionUID = 1L;

        public abstract int[] getDimensions();

        public abstract long length();

        public T get(int index) throws IndexOutOfBoundsException {
            return get((long)index);
        }

        public void set(int index, T value) throws IndexOutOfBoundsException {
            set((long)index, value);
        }

        public abstract T get(long index) throws IndexOutOfBoundsException;

        public abstract void set(long index, T value) throws IndexOutOfBoundsException;

        public abstract void fill(T value);

        public void copy(long srcPos, Data<T> dest, long destPos, long length)
                throws NullPointerException, IndexOutOfBoundsException {
            if (length == 0) {
                return;
            }
            Checker.checkRangeIndexes(srcPos, srcPos + length, length());
            Checker.checkRangeIndexes(destPos, destPos + length, dest.length());
            int[] dimensions = getDimensions();
            int[] destDimensions = dest.getDimensions();
            int[] srcMultiIndexes = new int[dimensions.length];
            int[] destMultiIndexes = new int[destDimensions.length];
            int sWide = dimensions[dimensions.length - 1];
            int dWide = destDimensions[destDimensions.length - 1];
            for (long i = 0; i < length;) {
                BigArrayUtil.mapIndexes(srcPos + i, dimensions, srcMultiIndexes);
                Object[] sr = (Object[])BigArrayUtil.getLastDimension(getBackedArray(), srcMultiIndexes);
                int ys = (int)((srcPos + i) % sWide);
                BigArrayUtil.mapIndexes(destPos + i, destDimensions, destMultiIndexes);
                Object[] dr = (Object[])BigArrayUtil.getLastDimension(dest.getBackedArray(), destMultiIndexes);
                int yd = (int)((destPos + i) % dWide);
                int copyLength = Math.min(sWide - ys, dWide - yd);
                copyLength = (int)Math.min((long)copyLength, length - i);
                if (sr != null) {
                    if (dr == null) {
                        // One-dimensional backed array is never null.
                        dr = (Object[])BigArrayUtil.alloc(dest.getBackedArray(), Object.class, destDimensions,
                                destMultiIndexes);
                    }
                    System.arraycopy(sr, ys, dr, yd, copyLength);
                }
                i += copyLength;
            }
        }

        public abstract Data<T> clone();

        public abstract T[] toArray(Class<?> type);

        public abstract Object getBackedArray();
    }

    private static class ArrayWrapper<T> extends Data<T> {

        private static final long serialVersionUID = 1L;

        private final T[] source;

        private int from;

        private int length;

        public ArrayWrapper(T[] source, int from, int to) {
            this.source = source;
            this.from = from;
            this.length = to - from;
        }

        @Override
        public int[] getDimensions() {
            return null;
        }

        @Override
        public long length() {
            return length;
        }

        @Override
        public T get(int index) throws IndexOutOfBoundsException {
            return source[index + from];
        }

        @Override
        public void set(int index, T value) throws IndexOutOfBoundsException {
            source[index + from] = value;
        }

        @Override
        public T get(long index) throws IndexOutOfBoundsException {
            return source[(int)index + from];
        }

        @Override
        public void set(long index, T value) throws IndexOutOfBoundsException {
            source[(int)index + from] = value;
        }

        @Override
        public void fill(T value) {
            Arrays.fill(source, from, from + length, value);
        }

        @Override
        public void copy(long srcPos, Data<T> dest, long destPos, long length)
                throws NullPointerException, IndexOutOfBoundsException {
            if (dest instanceof OfOneDimension) {
                if (length == 0) {
                    return;
                }
                Checker.checkRangeIndexes(srcPos, srcPos + length, length());
                Checker.checkRangeIndexes(destPos, destPos + length, ((OfOneDimension<T>)dest).source.length);
                System.arraycopy(source, (int)srcPos + from, ((OfOneDimension<T>)dest).source, (int)destPos,
                        (int)length);
            } else if (dest instanceof ArrayWrapper) {
                if (length == 0) {
                    return;
                }
                Checker.checkRangeIndexes(srcPos, srcPos + length, length());
                Checker.checkRangeIndexes(destPos, destPos + length, dest.length());
                System.arraycopy(source, (int)srcPos + from, ((ArrayWrapper<T>)dest).source,
                        (int)destPos + ((ArrayWrapper<T>)dest).from, (int)length);
            } else {
                super.copy(srcPos, dest, destPos, length);
            }
        }

        @Override
        public Data<T> clone() {
            return new OfOneDimension<T>(this);
        }

        @Override
        public T[] toArray(Class<?> type) {
            @SuppressWarnings("unchecked")
            T[] ar = (T[])Array.newInstance(type, length);
            System.arraycopy(source, from, ar, 0, length);
            return ar;
        }

        @Override
        public Object getBackedArray() {
            return source;
        }
    }

    /**
     * <p>
     * One dimension Implementation.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-03-18T15:52:34+08:00
     * @since 0.0.0, 2016-03-18T15:52:34+08:00
     */
    private static class OfOneDimension<T> extends Data<T> {

        private static final long serialVersionUID = 1L;

        private final Object[] source;

        private final int[] dimensions;

        public OfOneDimension(int length, T initial) {
            source = new Object[length];
            if (initial != null) {
                Arrays.fill(source, initial);
            }
            this.dimensions = new int[]{(int)source.length};
        }

        public OfOneDimension(OfOneDimension<T> copyFrom) {
            this.source = copyFrom.source.clone();
            this.dimensions = new int[]{(int)source.length};
        }

        public OfOneDimension(Data<T> copyFrom) {
            this((int)copyFrom.length(), null);
            copyFrom.copy(0, this, 0, (int)copyFrom.length());
        }

        @Override
        public int[] getDimensions() {
            return dimensions;
        }

        @Override
        public long length() {
            return source.length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(int index) throws IndexOutOfBoundsException {
            return (T)source[index];
        }

        @Override
        public void set(int index, T value) throws IndexOutOfBoundsException {
            source[index] = value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(long index) throws IndexOutOfBoundsException {
            return (T)source[(int)index];
        }

        @Override
        public void set(long index, T value) throws IndexOutOfBoundsException {
            source[(int)index] = value;
        }

        @Override
        public void fill(T value) {
            Arrays.fill(source, value);
        }

        @Override
        public void copy(long srcPos, Data<T> dest, long destPos, long length)
                throws NullPointerException, IndexOutOfBoundsException {
            if (dest instanceof OfOneDimension) {
                if (length == 0) {
                    return;
                }
                Checker.checkRangeIndexes(srcPos, srcPos + length, source.length);
                Checker.checkRangeIndexes(destPos, destPos + length, ((OfOneDimension<T>)dest).source.length);
                System.arraycopy(source, (int)srcPos, ((OfOneDimension<T>)dest).source, (int)destPos, (int)length);
            } else if (dest instanceof ArrayWrapper) {
                if (length == 0) {
                    return;
                }
                Checker.checkRangeIndexes(srcPos, srcPos + length, length());
                Checker.checkRangeIndexes(destPos, destPos + length, dest.length());
                System.arraycopy(source, (int)srcPos, ((ArrayWrapper<T>)dest).source,
                        (int)destPos + ((ArrayWrapper<T>)dest).from, (int)length);
            } else {
                super.copy(srcPos, dest, destPos, length);
            }
        }

        @Override
        public Data<T> clone() {
            return new OfOneDimension<T>(this);
        }

        @Override
        public T[] toArray(Class<?> type) {
            @SuppressWarnings("unchecked")
            T[] ar = (T[])Array.newInstance(type, source.length);
            System.arraycopy(source, 0, ar, 0, source.length);
            return ar;
        }

        @Override
        public Object getBackedArray() {
            return source;
        }
    }

    /**
     * <p>
     * Abstract two dimensions implementation.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-03-22T10:18:37+08:00
     * @since 0.0.0, 2016-03-22T10:18:37+08:00
     */
    private static class OfTwoDimensions<T> extends Data<T> {

        private static final long serialVersionUID = 1L;

        private final long length;

        private final int[] dimensions;

        private final Object[][] source;

        private T filler;

        private boolean lazy;

        public OfTwoDimensions(long length, int[] dimensions, T initial, boolean lazy) {
            this.length = length;
            this.dimensions = dimensions;
            this.lazy = lazy;
            if (lazy) {
                source = new Object[dimensions[0]][];
            } else {
                source = new Object[dimensions[0]][dimensions[1]];
            }
            fill(initial);
        }

        public OfTwoDimensions(OfTwoDimensions<T> copyFrom) {
            this(copyFrom.length, copyFrom.dimensions, copyFrom.filler, copyFrom.lazy);
            copyFrom.copy(0, this, 0, length);
        }

        @Override
        public int[] getDimensions() {
            return dimensions;
        }

        @Override
        public long length() {
            return length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(long index) throws IndexOutOfBoundsException {
            Checker.checkIndex(index, length);
            int x = (int)(index / dimensions[1]);
            return null == source[x] ? filler : (T)source[x][(int)(index % dimensions[1])];
        }

        @Override
        public void set(long index, T value) throws IndexOutOfBoundsException {
            Checker.checkIndex(index, length);
            int x = (int)(index / dimensions[1]);
            if (source[x] == null) {
                if (value == filler) {
                    return;
                } else {
                    source[x] = new Object[dimensions[1]];
                    if (null != filler) {
                        Arrays.fill(source[x], filler);
                    }
                }
            }
            int y = (int)(index % dimensions[1]);
            source[x][y] = value;
        }

        @Override
        public void fill(T value) {
            this.filler = value;
            for (int i = 0; i < source.length; i++) {
                fillBlock(i, value);
            }
        }

        private void fillBlock(int block, T value) {
            if (null == source[block]) {
                if (this.filler == value) {
                    return;
                }
                source[block] = new Object[dimensions[1]];
            }
            Arrays.fill(source[block], value);
        }

        @Override
        public void copy(long srcPos, Data<T> dest, long destPos, long length)
                throws NullPointerException, IndexOutOfBoundsException {
            if (dest instanceof OfTwoDimensions) {
                if (length == 0) {
                    return;
                }
                Checker.checkRangeIndexes(srcPos, srcPos + length, length());
                Checker.checkRangeIndexes(destPos, destPos + length, dest.length());
                OfTwoDimensions<T> d = (OfTwoDimensions<T>)dest;
                for (long i = 0; i < length;) {
                    int xs = (int)((srcPos + i) / dimensions[1]);
                    int ys = (int)((srcPos + i) % dimensions[1]);
                    int xd = (int)((destPos + i) / d.dimensions[1]);
                    int yd = (int)((destPos + i) % d.dimensions[1]);
                    int copyLength = Math.min(dimensions[1] - ys, d.dimensions[1] - yd);
                    copyLength = (int)Math.min((long)copyLength, length - i);
                    if (source[xs] != null) {
                        if (d.source[xd] == null) {
                            d.source[xd] = new Object[d.dimensions[1]];
                        }
                        System.arraycopy(source[xs], ys, d.source[xd], yd, copyLength);
                    }
                    i += copyLength;
                }
            } else {
                super.copy(srcPos, dest, destPos, length);
            }
        }

        @Override
        public Data<T> clone() {
            return new OfTwoDimensions<T>(this);
        }

        @Override
        public T[] toArray(Class<?> type) {
            return new OfOneDimension<>(this).toArray(type);
        }

        @Override
        public Object getBackedArray() {
            return source;
        }
    }

    /**
     * <p>
     * Multi-dimensions implementation.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-03-18T15:52:54+08:00
     * @since 0.0.0, 2016-03-18T15:52:54+08:00
     */
    private static class OfMultiDimensions<T> extends Data<T> {

        private static final long serialVersionUID = 1L;

        private final long length;

        private final int[] dimensions;

        private final Object source;

        private T filler;

        private boolean lazy;

        public OfMultiDimensions(long length, int[] dimensions, T initial, boolean lazy) {
            this.length = length;
            this.dimensions = dimensions;
            this.lazy = lazy;
            if (lazy) {
                source = new Object[dimensions[0]];
            } else {
                source = Array.newInstance(Object.class, dimensions);
            }
            fill(initial);
        }

        public OfMultiDimensions(OfMultiDimensions<T> copyFrom) {
            this(copyFrom.length, copyFrom.dimensions, copyFrom.filler, copyFrom.lazy);
            copyFrom.copy(0, this, 0, length);
        }

        @Override
        public int[] getDimensions() {
            return dimensions;
        }

        @Override
        public long length() {
            return length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(long index) throws IndexOutOfBoundsException {
            Checker.checkIndex(index, length);
            int[] indexes = new int[dimensions.length];
            BigArrayUtil.mapMultiIndexes(index, dimensions, indexes);
            Object[] array = (Object[])BigArrayUtil.getLastDimension(source, indexes);
            if (array == null) {
                return filler;
            } else {
                return (T)array[indexes[indexes.length - 1]];
            }
        }

        @Override
        public void set(long index, T value) throws IndexOutOfBoundsException {
            Checker.checkIndex(index, length);
            int[] indexes = new int[dimensions.length];
            BigArrayUtil.mapMultiIndexes(index, dimensions, indexes);
            Object[] array = (Object[])BigArrayUtil.getLastDimension(source, indexes);
            if (array == null) {
                if (value == filler) {
                    return;
                } else {
                    array = (Object[])BigArrayUtil.alloc(source, Object.class, dimensions, indexes);
                    if (null != filler) {
                        Arrays.fill(array, filler);
                    }
                }
            }
            array[indexes[indexes.length - 1]] = value;
        }

        @Override
        public void fill(T value) {
            this.filler = value;
            fillMultiArray((Object[])source, value, 0);
        }

        private void fillMultiArray(Object[] array, T value, int dimension) {
            if (dimension == dimensions.length - 2) {
                for (int i = 0; i < array.length; i++) {
                    if (null == array[i]) {
                        if (this.filler == value) {
                            continue;
                        }
                        array[i] = new Object[dimension + 1];
                    }
                    Arrays.fill((Object[])array[i], value);
                }
            } else {
                for (int i = 0; i < array.length; i++) {
                    if (null == array[i]) {
                        if (this.filler == value) {
                            continue;
                        }
                        array[i] = new Object[dimension + 1];
                    }
                    fillMultiArray((Object[])array[i], value, dimension + 1);
                }
            }
        }

        @Override
        public Data<T> clone() {
            return new OfMultiDimensions<T>(this);
        }

        @Override
        public T[] toArray(Class<?> type) {
            return new OfOneDimension<>(this).toArray(type);
        }

        @Override
        public Object getBackedArray() {
            return source;
        }
    }
}
