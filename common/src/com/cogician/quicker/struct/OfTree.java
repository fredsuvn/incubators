package com.cogician.quicker.struct;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

/**
 * <p>
 * Base tree node which has a parent node and some child nodes. Tree node is commonly used in tree structure.
 * </p>
 * <p>
 * This class is cloneable and serializable.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-06-29T09:41:08+08:00
 * @since 0.0.0, 2016-06-29T09:41:08+08:00
 */
abstract class OfTree<E, T extends OfTree<E, T>> extends Node<E> implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    @Nullable
    private T parent = null;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    OfTree() {
        this(null);
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
    OfTree(@Nullable E value) {
        super(value);
    }

    /**
     * <p>
     * Gets parent node.
     * </p>
     * 
     * @return parent node
     * @since 0.0.0
     */
    public @Nullable T getParent() {
        return parent;
    }

    /**
     * <p>
     * Sets parent node.
     * </p>
     * 
     * @param parent
     *            parent node
     * @since 0.0.0
     */
    public void setParent(@Nullable T parent) {
        this.parent = parent;
    }

    /**
     * <p>
     * Gets value of parent node.
     * </p>
     * 
     * @return value of parent node
     * @since 0.0.0
     */
    public @Nullable E getParentValue() {
        return getValue(getParent());
    }

    /**
     * <p>
     * Sets parent node created by given value. If given value is null, create and set an empty node.
     * </p>
     * 
     * @param parentValue
     *            given value
     * @since 0.0.0
     */
    public void setParentByValue(@Nullable E parentValue) {
        setParent(createNode(parentValue));
    }

    /**
     * <p>
     * Returns number of child nodes of this node.
     * </p>
     * 
     * @return number of child nodes of this node
     * @since 0.0.0
     */
    public abstract int getChildrenNumber();

    /**
     * <p>
     * Bidirectional-links this node and its child nodes. Each child node will set its parent node to this node.
     * </p>
     * 
     * @since 0.0.0
     */
    public abstract void bidirectionalLink();

    /**
     * <p>
     * Returns root node of this node.
     * </p>
     * 
     * @return root node of this node
     * @since 0.0.0
     */
    public T getRoot() {
        @SuppressWarnings("unchecked")
        T child = (T)this;
        T parent = child.getParent();
        while (parent != null) {
            child = parent;
            parent = parent.getParent();
        }
        return child;
    }

    /**
     * <p>
     * Returns whether this node is root node (no parent node).
     * </p>
     * 
     * @return whether this node is root node (no parent node)
     * @since 0.0.0
     */
    public boolean isRoot() {
        return getParent() == null;
    }

    /**
     * <p>
     * Returns whether this node has a parent node.
     * </p>
     * 
     * @return whether this node has a parent node
     * @since 0.0.0
     */
    public boolean isChild() {
        return getParent() != null;
    }

    /**
     * <p>
     * Returns whether this node is parent node of given node.
     * </p>
     * 
     * @param child
     *            given node
     * @return whether this node is parent node of given node
     * @since 0.0.0
     */
    public boolean isParentOf(@Nullable T child) {
        if (child == null) {
            return false;
        }
        return child.getParent() == this;
    }

    /**
     * <p>
     * Returns whether this node is child node of given node.
     * </p>
     * 
     * @param parent
     *            given node
     * @return whether this node is child node of given node
     * @since 0.0.0
     */
    public boolean isChildOf(@Nullable T parent) {
        if (parent == null) {
            return false;
        }
        return getParent() == parent;
    }

    /**
     * <p>
     * Returns whether this node has at least one child node.
     * </p>
     * 
     * @return whether this node has at least one child node
     * @since 0.0.0
     */
    public boolean isParent() {
        return !isLeaf();
    }

    /**
     * <p>
     * Returns whether this node has no child node.
     * </p>
     * 
     * @return whether this node has no child node
     * @since 0.0.0
     */
    abstract public boolean isLeaf();

    /**
     * <p>
     * Returns whether this node has parent node.
     * </p>
     * 
     * @return whether this node has parent node
     * @since 0.0.0
     */
    public boolean hasParent() {
        return isChild();
    }

    /**
     * <p>
     * Returns whether this node has child node.
     * </p>
     * 
     * @return whether this node has child node
     * @since 0.0.0
     */
    public boolean hasChild() {
        return isParent();
    }

    /**
     * <p>
     * Returns an iterator to iterate each node under this node which is considered as root in preorder.
     * </p>
     * 
     * @return an iterator to iterate each node under this node which is considered as root in preorder
     * @since 0.0.0
     */
    public abstract Iterator<T> iteratorInPreorder();

    /**
     * <p>
     * Returns an iterator to iterate each node under this node which is considered as root in postorder.
     * </p>
     * 
     * @return an iterator to iterate each node under this node which is considered as root in postorder
     * @since 0.0.0
     */
    public abstract Iterator<T> iteratorInPostorder();

    /**
     * <p>
     * Returns an iterator to iterate each node under this node which is considered as root in level-order.
     * </p>
     * 
     * @return an iterator to iterate each node under this node which is considered as root in level-order
     * @since 0.0.0
     */
    public abstract Iterator<T> iteratorInLevelorder();

    /**
     * <p>
     * Returns a list iterator to iterate each level under this node which is considered as root.
     * </p>
     * 
     * @return a list iterator to iterate each level under this node which is considered as root
     * @since 0.0.0
     */
    public abstract Iterator<List<T>> levelIterator();

    /**
     * <p>
     * Counts depth under this node. This node is depth 1.
     * </p>
     * 
     * @return depth under this node
     * @since 0.0.0
     */
    public abstract int countDepth();

    @Override
    public String toString() {
        return getClass() + "{id=" + System.identityHashCode(this) + ",value=" + getValue() + "}";
    }
}
