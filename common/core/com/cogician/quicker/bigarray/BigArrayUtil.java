package com.cogician.quicker.bigarray;

import java.lang.reflect.Array;

import com.cogician.quicker.OutOfBoundsException;
import com.cogician.quicker.util.QuickMaths;

/**
 * <p>
 * Utility for big array.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-19T22:00:34+08:00
 * @since 0.0.0, 2016-08-19T22:00:34+08:00
 */
class BigArrayUtil {

    /**
     * <p>
     * Caculates needed dimensions of multidimensional array to store elements of specified length.
     * </p>
     * 
     * @param length
     *            specified length, >= 0
     * @param blockSize
     *            specified block size
     * @return an int array stores each size of dimensions, not null
     * @throws OutOfBoundsException
     *             if given length is too long
     * @since 0.0.0
     */
    static int[] caculateDimensions(long length, int blockSize) throws OutOfBoundsException {
        return QuickMaths.minPolynomial(length, blockSize);
    }

    /**
     * <p>
     * Maps given direct index of a big array into multi-indexes of backed multidimensional array. The dimension value
     * should >= 3. {@code dimensions}'s length should == {@code multiIndexes}'s.
     * </p>
     * 
     * @param index
     *            given direct index of a big array in bounds
     * @param dimensions
     *            dimension values of backed multidimensional array (length >= 3), each element of which representing
     *            size of corresponding dimension, not null
     * @param indexes
     *            output param; multi-indexes of backed multidimensional array (dimension >= 3), not null
     * @since 0.0.0
     */
    static void mapMultiIndexes(long index, int[] dimensions, int[] indexes) {
        indexes[indexes.length - 1] = (int)(index % dimensions[dimensions.length - 1]);
        for (int i = dimensions.length - 2; i > 0; i--) {
            long mod = (int)(index % multiplyFrom(dimensions, i));
            indexes[i] = (int)(mod / multiplyFrom(dimensions, i + 1));
        }
        indexes[0] = (int)(index / multiplyFrom(dimensions, 1));
    }

    /**
     * <p>
     * Multiplies from array[index] to end.
     * </p>
     * 
     * @param array
     *            given array, not null
     * @param index
     *            given start index in bounds
     * @return the result
     * @since 0.0.0
     */
    static long multiplyFrom(int[] array, int index) {
        long r = 1;
        for (; index < array.length; index++) {
            r *= array[index];
        }
        return r;
    }

    /**
     * <p>
     * Maps given direct index of a big array into multi-indexes of backed multidimensional array. {@code dimensions}'s
     * length should == {@code multiIndexes}'s.
     * </p>
     * 
     * @param index
     *            given direct index of a big array in bounds
     * @param dimensions
     *            dimension values of backed multidimensional array, each element of which representing size of
     *            corresponding dimension, not null
     * @param inedxes
     *            output param; multi-indexes of backed multidimensional array, not null
     * @since 0.0.0
     */
    static void mapIndexes(long index, int[] dimensions, int[] inedxes) {
        if (dimensions.length == 1) {
            inedxes[0] = (int)index;
        } else if (dimensions.length == 2) {
            inedxes[0] = (int)(index / dimensions[1]);
            inedxes[1] = (int)(index % dimensions[1]);
        } else {
            mapMultiIndexes(index, dimensions, inedxes);
        }
    }

    /**
     * <p>
     * Allocates spaces of multidimensional array (dimensions >= 2) according to given multi-indexes if those spaces is
     * not allocated. The array of last dimension will be returned. If those spaces is allocated, returned directly.
     * </p>
     * 
     * @param array
     *            the multidimensional array, not null
     * @param type
     *            component type of multidimensional array
     * @param dimensions
     *            dimension values of backed multidimensional array, each element of which representing size of
     *            corresponding dimension, not null
     * @param indexes
     *            given multi-indexes, not null
     * @return array of last dimension according to given multi-indexes as Object, not null
     * @since 0.0.0
     */
    static Object alloc(Object array, Class<?> type, int[] dimensions, int[] indexes) {
        Object parent = array;
        int i = 0;
        for (; i < dimensions.length - 2; i++) {
            array = Array.get(parent, indexes[i]);
            if (array == null) {
                array = Array.newInstance(Object.class, dimensions[i + 1]);
                Array.set(parent, indexes[i], array);
            }
            parent = array;
        }
        array = Array.newInstance(type, dimensions[i + 1]);
        Array.set(parent, indexes[i], array);
        return array;
    }

    /**
     * <p>
     * Returns array of last dimension of multidimensional array according to given multi-indexes, maybe null.
     * </p>
     * 
     * @param array
     *            the multidimensional array, not null
     * @param indexes
     *            given multi-indexes, not null
     * @return array of last dimension of multidimensional array according to given multi-indexes, maybe null
     * @since 0.0.0
     */
    static Object getLastDimension(Object array, int[] indexes) {
        for (int i = 0; i < indexes.length - 1; i++) {
            array = Array.get(array, indexes[i]);
            if (array == null) {
                return null;
            }
        }
        return array;
    }

    /**
     * <p>
     * Returns whether given array is penultimate dimension.
     * </p>
     * 
     * @param array
     *            given array, not null
     * @return whether given array is penultimate dimension
     * @since 0.0.0
     */
    @Deprecated
    static boolean isPenultimateDimension(Object array) {
        Class<?> component = array.getClass().getComponentType();
        if (component != null) {
            return component.getClass().isArray() && !component.getComponentType().isArray();
        }
        return false;
    }
}
