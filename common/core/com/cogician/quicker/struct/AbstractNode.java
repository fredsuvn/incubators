package com.cogician.quicker.struct;

import javax.annotation.Nullable;

/**
 * <p>
 * Base abstract node class.
 * </p>
 *
 * @param <E>
 *            type of value
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-21T17:01:04+08:00
 * @since 0.0.0, 2016-04-21T17:01:04+08:00
 */
public abstract class AbstractNode<E> implements QuickWrapper<E> {

    private E value;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    protected AbstractNode() {

    }

    /**
     * <p>
     * Constructs an instance with specified value.
     * </p>
     * 
     * @param value
     *            specified value
     * @since 0.0.0
     */
    protected AbstractNode(@Nullable E value) {
        setValue(value);
    }

    /**
     * <p>
     * Gets value of this node.
     * </p>
     * 
     * @return value of this node
     * @since 0.0.0
     */
    public @Nullable E getValue() {
        return value;
    }

    /**
     * <p>
     * Sets value of this node.
     * </p>
     * 
     * @param value
     *            value of this node
     * @since 0.0.0
     */
    public void setValue(@Nullable E value) {
        this.value = value;
    }

    /**
     * <p>
     * Creates a new node by given value. Null value is permitted.
     * </p>
     * 
     * @param <T>
     *            runtime type of node
     * @param value
     *            given value
     * @return a new node instance
     * @since 0.0.0
     */
    protected abstract @Nullable AbstractNode<E> createNode(@Nullable E value);
}
