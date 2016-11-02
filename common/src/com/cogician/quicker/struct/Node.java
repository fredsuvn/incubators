package com.cogician.quicker.struct;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Base node class, including a value and getter and setter of the value. Node class represents a node in a structure
 * such as linked list, tree or bin-tree.
 * </p>
 *
 * @param <E>
 *            type of value
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-21T17:01:04+08:00
 * @since 0.0.0, 2016-04-21T17:01:04+08:00
 */
public abstract class Node<E> implements ValueWrapper<E> {

    @Nullable
    private E value;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public Node() {

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
    public Node(@Nullable E value) {
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
     * Returns whether value of given node is equal to of this. If given node is null, return false.
     * </p>
     * 
     * @param node
     *            given node
     * @return whether value of given node is equal to of this
     * @since 0.0.0
     */
    public boolean valueEqual(@Nullable Node<E> node) {
        if (this == node) {
            return true;
        }
        if (node == null) {
            return false;
        }
        return valueEqual(node.getValue());
    }

    /**
     * <p>
     * Returns whether given value is equal to value of this node.
     * </p>
     * 
     * @param value
     *            given value
     * @return whether given value is equal to value of this node
     * @since 0.0.0
     */
    public boolean valueEqual(@Nullable E value) {
        return Checker.isEqual(getValue(), value);
    }

    /**
     * <p>
     * Creates a new node instance of which value is given value. Null value is permitted.
     * </p>
     * 
     * @param <T>
     *            type of current node
     * @param value
     *            given value
     * @return a new node instance
     * @since 0.0.0
     */
    protected abstract @Nullable <T extends Node<E>> T createNode(@Nullable E value);

    /**
     * <p>
     * Returns value of given node. If given node is null, return null.
     * </p>
     * 
     * @param node
     *            given node
     * @return value of given node
     * @since 0.0.0
     */
    protected @Nullable <T extends Node<E>> E getValue(@Nullable T node) {
        return node == null ? null : node.getValue();
    }
}
