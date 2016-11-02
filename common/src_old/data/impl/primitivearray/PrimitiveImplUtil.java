package com.cogician.quicker.binary.data.impl.primitivearray;

import com.cogician.quicker.annotation.Base;
import com.cogician.quicker.binary.Bits;

/**
 * <p>
 * Utility class for primitive implementation of this package.
 * </p>
 * <p>
 * Methods of this class don't check passed parameters, be careful to use!
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-05-22 15:08:21
 * @since 0.0.0
 * @see Bits
 */
@Base
final class PrimitiveImplUtil {

    /**
     * <p>
     * Returns whether two accessor are same implementation.
     * </p>
     *
     * @param a1
     *            an implementation, not null
     * @param a2
     *            another implementation, not null
     * @return whether two accessor are same implementation
     */
    static final boolean isSameClass(final Bits a1,
            final Bits a2) {
        return a1.getClass().getName().equals(a2.getClass().getName());
    }

    /**
     * <p>
     * Returns shorter accessor between given two accessor.
     * </p>
     *
     * @param a1
     *            an accessor, not null
     * @param a2
     *            another accessor, not null
     * @return shorter one
     */
    static final Bits getShorterAccessor(final Bits a1,
            final Bits a2) {
        return a1.lengthInBits() <= a2.lengthInBits() ? a1 : a2;
    }

    /**
     * <p>
     * Returns whether given two class are same.
     * </p>
     *
     * @param c1
     *            one class, not null
     * @param c2
     *            another class, not null
     * @return whether same
     */
    static final boolean classEqual(final Class<?> c1, final Class<?> c2) {
        return c1.getName().equals(c2.getName());
    }

}
