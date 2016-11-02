package com.cogician.quicker;

/**
 * <p>
 * This interface is used to element-ize specified portion of total content or merge specified count of elements as one
 * element. It can element-ize an non-element-ized instance like instance of binary data, or merge elements of an
 * element-ized instance like instance of list. For example：
 * 
 * <pre>
 * A block of binary:
 * 
 * 0x01 0x02 0x03 0x04
 * 
 * An implementation of which elementize(2) can set 2 bytes as an element, then:
 * 
 * element 0: 0x01 0x02
 * element 1: 0x03 0x04
 * 
 * Or, a list has 4 elements:
 * 
 * element 0: A
 * element 1: B
 * element 2: C
 * element 3: D
 * 
 * An implementation of which elementize(2) can merge 2 element as one element, then:
 * 
 * element 0: A, B
 * element 1: C, D
 * </pre>
 * 
 * </p>
 * <p>
 * This interface has two sets of methods: one in int and the other in long. The different between those is range, long
 * being greater then int.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-29T08:58:08+08:00
 * @since 0.0.0, 2016-02-29T08:58:08+08:00
 */
public interface Elementize {

    /**
     * <p>
     * Element-izes portion of total content or merges elements as one element in specified length.
     * </p>
     * 
     * @param length
     *            specified length, positive
     * @throws IllegalArgumentException
     *             if specified length is not positive
     * @since 0.0.0
     */
    public void elementize(int length) throws IllegalArgumentException;

    /**
     * <p>
     * Returns element-ized width.
     * </p>
     * 
     * @return element-ized width
     * @since 0.0.0
     */
    public int getElementizedWidth();

    /**
     * <p>
     * Element-izes portion of total content or merges elements as one element in specified length.
     * </p>
     * 
     * @param length
     *            specified length, positive
     * @throws IllegalArgumentException
     *             if specified length is not positive
     * @since 0.0.0
     */
    public void elementize(long length) throws IllegalArgumentException;

    /**
     * <p>
     * Returns element-ized width.
     * </p>
     * 
     * @return element-ized width
     * @since 0.0.0
     */
    public long getLongElementizedWidth();
}
