package com.cogician.quicker;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Comparator;

/**
 * This class represents a type of number to describe binary data accurate to
 * bit. It consist of a byte value and a bit value to indicate length, offset,
 * position or other possible significance about measuring of data.
 * <p>
 * The length of data may not be multiple of byte, like 17 bits. Byte value is
 * number of data length which rounded down to byte, bit value indicates number
 * of rest bits when the data length has been rounded. That is, for a
 * 17-bits-length data, its byte value is 2, bit value is 1 (17 = 2 * 8 + 1).
 * <p>
 * This class is immutable, can be added and subtracted then return a new
 * instance.
 * <p>
 * <b>Note</b>: a legal data number is always positive or zero.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2014-10-08 16:14:21
 */
public final class DataNumber implements Serializable, Cloneable,
        Comparable<DataNumber>, Calculable.Addable<DataNumber>,
        Calculable.Subtractable<DataNumber> {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * A data number represents 0.
     */
    public static final DataNumber ZERO = new DataNumber();

    /**
     * Max data number.
     */
    public static final DataNumber MAX = new DataNumber(Long.MAX_VALUE,
            Byte.SIZE - 1, false);

    /**
     * A data number of which total number of bits is {@link Long#MAX_VALUE}.
     */
    public static final DataNumber LONG_MAX = new DataNumber(
            1152921504606846975L, 7, false);

    /**
     * Byte value.
     */
    private final long byteValue;

    /**
     * Bit value.
     */
    private final int bitValue;

    /**
     * Checks whether input values are legal.
     * <p>
     * A legal byte value should be [0,+)
     * <p>
     * A legal bit value should be [0,7).
     *
     * @param byteValue
     *            input byte value, [0,+)
     * @param bitValue
     *            input bit value, [0,7)
     * @return true if input values are legal, false illegal
     */
    private boolean checkValue(final long byteValue, final int bitValue) {
        return byteValue >= 0 && bitValue >= 0 && bitValue < Byte.SIZE;
    }

    /**
     * Constructs an instance of this class by given params.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @param bitValue
     *            given bit value, [0,7)
     * @param needCheck
     *            whether need check legality of given values
     * @exception IllegalArgumentException
     *                thrown if needCheck is true and given values are illegal
     * @see #checkValue(long, int)
     */
    private DataNumber(final long byteValue, final int bitValue,
            final boolean needCheck) {
        if (needCheck && !checkValue(byteValue, bitValue)) {
            throw new IllegalArgumentException(
                    "Constructs failed: input byte value or bit value"
                            + " is illegal.");
        }
        this.byteValue = byteValue;
        this.bitValue = bitValue;
    }

    /**
     * Constructs a data number instance represents 0.
     */
    public DataNumber() {
        this.byteValue = 0L;
        this.bitValue = 0;
    }

    /**
     * Constructs an instance of this class by given values.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @param bitValue
     *            given bit value, [0,7)
     * @exception IllegalArgumentException
     *                thrown if given values are illegal
     */
    public DataNumber(final long byteValue, final int bitValue) {
        this(byteValue, bitValue, true);
    }

    /**
     * Constructs an instance of this class by given byte value.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @exception IllegalArgumentException
     *                thrown if byte value is illegal
     */
    public DataNumber(final long byteValue) {
        this(byteValue, 0);
    }

    /**
     * Constructs an instance of this class by given bit value.
     *
     * @param bitValue
     *            given bit value, [0,7)
     * @exception IllegalArgumentException
     *                thrown if bit value is illegal
     */
    public DataNumber(final int bitValue) {
        this(0L, bitValue);
    }

    /**
     * Constructs an instance of this class copy from given data number.
     *
     * @param dn
     *            given data number, not null
     * @exception NullPointerException
     *                thrown if given object is null
     */
    public DataNumber(final DataNumber dn) {
        this(dn.byteValue, dn.bitValue, false);
    }

    /**
     * Returns byte value of this instance.
     *
     * @return byte value of this instance
     * @see DataNumber
     */
    public long byteValue() {
        return byteValue;
    }

    /**
     * Returns bit value of this instance.
     *
     * @return bit value of this instance
     * @see DataNumber
     */
    public int bitValue() {
        return bitValue;
    }

    /**
     * Returns byte value in int type, if byte value is greater than
     * {@linkplain Integer#MAX_VALUE}, return {@linkplain Integer#MAX_VALUE},
     * else return byte value in int type.
     *
     * @return byte value in int type
     */
    public int byteValueAsInt() {
        return byteValue > Integer.MAX_VALUE ? Integer.MAX_VALUE
                : (int) byteValue;
    }

    /**
     * Returns sum of bits of this instance.
     *
     * @return sum of bits of this instance, not null
     */
    public BigInteger sumBits() {
        if (this.lessThanAndEqualsTo(LONG_MAX)) {
            return new BigInteger(Long.toString(byteValue * Byte.SIZE
                    + bitValue));
        } else {
            return new BigInteger(Long.toString(byteValue)).multiply(
                    new BigInteger(Integer.toString(Byte.SIZE))).add(
                    new BigInteger(Integer.toString(bitValue)));
        }
    }

    /**
     * Returns sum of bits of this instance as long type, if the value is more
     * than max value of long type, {@link Long#MAX_VALUE} will be returned.
     *
     * @return sum of bits of this instance as long type, if the value is more
     *         than max value of long type, {@link Long#MAX_VALUE} will be
     *         returned
     */
    public long sumBitsAsLong() {
        if (this.lessThanAndEqualsTo(LONG_MAX)) {
            return byteValue * 8L + bitValue;
        } else {
            return Long.MAX_VALUE;
        }
    }

    /**
     * Returns whether total value of this instance is in integer byte bounds,
     * less than {@linkplain Integer#MAX_VALUE} byte, 0 bit.
     *
     * @return whether total value of this instance is in integer byte bounds
     */
    public boolean isInInteger() {
        return byteValue < Integer.MAX_VALUE
                || (byteValue == Integer.MAX_VALUE && bitValue == 0);
    }

    /**
     * Returns whether this instance is equal to 0 bits.
     *
     * @return whether this instance is equal to 0 bits
     */
    public boolean isZero() {
        return byteValue == 0L && bitValue == 0;
    }

    /**
     * Returns a new instance of which values is what this add given values.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @param bitValue
     *            given bit value, [0,7)
     * @return a new instance of which values is what this add given values, not
     *         null
     * @exception IllegalArgumentException
     *                thrown if given values are illegal
     * @exception ArithmeticException
     *                throw if result is overflow
     */
    public DataNumber add(final long byteValue, final int bitValue) {
        if (!checkValue(byteValue, bitValue)) {
            throw new IllegalArgumentException(
                    "Add failed: input byte value or bit value"
                            + " is illegal.");
        }
        long newByteValue = this.byteValue + byteValue;
        int newBitValue = this.bitValue + bitValue;
        if (newBitValue >= Byte.SIZE) {
            newByteValue++;
            newBitValue -= Byte.SIZE;
        }
        if (newByteValue < 0) {
            throw new ArithmeticException(
                    "Result overflow after add operation.");
        }
        return new DataNumber(newByteValue, newBitValue, false);
    }

    /**
     * Returns a new instance of which values is what this add given data
     * number.
     *
     * @param dn
     *            given data number, not null
     * @return a new instance of which values is what this add given data
     *         number, not null
     * @exception NullPointerException
     *                thrown if given object if null
     * @exception ArithmeticException
     *                throw if result is overflow
     */
    @Override
    public DataNumber add(final DataNumber dn) {
        return add(dn.byteValue, dn.bitValue);
    }

    /**
     * Returns a new instance of which values is what this add given byte value.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @return a new instance of which values is what this add given byte value,
     *         not null
     * @exception IllegalArgumentException
     *                thrown if byte value is illegal
     * @exception ArithmeticException
     *                throw if result is overflow
     */
    public DataNumber add(final long byteValue) {
        return add(byteValue, 0);
    }

    /**
     * Returns a new instance of which values is what this add given bit value.
     *
     * @param bitValue
     *            given bit value, [0,7)
     * @return a new instance of which values is what this add given bit value,
     *         not null
     * @exception IllegalArgumentException
     *                thrown if bit value is illegal
     * @exception ArithmeticException
     *                throw if result is overflow
     */
    public DataNumber add(final int bitValue) {
        return add(0L, bitValue);
    }

    /**
     * Returns a new instance of which values is what this subtract given
     * values.
     * <p>
     * <b>Note</b>: given values should be less than or equal to this instance.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @param bitValue
     *            given bit value, [0,7)
     * @return a new instance of which values is what this subtract given
     *         values, not null
     * @exception IllegalArgumentException
     *                thrown if given values are illegal or greater than this
     */
    public DataNumber subtract(final long byteValue, final int bitValue) {
        if (!checkValue(byteValue, bitValue)) {
            throw new IllegalArgumentException(
                    "Subtract failed: input byte value or bit value"
                            + " is illegal.");
        }
        if (lessThan(byteValue, bitValue, true)) {
            throw new IllegalArgumentException(
                    "Subtract failed: input byte value or bit value"
                            + " is less than this.");
        }
        long newByteValue = this.byteValue - byteValue;
        int newBitValue = this.bitValue - bitValue;
        if (newBitValue < 0) {
            newByteValue--;
            newBitValue += Byte.SIZE;
        }
        return new DataNumber(newByteValue, newBitValue, false);
    }

    /**
     * Returns a new instance of which values is what this subtract given data
     * number.
     * <p>
     * <b>Note</b>: given data number should be less than or equal to this
     * instance.
     *
     * @param dn
     *            given data number, not null
     * @return a new instance of which values is what this subtract given data
     *         number, not null
     * @exception NullPointerException
     *                thrown if given object is null
     * @exception IllegalArgumentException
     *                thrown if given data number are illegal or greater than
     *                this
     */
    @Override
    public DataNumber subtract(final DataNumber dn) {
        return subtract(dn.byteValue, dn.bitValue);
    }

    /**
     * Returns a new instance of which values is what this subtract given byte
     * value.
     * <p>
     * <b>Note</b>: given byte value should be less than or equal to this
     * instance.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @return a new instance of which values is what this subtract given byte
     *         value, not null
     * @exception IllegalArgumentException
     *                thrown if given byte value are illegal or greater than
     *                this
     */
    public DataNumber subtract(final long byteValue) {
        return subtract(byteValue, 0);
    }

    /**
     * Returns a new instance of which values is what this subtract given bit
     * value.
     * <p>
     * <b>Note</b>: given bit value should be less than or equal to this
     * instance.
     *
     * @param bitValue
     *            given bit value, [0,7)
     * @return a new instance of which values is what this subtract given bit
     *         value, not null
     * @exception IllegalArgumentException
     *                thrown if given bit value are illegal or greater than this
     */
    public DataNumber subtract(final int bitValue) {
        return subtract(0L, bitValue);
    }

    /**
     * Returns whether this instance is greater than or equal to given values.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @param bitValue
     *            given bit value, [0,7)
     * @param includeEqual
     *            indicates whether include equal to
     * @return true greater than, false else
     */
    private boolean greatThan(final long byteValue, final int bitValue,
            final boolean includeEqual) {
        return this.byteValue > byteValue ? true
                : (this.byteValue == byteValue ? (includeEqual ? this.bitValue >= bitValue
                        : this.bitValue > bitValue)
                        : false);
    }

    /**
     * Returns whether this instance is greater than given data number.
     *
     * @param dn
     *            given data number, not null
     * @return true if greater than, false else
     * @exception NullPointerException
     *                thrown if given object is null
     */
    public boolean greatThan(final DataNumber dn) {
        return greatThan(dn.byteValue, dn.bitValue, false);
    }

    /**
     * Returns whether this instance is greater than or equal to given data
     * number.
     *
     * @param dn
     *            given data number, not null
     * @return true if greater than or equal to, false else
     * @exception NullPointerException
     *                thrown if given object is null
     */
    public boolean greatThanAndEqualsTo(final DataNumber dn) {
        return greatThan(dn.byteValue, dn.bitValue, true);
    }

    /**
     * Returns whether this instance is less than or equal to given values.
     *
     * @param byteValue
     *            given byte value, [0,+)
     * @param bitValue
     *            given bit value, [0,7)
     * @param includeEqual
     *            indicates whether include equal to
     * @return true less than, false else
     */
    private boolean lessThan(final long byteValue, final int bitValue,
            final boolean includeEqual) {
        return this.byteValue < byteValue ? true
                : (this.byteValue == byteValue ? (includeEqual ? this.bitValue <= bitValue
                        : this.bitValue < bitValue)
                        : false);
    }

    /**
     * Returns whether this instance is less than given data number.
     *
     * @param dn
     *            given data number, not null
     * @return true if less than, false else
     * @exception NullPointerException
     *                thrown if given object is null
     */
    public boolean lessThan(final DataNumber dn) {
        return lessThan(dn.byteValue, dn.bitValue, false);
    }

    /**
     * Returns whether this instance is less than or equal to given data number.
     *
     * @param dn
     *            given data number, not null
     * @return true if less than or equal to, false else
     * @exception NullPointerException
     *                thrown if given object is null
     */
    public boolean lessThanAndEqualsTo(final DataNumber dn) {
        return lessThan(dn.byteValue, dn.bitValue, true);
    }

    /**
     * Returns the hash code of {@link #sumBits()}.
     *
     * @return the hash code of {@link #sumBits()}
     */
    @Override
    public int hashCode() {
        return sumBits().hashCode();
    }

    /**
     * Returns whether values of both data number are equal.
     *
     * @param obj
     *            given data number object
     * @return true if values are equal or both object is the same one
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        // Compare names in case of different class loader.
        if (getClass().getName().equals(obj.getClass().getName())) {
            final DataNumber dn = (DataNumber) obj;
            return this.byteValue == dn.byteValue
                    && this.bitValue == dn.bitValue;
        }
        return false;
    }

    /**
     * Clones a new instance from this.
     *
     * @return a new instance cloned from this, not null
     * @throws CloneNotSupportedException
     *             won't be thrown
     */
    @Override
    public final Object clone() throws CloneNotSupportedException {
        return new DataNumber(this.byteValue, this.bitValue, false);
    }

    /**
     * Returns string describes this format as:
     * <p>
     * DataNumber{bytes: xxx, bits: xxx, total bits: xxx}
     *
     * @return string describe this, not null
     */
    @Override
    public String toString() {
        return new StringBuilder().append("DataNumber{bytes: ")
                .append(byteValue).append(", bits: ").append(bitValue)
                .append(", total bits: ").append(sumBits()).append("}")
                .toString();
    }

    /**
     * Returns -1, 0, or 1 if this instance is less than, equal to, or greater
     * than given data number.
     *
     * @param dn
     *            given data number
     * @return -1, 0, or 1 if this instance is less than, equal to, or greater
     *         than given data number
     * @exception NullPointerException
     *                thrown if given data number is null
     */
    @Override
    public int compareTo(final DataNumber dn) {
        return naturalOrderingComparator().compare(this, dn);
    }

    /**
     * Returns default comparator of this class.
     * <p>
     * The method {@link Comparator#compare(Object, Object)} of this comparator
     * will return -1, 0, or 1 if first data number is less than, equal to, or
     * greater than second
     *
     * @return default comparator of this class, not null
     */
    public static Comparator<DataNumber> naturalOrderingComparator() {
        return (d1, d2) -> d1.lessThan(d2) ? -1 : (d1.equals(d2) ? 0 : 1);
    }
}
