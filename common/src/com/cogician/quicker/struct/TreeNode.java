package com.cogician.quicker.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.util.QuiCollections;
import com.cogician.quicker.util.RandomQuicker;

/**
 * <p>
 * Node of a tree.
 * </p>
 * <p>
 * This class has add, set, remove and link methods to operate its child nodes. Especially, add methods only add child
 * nodes into current node but don't set its parent to current node; link method not only add child nodes but also set
 * its parent to current node (bidirectional link).
 * </p>
 * <p>
 * This class is cloneable and serializable.
 * </p>
 * 
 * @param <E>
 *            type of value of node
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-09T09:34:36+08:00
 * @since 0.0.0, 2016-05-09T09:34:36+08:00
 */
public class TreeNode<E> extends OfTree<E, TreeNode<E>> {

    private static final long serialVersionUID = 1L;

    @Nullable
    private List<TreeNode<E>> children;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public TreeNode() {
        super();
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
    public TreeNode(@Nullable E value) {
        super(value);
    }

    /**
     * <p>
     * Constructs an instance with specified value and child nodes. Child nodes and this node will be bidirectional
     * link.
     * </p>
     * 
     * @param value
     *            specified value
     * @param children
     *            child nodes
     * @since 0.0.0
     */
    public TreeNode(@Nullable E value, @Nullable List<TreeNode<E>> children) {
        super(value);
        if (Checker.isNotEmpty(children)) {
            this.children = QuiCollections.addAll(new ArrayList<>(), children);
        }
    }

    /**
     * <p>
     * Constructs an instance with specified value and child nodes. Child nodes and its parent (this) will be
     * bidirectional link.
     * </p>
     * 
     * @param value
     *            specified value
     * @param children
     *            child nodes
     * @since 0.0.0
     */
    @SafeVarargs
    public TreeNode(@Nullable E value, @Nullable TreeNode<E>... children) {
        super(value);
        if (Checker.isNotEmpty(children)) {
            this.children = QuiCollections.addAll(new LinkedList<>(), children);
        }
    }

    /**
     * <p>
     * Constructs an instance with specified value and values of child nodes. Child nodes and its parent (this) will be
     * bidirectional link.
     * </p>
     * 
     * @param value
     *            specified value
     * @param childrenValues
     *            values of child nodes
     * @since 0.0.0
     */
    @SafeVarargs
    public TreeNode(@Nullable E value, @Nullable E... childrenValues) {
        this(value, QuiCollections.convert(childrenValues, TreeNode.class, e -> new TreeNode<E>(e)));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Node<E>> T createNode(E value) {
        return (T)new TreeNode<E>(value);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Null node is permitted.
     * </p>
     * 
     * @return {@inheritDoc}
     * @since 0.0.0
     */
    @Override
    public int getChildrenNumber() {
        return children == null ? 0 : children.size();
    }

    @Override
    public void bidirectionalLink() {
        if (children != null) {
            getChildren().forEach(e -> {
                e.setParent(this);
            });
        }
    }

    /**
     * <p>
     * Returns backed children list of this node. Any change of returned list will reflect this node, and vice versa.
     * </p>
     * 
     * @return backed children list of this node
     * @since 0.0.0
     */
    public List<TreeNode<E>> getBackedChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    /**
     * <p>
     * Returns view of children list of this node. Using {@linkplain #getBackedChildren()} to get backed children list.
     * </p>
     * 
     * @return view of children list of this node
     * @since 0.0.0
     * @see #getBackedChildren()
     */
    public List<TreeNode<E>> getChildren() {
        return (Checker.isEmpty(children)) ? Collections.emptyList() : Collections.unmodifiableList(children);
    }

    /**
     * <p>
     * Gets first child node or null if this node doesn't have child node.
     * </p>
     * 
     * @return first child node or null if this node doesn't have child node
     * @since 0.0.0
     */
    public @Nullable TreeNode<E> getChild() {
        if (children == null) {
            return null;
        }
        return getChild(0);
    }

    private void checkNoChildren() {
        if (children == null) {
            throw new IndexOutOfBoundsException("No child nodes.");
        }
    }

    /**
     * <p>
     * Gets child node at specified index.
     * </p>
     * 
     * @param index
     *            specified index in bounds
     * @return child node at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public @Nullable TreeNode<E> getChild(int index) throws IndexOutOfBoundsException {
        checkNoChildren();
        return children.get(index);
    }

    /**
     * <p>
     * Random gets child node or null if this node doesn't have child node.
     * </p>
     * 
     * @return random child node or null if this node doesn't have child node
     * @since 0.0.0
     */
    public @Nullable TreeNode<E> getRandomChild() {
        if (children == null) {
            return null;
        }
        return getChild(RandomQuicker.nextInt(0, children.size()));
    }

    /**
     * <p>
     * Adds given child node. Null node is permitted.
     * </p>
     * 
     * @param child
     *            given child node
     * @since 0.0.0
     */
    public void addChild(@Nullable TreeNode<E> child) {
        getBackedChildren().add(child);
    }

    /**
     * <p>
     * Adds given child nodes in index order. Null node is permitted.
     * </p>
     * 
     * @param children
     *            given child nodes
     * @since 0.0.0
     */
    public void addChildren(@SuppressWarnings("unchecked") @Nullable TreeNode<E>... children) {
        if (Checker.isNotEmpty(children)) {
            getBackedChildren().addAll(Arrays.asList(children));
        }
    }

    /**
     * <p>
     * Adds given child nodes in index order. Null node is permitted.
     * </p>
     * 
     * @param children
     *            given child nodes
     * @since 0.0.0
     */
    public void addChildren(@Nullable List<TreeNode<E>> children) {
        if (Checker.isNotEmpty(children)) {
            getBackedChildren().addAll(children);
        }
    }

    /**
     * <p>
     * Adds given child node at specified index of children. If 0 <= index < number of child nodes, current node at
     * specified index and subsequence from here to tail will be shifted right; if index >= number of child nodes,
     * positions between tail and specified index will be filled null. Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param child
     *            given child node
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void addChild(int index, @Nullable TreeNode<E> child) throws IllegalArgumentException {
        Checker.checkPositiveOr0(index);
        List<TreeNode<E>> list = getBackedChildren();
        if (index >= list.size()) {
            QuiCollections.addAll(list, QuiCollections.multipleSingletonElementList(null, index - list.size()));
            list.add(child);
        } else {
            list.add(index, child);
        }
    }

    /**
     * <p>
     * Adds given child node at specified index of children. If 0 <= index < number of child nodes, current node at
     * specified index and subsequence from here to tail will be shifted right; if index >= number of child nodes,
     * positions between tail and specified index will be filled null. Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param children
     *            given child nodes
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void addChildren(int index, @SuppressWarnings("unchecked") @Nullable TreeNode<E>... children)
            throws IllegalArgumentException {
        if (Checker.isNotEmpty(children)) {
            Checker.checkPositiveOr0(index);
            List<TreeNode<E>> list = getBackedChildren();
            if (index >= list.size()) {
                QuiCollections.addAll(list,
                        QuiCollections.multipleSingletonElementList(null, index - list.size()));
                list.addAll(Arrays.asList(children));
            } else {
                list.addAll(index, Arrays.asList(children));
            }
        }
    }

    /**
     * <p>
     * Adds given child node at specified index of children. If 0 <= index < number of child nodes, current node at
     * specified index and subsequence from here to tail will be shifted right; if index >= number of child nodes,
     * positions between tail and specified index will be filled null. Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param children
     *            given child nodes
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void addChildren(int index, @Nullable List<TreeNode<E>> children) throws IllegalArgumentException {
        if (Checker.isNotEmpty(children)) {
            Checker.checkPositiveOr0(index);
            List<TreeNode<E>> list = getBackedChildren();
            if (index >= list.size()) {
                QuiCollections.addAll(list,
                        QuiCollections.multipleSingletonElementList(null, index - list.size()));
                list.addAll(children);
            } else {
                list.addAll(index, children);
            }
        }
    }

    /**
     * <p>
     * Returns read-only view of values of children list of this node. If a node is null, its value is null.
     * </p>
     * 
     * @return read-only view of values of children list of this node
     * @since 0.0.0
     */
    public List<E> getChildrenValue() {
        return Collections.unmodifiableList(QuiCollections.convert(new ArrayList<>(), children, n -> getValue(n)));
    }

    /**
     * <p>
     * Gets value of first child node or null if this node doesn't have child node or that first node is null.
     * </p>
     * 
     * @return value of first child node or null if this node doesn't have child node or that first node is null
     * @since 0.0.0
     */
    public @Nullable E getChildValue() {
        return getValue(getChild());
    }

    /**
     * <p>
     * Gets value of child node at specified index or null if that node is null.
     * </p>
     * 
     * @param index
     *            specified index in bounds
     * @return value of child node of specified index or null if that node is null
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public @Nullable E getChildValue(int index) throws IndexOutOfBoundsException {
        return getValue(getChild(index));
    }

    /**
     * <p>
     * Random gets value of child node or null if this node doesn't have child node or that random node is null.
     * </p>
     * 
     * @return value of random child node or null if this node doesn't have child node or that random node is null
     * @since 0.0.0
     */
    public @Nullable E getRandomChildValue() {
        return getValue(getRandomChild());
    }

    /**
     * <p>
     * Adds a new child node created by given value.
     * </p>
     * 
     * @param value
     *            given value
     * @since 0.0.0
     */
    public void addChildByValue(@Nullable E value) {
        addChild(createNode(value));
    }

    /**
     * <p>
     * Adds child nodes created by given values in index order.
     * </p>
     * 
     * @param values
     *            given values
     * @since 0.0.0
     */
    public void addChildrenByValues(@SuppressWarnings("unchecked") @Nullable E... values) {
        if (Checker.isNotEmpty(values)) {
            Quicker.each(values, v -> {
                addChildByValue(v);
            });
        }
    }

    /**
     * <p>
     * Adds child nodes created by given values in index order.
     * </p>
     * 
     * @param values
     *            given values
     * @since 0.0.0
     */
    public void addChildrenByValues(@Nullable List<E> values) {
        if (Checker.isNotEmpty(values)) {
            Quicker.each(values, v -> {
                addChildByValue(v);
            });
        }
    }

    /**
     * <p>
     * Adds child node created by given value at specified index of children. If 0 <= index < number of child nodes,
     * current node at specified index and subsequence from here to tail will be shifted right; if index >= number of
     * child nodes, positions between tail of child nodes and specified index will be filled null.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void addChildByValue(int index, @Nullable E value) throws IllegalArgumentException {
        Checker.checkPositiveOr0(index);
        List<TreeNode<E>> list = getBackedChildren();
        if (index >= list.size()) {
            QuiCollections.addAll(list, QuiCollections.multipleSingletonElementList(null, index - list.size()));
            list.add(createNode(value));
        } else {
            list.add(index, createNode(value));
        }
    }

    /**
     * <p>
     * Adds child nodes created by given values at specified index of children in index order. If 0 <= index < number of
     * child nodes, current node at specified index and subsequence from here to tail will be shifted right; if index >=
     * number of child nodes, positions between tail of child nodes and specified index will be filled null.
     * </p>
     * 
     * @param index
     *            specified index
     * @param values
     *            given values
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void addChildrenByValues(int index, @SuppressWarnings("unchecked") @Nullable E... values)
            throws IllegalArgumentException {
        Checker.checkPositiveOr0(index);
        List<TreeNode<E>> list = getBackedChildren();
        if (index >= list.size()) {
            QuiCollections.addAll(list, QuiCollections.multipleSingletonElementList(null, index - list.size()));
            Quicker.each(values, v -> addChildByValue(v));
        } else {
            list.addAll(index, QuiCollections.convert(new ArrayList<>(), Arrays.asList(values), v -> createNode(v)));
        }
    }

    /**
     * <p>
     * Adds child nodes created by given values at specified index of children in index order. If 0 <= index < number of
     * child nodes, current node at specified index and subsequence from here to tail will be shifted right; if index >=
     * number of child nodes, positions between tail of child nodes and specified index will be filled null.
     * </p>
     * 
     * @param index
     *            specified index
     * @param values
     *            given values
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void addChildrenByValues(int index, @Nullable List<E> values) throws IllegalArgumentException {
        Checker.checkPositiveOr0(index);
        List<TreeNode<E>> list = getBackedChildren();
        if (index >= list.size()) {
            QuiCollections.addAll(list, QuiCollections.multipleSingletonElementList(null, index - list.size()));
            Quicker.each(values, v -> addChildByValue(v));
        } else {
            list.addAll(index, QuiCollections.convert(new ArrayList<>(), values, v -> createNode(v)));
        }
    }

    /**
     * <p>
     * Sets given child node at specified index of children. Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param child
     *            given child node
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public void setChild(int index, @Nullable TreeNode<E> child) throws IndexOutOfBoundsException {
        Checker.checkIndex(index, children);
        children.set(index, child);
    }

    /**
     * <p>
     * Sets a new child node created by given value at specified index.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public void setChildByValue(int index, @Nullable E value) throws IndexOutOfBoundsException {
        Checker.checkIndex(index, children);
        children.set(index, createNode(value));
    }

    /**
     * <p>
     * Adds and bidirectional links given child node. Null node is permitted.
     * </p>
     * 
     * @param child
     *            given child node
     * @since 0.0.0
     */
    public void linkChild(@Nullable TreeNode<E> child) {
        addChild(child);
        if (child != null) {
            child.setParent(this);
        }
    }

    /**
     * <p>
     * Adds and bidirectional links given child nodes. Null node is permitted.
     * </p>
     * 
     * @param children
     *            given child nodes
     * @since 0.0.0
     */
    public void linkChildren(@SuppressWarnings("unchecked") @Nullable TreeNode<E>... children) {
        addChildren(children);
        Quicker.each(children, c -> {
            c.setParent(this);
        });
    }

    /**
     * <p>
     * Adds and bidirectional links given child nodes. Null node is permitted.
     * </p>
     * 
     * @param children
     *            given child nodes
     * @since 0.0.0
     */
    public void linkChildren(@Nullable List<TreeNode<E>> children) {
        addChildren(children);
        Quicker.each(children, c -> {
            c.setParent(this);
        });
    }

    /**
     * <p>
     * Adds and bidirectional links given child node at specified index of children. If 0 <= index < number of child
     * nodes, current node at specified index and subsequence from here to tail will be shifted right; if index >=
     * number of child nodes, positions between tail and specified index will be filled null. Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param child
     *            given child node
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void linkChild(int index, @Nullable TreeNode<E> child) throws IllegalArgumentException {
        addChild(index, child);
        if (child != null) {
            child.setParent(this);
        }
    }

    /**
     * <p>
     * Bidirectional links given child nodes at specified index of children in index order. If 0 <= index < number of
     * child nodes, current node at specified index and subsequence from here to tail will be shifted right; if index >=
     * number of child nodes, positions between tail and specified index will be filled null. Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param children
     *            given child nodes
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void linkChildren(int index, @SuppressWarnings("unchecked") @Nullable TreeNode<E>... children)
            throws IllegalArgumentException {
        addChildren(index, children);
        Quicker.each(children, c -> {
            c.setParent(this);
        });
    }

    /**
     * <p>
     * Bidirectional links given child nodes at specified index of children in index order. If 0 <= index < number of
     * child nodes, current node at specified index and subsequence from here to tail will be shifted right; if index >=
     * number of child nodes, positions between tail and specified index will be filled null. Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param children
     *            given child nodes
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void linkChildren(int index, @Nullable List<TreeNode<E>> children) throws IllegalArgumentException {
        addChildren(index, children);
        Quicker.each(children, c -> {
            c.setParent(this);
        });
    }

    /**
     * <p>
     * Adds and bidirectional links child node created by given value.
     * </p>
     * 
     * @param value
     *            given value
     * @since 0.0.0
     */
    public void linkChildByValue(@Nullable E value) {
        linkChild(createNode(value));
    }

    /**
     * <p>
     * Adds and bidirectional links child nodes created by given values in index order.
     * </p>
     * 
     * @param values
     *            given values
     * @since 0.0.0
     */
    public void linkChildrenByValues(@SuppressWarnings("unchecked") @Nullable E... values) {
        TreeNode<E>[] array = QuiCollections.convert(values, TreeNode.class, v -> createNode(v));
        linkChildren(array);
    }

    /**
     * <p>
     * Adds and bidirectional links child nodes created by given values in index order.
     * </p>
     * 
     * @param values
     *            given values
     * @since 0.0.0
     */
    public void linkChildrenByValues(@Nullable List<E> values) {
        List<TreeNode<E>> list = QuiCollections.convert(new ArrayList<>(), values, v -> createNode(v));
        linkChildren(list);
    }

    /**
     * <p>
     * Adds and bidirectional links child node created by given value at specified index of children. If 0 <= index <
     * number of child nodes, current node at specified index and subsequence from here to tail will be shifted right;
     * if index >= number of child nodes, positions between tail and specified index will be filled null. Null node is
     * permitted.
     * </p>
     * <p>
     * If given value is null, it will add a null node.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void linkChildByValue(int index, @Nullable E value) throws IllegalArgumentException {
        Checker.checkPositiveOr0(index);
        linkChild(index, createNode(value));
    }

    /**
     * <p>
     * Adds and bidirectional links child nodes created by given values at specified index of children in index order.
     * If 0 <= index < number of child nodes, current node at specified index and subsequence from here to tail will be
     * shifted right; if index >= number of child nodes, positions between tail and specified index will be filled null.
     * Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param values
     *            given values
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void linkChildrenByValues(int index, @SuppressWarnings("unchecked") @Nullable E... values)
            throws IllegalArgumentException {
        Checker.checkPositiveOr0(index);
        TreeNode<E>[] array = QuiCollections.convert(values, TreeNode.class, v -> createNode(v));
        linkChildren(index, array);
    }

    /**
     * <p>
     * Adds and bidirectional links child nodes created by given values at specified index of children in index order.
     * If 0 <= index < number of child nodes, current node at specified index and subsequence from here to tail will be
     * shifted right; if index >= number of child nodes, positions between tail and specified index will be filled null.
     * Null node is permitted.
     * </p>
     * 
     * @param index
     *            specified index
     * @param values
     *            given values
     * @throws IllegalArgumentException
     *             if index is negative
     * @since 0.0.0
     */
    public void linkChildrenByValues(int index, @Nullable List<E> values) throws IllegalArgumentException {
        Checker.checkPositiveOr0(index);
        List<TreeNode<E>> list = QuiCollections.convert(new ArrayList<>(), values, v -> createNode(v));
        linkChildren(index, list);
    }

    /**
     * <p>
     * Removes child node at specified index.
     * </p>
     * 
     * @param index
     *            specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public void removeChild(int index) throws IndexOutOfBoundsException {
        getBackedChildren().remove(index);
    }

    /**
     * <p>
     * Removes child nodes between given from index inclusive and end of children list.
     * </p>
     * 
     * @param from
     *            given from index inclusive
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public void removeChildren(int from) throws IndexOutOfBoundsException {
        List<TreeNode<E>> list = getBackedChildren();
        list.subList(from, list.size()).clear();
    }

    /**
     * <p>
     * Removes child nodes between given from index inclusive and given to index exclusive.
     * </p>
     * 
     * @param from
     *            given from index inclusive
     * @param to
     *            given to index exclusive
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public void removeChildren(int from, int to) throws IndexOutOfBoundsException {
        List<TreeNode<E>> list = getBackedChildren();
        list.subList(from, to).clear();
    }

    @Override
    public boolean isLeaf() {
        return Checker.isEmpty(children);
    }

    @Override
    public Iterator<TreeNode<E>> iteratorInPreorder() {
        return new PreorderIterator();
    }

    @Override
    public Iterator<TreeNode<E>> iteratorInPostorder() {
        return new PostorderIterator();
    }

    @Override
    public Iterator<TreeNode<E>> iteratorInLevelorder() {
        return new LevelorderIterator();
    }

    @Override
    public Iterator<List<TreeNode<E>>> levelIterator() {
        return new LevelIterator();
    }

    @Override
    public int countDepth() {
        int depth = 0;
        int currentStack = 0;
        int countStack = 1;
        int levelLastStack = 1;
        Deque<TreeNode<E>> queue = new LinkedList<>();
        queue.addLast(this);
        while (!queue.isEmpty()) {
            TreeNode<E> node = queue.pollFirst();
            currentStack++;
            if (node.hasChild()) {
                queue.addAll(node.getBackedChildren());
                countStack += node.getChildrenNumber();
            }
            if (currentStack == levelLastStack) {
                depth++;
                levelLastStack = countStack;
            }
        }
        return depth;
    }

    class PreorderIterator implements Iterator<TreeNode<E>> {

        private final Deque<TreeNode<E>> stack = new LinkedList<>();

        private final List<TreeNode<E>> temp = new ArrayList<>();

        PreorderIterator() {
            stack.addLast(TreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public TreeNode<E> next() {
            if (!stack.isEmpty()) {
                TreeNode<E> node = stack.pollLast();
                if (node.hasChild()) {
                    temp.addAll(node.getBackedChildren());
                    Collections.reverse(temp);
                    stack.addAll(temp);
                    temp.clear();
                }
                return node;
            }
            throw new NoSuchElementException();
        }
    }

    class PostorderIterator implements Iterator<TreeNode<E>> {

        private final Deque<TreeNode<E>> stack = new LinkedList<>();

        private final List<TreeNode<E>> temp = new ArrayList<>();

        @Nullable
        private TreeNode<E> pre = null;

        PostorderIterator() {
            stack.addLast(TreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public TreeNode<E> next() {
            while (!stack.isEmpty()) {
                TreeNode<E> cur = stack.peekLast();
                if (cur.isLeaf() || cur.isParentOf(pre)) {
                    pre = cur;
                    stack.pollLast();
                    return cur;
                } else if (cur.hasChild()) {
                    temp.addAll(cur.getBackedChildren());
                    Collections.reverse(temp);
                    stack.addAll(temp);
                    temp.clear();
                }
            }
            throw new NoSuchElementException();
        }
    }

    class LevelorderIterator implements Iterator<TreeNode<E>> {

        private final Deque<TreeNode<E>> queue = new LinkedList<>();

        LevelorderIterator() {
            queue.addLast(TreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public TreeNode<E> next() {
            while (!queue.isEmpty()) {
                TreeNode<E> node = queue.pollFirst();
                if (node.hasChild()) {
                    queue.addAll(node.getBackedChildren());
                }
                return node;
            }
            throw new NoSuchElementException();
        }
    }

    class LevelIterator implements Iterator<List<TreeNode<E>>> {

        private final Deque<TreeNode<E>> queue = new LinkedList<>();
        private final Deque<TreeNode<E>> next = new LinkedList<>();

        private int depth = 0;
        private int currentStack = 0;
        private int countStack = 1;
        private int levelLastStack = 1;

        LevelIterator() {
            queue.addLast(TreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public List<TreeNode<E>> next() {
            while (!queue.isEmpty()) {
                TreeNode<E> node = queue.pollFirst();
                next.addLast(node);
                currentStack++;
                if (node.hasChild()) {
                    queue.addAll(node.getBackedChildren());
                    countStack += node.getChildrenNumber();
                }
                if (currentStack == levelLastStack) {
                    depth++;
                    levelLastStack = countStack;
                    List<TreeNode<E>> result = Collections
                            .unmodifiableList(new ArrayList<>((LinkedList<TreeNode<E>>)next));
                    next.clear();
                    return result;
                }
            }
            throw new NoSuchElementException();
        }
    }
}
