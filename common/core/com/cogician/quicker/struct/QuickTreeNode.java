package com.cogician.quicker.struct;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.Quicker.Flow;
import com.cogician.quicker.util.QuickCollections;

/**
 * <p>
 * Represents a tree node.
 * </p>
 * 
 * @param <E>
 *            type of value of node
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-09T09:34:36+08:00
 * @since 0.0.0, 2016-05-09T09:34:36+08:00
 */
public class QuickTreeNode<E> extends AbstractNode<E> {

    private QuickTreeNode<E> parent;

    private List<QuickTreeNode<E>> children;

    private List<E> childrenValues;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public QuickTreeNode() {
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
    public QuickTreeNode(@Nullable E value) {
        super(value);
    }

    @Override
    protected QuickTreeNode<E> createNode(E value) {
        return new QuickTreeNode<E>(value);
    }

    /**
     * <p>
     * Gets parent node.
     * </p>
     * 
     * @return parent node
     * @since 0.0.0
     */
    public @Nullable QuickTreeNode<E> getParent() {
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
    public void setParent(@Nullable QuickTreeNode<E> parent) {
        this.parent = parent;
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
     * Returns whether this node has parent node.
     * </p>
     * 
     * @return whether this node has parent node
     * @since 0.0.0
     */
    public boolean hasParent() {
        return getParent() != null;
    }

    /**
     * <p>
     * Returns backed children list of this node. Any change of returned list will reflect this node, and vice versa.
     * </p>
     * <p>
     * Returned list can be modified but refuses null.
     * </p>
     * 
     * @return backed children list of this node
     * @since 0.0.0
     */
    public List<QuickTreeNode<E>> children() {
        if (children == null) {
            children = QuickCollections.elementsNonnullList(new ArrayList<>());
        }
        return children;
    }

    /**
     * <p>
     * Returns a list which is backed by children list. Returned list can provide value of node at specified index in
     * backed children list. Set or Add method will create new node. Any change of returned list will reflect backed
     * children list, and vice versa.
     * </p>
     * 
     * @return value list which is backed by children list
     * @since 0.0.0
     */
    public List<E> childrenValues() {
        if (childrenValues == null) {
            List<QuickTreeNode<E>> children = children();
            childrenValues = new AbstractList<E>() {

                @Override
                public E get(int index) {
                    return children.get(index).getValue();
                }

                @Override
                public int size() {
                    return children.size();
                }

                @Override
                public E set(int index, E element) {
                    return children.set(index, new QuickTreeNode<>(element)).getValue();
                }

                @Override
                public void add(int index, E element) {
                    children.add(index, new QuickTreeNode<>(element));
                }

                @Override
                public E remove(int index) {
                    return children.remove(index).getValue();
                }
            };
        }
        return childrenValues;
    }

    // public

    /**
     * <p>
     * Returns whether this node has at least one child node.
     * </p>
     * 
     * @return whether this node has at least one child node
     * @since 0.0.0
     */
    public boolean hasChild() {
        return Checker.isNotEmpty(children);
    }

    /**
     * <p>
     * Bidirectional-links this node and its child nodes. That means each child node will set its parent node to this
     * node.
     * </p>
     * 
     * @since 0.0.0
     */
    public void bidirectionalLink() {
        if (Checker.isNotEmpty(children())) {
            children().forEach(e -> {
                e.setParent(this);
            });
        }
    }

    /**
     * <p>
     * Returns root node of this node.
     * </p>
     * 
     * @return root node of this node
     * @since 0.0.0
     */
    public QuickTreeNode<E> getRoot() {
        QuickTreeNode<E> node = this;
        while (node.hasParent()) {
            node = node.getParent();
        }
        return node;
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
    public boolean isParentOf(@Nullable QuickTreeNode<E> child) {
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
    public boolean isChildOf(@Nullable QuickTreeNode<E> parent) {
        if (parent == null) {
            return false;
        }
        return parent.children().contains(this);
    }

    /**
     * <p>
     * Returns a flow to traverse children of this node in preorder.
     * </p>
     * 
     * @return a flow to traverse children of this node in preorder
     * @since 0.0.0
     */
    public Flow<QuickTreeNode<E>> flowPreorder() {
        return Quicker.flow(new PreorderIterator());
    }

    /**
     * <p>
     * Returns whether this node is a leaf node.
     * </p>
     * 
     * @return whether this node is a leaf node
     * @since 0.0.0
     */
    public boolean isLeaf() {
        return !hasChild();
    }

    /**
     * <p>
     * Returns a flow to traverse children of this node in postorder.
     * </p>
     * 
     * @return a flow to traverse children of this node in postorder
     * @since 0.0.0
     */
    public Flow<QuickTreeNode<E>> flowPostorder() {
        return Quicker.flow(new PostorderIterator());
    }

    /**
     * <p>
     * Returns a flow to traverse children of this node in level-order.
     * </p>
     * 
     * @return a flow to traverse children of this node in level-order
     * @since 0.0.0
     */
    public Flow<QuickTreeNode<E>> flowLevelorder() {
        return Quicker.flow(new LevelorderIterator());
    }

    /**
     * <p>
     * Returns a flow to traverse children of this node by level step.
     * </p>
     * 
     * @return a flow to traverse children of this node by level step
     * @since 0.0.0
     */
    public Flow<List<QuickTreeNode<E>>> flowLevel() {
        return Quicker.flow(new LevelIterator());
    }

    public int countDepth() {
        int depth = 0;
        int currentStack = 0;
        int countStack = 1;
        int levelLastStack = 1;
        Deque<QuickTreeNode<E>> queue = new LinkedList<>();
        queue.addLast(this);
        while (!queue.isEmpty()) {
            QuickTreeNode<E> node = queue.pollFirst();
            currentStack++;
            if (node.hasChild()) {
                queue.addAll(node.children());
                countStack += node.children().size();
            }
            if (currentStack == levelLastStack) {
                depth++;
                levelLastStack = countStack;
            }
        }
        return depth;
    }

    // public toBiTreeNode(){
    // QuickBiTreeNode<E> result = new QuickBiTreeNode<>();
    // }

    /**
     * <p>
     * Returns a string represents this node as:
     * 
     * <pre>
     * {parent, value, [children]}
     * </pre>
     * </p>
     * 
     * @return a string represents this node
     * @since 0.0.0
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{value:");
        sb.append(getValue());
        sb.append(",parent:");
        sb.append(hasParent() ? getParent().getValue() : null);
        sb.append(",childre:[");
        sb.append(hasChild() ? QuickCollections.join(childrenValues()) : "");
        sb.append("]");
        return sb.toString();
    }

    private class PreorderIterator implements Iterator<QuickTreeNode<E>> {

        private final Deque<QuickTreeNode<E>> stack = new LinkedList<>();

        private final List<QuickTreeNode<E>> temp = new ArrayList<>();

        PreorderIterator() {
            stack.addLast(QuickTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public QuickTreeNode<E> next() {
            if (!stack.isEmpty()) {
                QuickTreeNode<E> node = stack.pollLast();
                if (node.hasChild()) {
                    temp.addAll(node.children());
                    Collections.reverse(temp);
                    stack.addAll(temp);
                    temp.clear();
                }
                return node;
            }
            throw new NoSuchElementException();
        }
    }

    private class PostorderIterator implements Iterator<QuickTreeNode<E>> {

        private final Deque<QuickTreeNode<E>> stack = new LinkedList<>();

        private final List<QuickTreeNode<E>> temp = new ArrayList<>();

        @Nullable
        private QuickTreeNode<E> pre = null;

        PostorderIterator() {
            stack.addLast(QuickTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public QuickTreeNode<E> next() {
            while (!stack.isEmpty()) {
                QuickTreeNode<E> cur = stack.peekLast();
                if (cur.isLeaf() || cur.isParentOf(pre)) {
                    pre = cur;
                    stack.pollLast();
                    return cur;
                } else if (cur.hasChild()) {
                    temp.addAll(cur.children());
                    Collections.reverse(temp);
                    stack.addAll(temp);
                    temp.clear();
                }
            }
            throw new NoSuchElementException();
        }
    }

    private class LevelorderIterator implements Iterator<QuickTreeNode<E>> {

        private final Deque<QuickTreeNode<E>> queue = new LinkedList<>();

        LevelorderIterator() {
            queue.addLast(QuickTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public QuickTreeNode<E> next() {
            while (!queue.isEmpty()) {
                QuickTreeNode<E> node = queue.pollFirst();
                if (node.hasChild()) {
                    queue.addAll(node.children());
                }
                return node;
            }
            throw new NoSuchElementException();
        }
    }

    private class LevelIterator implements Iterator<List<QuickTreeNode<E>>> {

        private final Deque<QuickTreeNode<E>> queue = new LinkedList<>();
        private final Deque<QuickTreeNode<E>> next = new LinkedList<>();

        private int depth = 0;
        private int currentStack = 0;
        private int countStack = 1;
        private int levelLastStack = 1;

        LevelIterator() {
            queue.addLast(QuickTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public List<QuickTreeNode<E>> next() {
            while (!queue.isEmpty()) {
                QuickTreeNode<E> node = queue.pollFirst();
                next.addLast(node);
                currentStack++;
                if (node.hasChild()) {
                    queue.addAll(node.children());
                    countStack += node.children().size();
                }
                if (currentStack == levelLastStack) {
                    depth++;
                    levelLastStack = countStack;
                    List<QuickTreeNode<E>> result = Collections
                            .unmodifiableList(new ArrayList<>((LinkedList<QuickTreeNode<E>>)next));
                    next.clear();
                    return result;
                }
            }
            throw new NoSuchElementException();
        }
    }
}
