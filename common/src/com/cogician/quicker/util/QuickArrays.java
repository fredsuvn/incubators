package com.cogician.quicker.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * Static quick utility class provides static methods for array.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-11-08T08:56:56+08:00
 * @since 0.0.0, 2016-11-08T08:56:56+08:00
 */
public class QuickArrays {

    /**
     * <p>
     * Concats given arrays into a new array and returns.
     * </p>
     *
     * @param <E>
     *            specified type of returned array
     * @param arrays
     *            given arrays
     * @return an array concats given arrays
     * @since 0.0.0
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] concat(E[]... arrays) {
        long newLength = sumLength(arrays);
        E[] result = (E[])Array.newInstance(arrays.getClass().getComponentType().getComponentType(),
                newLength > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)newLength);
        long index = 0;
        LOOP: for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] != null) {
                for (int j = 0; j < arrays[i].length; j++) {
                    E e = (E)arrays[i][j];
                    result[(int)index] = e;
                    if (index >= Integer.MAX_VALUE) {
                        break LOOP;
                    }
                    index++;
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Returns sum length of given arrays. Null array will be seen as an empty array.
     * </p>
     *
     * @param arrays
     *            given arrays
     * @return sum length of given arrays
     * @throws IllegalArgumentException
     *             if there exists non-array object in given arrays
     * @since 0.0.0
     */
    public static long sumLength(@Nullable Object[]... arrays) throws IllegalArgumentException {
        if (Checker.isNotEmpty(arrays)) {
            long l = 0;
            for (int i = 0; i < arrays.length; i++) {
                if (arrays[i] != null) {
                    l += Array.getLength(arrays[i]);
                }
            }
            return l;
        }
        return 0;
    }

    /**
     * <p>
     * Converts given original array into new array of given new type. Each element of original array will be converted
     * by given converter and put into returned new array at corresponding index.
     * </p>
     * <p>
     * If given original array is null, the converter will be ignored and an empty array returned.
     * </p>
     *
     * @param <T>
     *            type of elements of given array
     * @param <R>
     *            type of elements of result array
     * @param original
     *            given original array
     * @param newType
     *            given new type
     * @param converter
     *            given converter
     * @return a new array of which elements are converted by given array
     * @throws NullPointerException
     *             if new type is null or converter is null when it is needed
     * @since 0.0.0
     */
    public static <T, R> R[] convert(@Nullable T[] original, Class<?> newType,
            Function<? super T, ? extends R> converter) throws NullPointerException {
        Checker.checkNull(newType);
        if (Checker.isEmpty(original)) {
            @SuppressWarnings("unchecked")
            R[] ret = (R[])Array.newInstance(newType, 0);
            return ret;
        }
        @SuppressWarnings("unchecked")
        R[] ret = (R[])Array.newInstance(newType, original.length);
        Quicker.flow(original).each((i, e) -> {
            ret[(int)i] = converter.apply(e);
        });
        return ret;
    }

    /**
     * <p>
     * Returns number of equal elements at corresponding index for given 2 arrays.
     * </p>
     *
     * @param array1
     *            first array
     * @param array2
     *            second array
     * @return number of equal elements at corresponding index for given 2 arrays
     * @since 0.0.0
     */
    public static int similarity(@Nullable Object[] array1, @Nullable Object[] array2) {
        if (Checker.isEmpty(array1) || Checker.isEmpty(array2)) {
            return 0;
        }
        int l = Math.max(array1.length, array2.length);
        int result = 0;
        for (int i = 0; i < l; i++) {
            if (Checker.isEqual(array1[i], array2[i])) {
                result++;
            }
        }
        return result;
    }

    /**
     * <p>
     * Returns number of equal elements at corresponding index for each given arrays.
     * </p>
     *
     * @param arrays
     *            given arrays
     * @return number of equal elements at corresponding index for each given arrays
     * @since 0.0.0
     */
    public static int similarity(@Nullable Object[]... arrays) {
        return QuickCollections.similarity(convert(arrays, List.class, array -> Arrays.asList(array)));
    }

    /**
     * <p>
     * Removes duplications in given array to make sure each element is only, then returns array after removing. This
     * method guarantees the index order of elements is same as original. If given array is null, return null.
     * </p>
     * 
     * @param <E>
     *            type of element
     * @param array
     *            given array
     * @return new array after this operation
     * @since 0.0.0
     */
    public static @Nullable <E> E[] distinct(@Nullable E[] array) {
        if (array == null) {
            return null;
        }
        List<E> dis = QuickCollections.distinct(QuickCollections.addAll(new ArrayList<>(), array));
        @SuppressWarnings("unchecked")
        E[] newIs = (E[])Array.newInstance(array.getClass().getComponentType(), dis.size());
        return dis.toArray(newIs);
    }

    /**
     * <p>
     * Joins given array with specified separator.
     * </p>
     * 
     * @param array
     *            given array
     * @param separator
     *            specified separator
     * @return string after joining
     * @since 0.0.0
     */
    public static String join(Object[] array, String separator) {
        if (array.length == 0) {
            return Consts.emptyString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i]);
            sb.append(separator);
        }
        sb.append(array[array.length - 1]);
        return sb.toString();
    }

    /**
     * <p>
     * Joins given array with comma ",".
     * </p>
     * 
     * @param array
     *            given array
     * @return string after joining
     * @since 0.0.0
     */
    public static String join(Object[] array) {
        return join(array, ",");
    }

    /**
     * <p>
     * Joins given array with specified separator. this method can be used for primitive array.
     * </p>
     * 
     * @param array
     *            given array
     * @param separator
     *            specified separator
     * @return string after joining
     * @since 0.0.0
     */
    public static String join(Object array, String separator) {
        Checker.checkArray(array);
        if (array.getClass().getComponentType().isPrimitive()) {
            int length = Array.getLength(array);
            if (length == 0) {
                return Consts.emptyString();
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length - 1; i++) {
                sb.append(Array.get(array, i));
                sb.append(separator);
            }
            sb.append(Array.get(array, length - 1));
            return sb.toString();
        } else {
            return join((Object[])array, separator);
        }
    }

    /**
     * <p>
     * Joins given array with comma ",". this method can be used for primitive array.
     * </p>
     * 
     * @param array
     *            given array
     * @return string after joining
     * @since 0.0.0
     */
    public static String join(Object array) {
        return join(array, ",");
    }

    /**
     * <p>
     * Returns index of the first element which is equal to given object in given array. If not found, return a negative
     * value.
     * </p>
     * 
     * @param array
     *            given array
     * @param obj
     *            given object
     * @return index of the first element which is equal to given object in given array
     * @since 0.0.0
     */
    public static int contain(Object[] array, @Nullable Object obj) {
        for (int i = 0; i < array.length; i++) {
            if (Checker.isEqual(array[i], obj)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * <p>
     * Returns index of the first element which is equal to given object in given array. If not found, return a negative
     * value. This method can be used for primitive array.
     * </p>
     * 
     * @param array
     *            given array
     * @param obj
     *            given object
     * @return index of the first element which is equal to given object in given array
     * @since 0.0.0
     */
    public static int contain(Object array, @Nullable Object obj) {
        Checker.checkArray(array);
        if (array.getClass().getComponentType().isPrimitive()) {
            int length = Array.getLength(array);
            if (length == 0) {
                return -1;
            }
            for (int i = 0; i < length; i++) {
                if (Checker.isEqual(Array.get(array, i), obj)) {
                    return i;
                }
            }
            return -1;
        } else {
            return contain((Object[])array, obj);
        }
    }
}
