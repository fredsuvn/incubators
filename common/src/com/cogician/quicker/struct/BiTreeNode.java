package com.cogician.quicker.struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * <p>
 * Node of a binary tree.
 * </p>
 * <p>
 * This class has set and link methods to operate its child nodes. Especially, set methods only set left and right nodes
 * but don't set those parent to current node; link method not only set child nodes but also set those parent to current
 * node (bidirectional link).
 * </p>
 * <p>
 * This class is cloneable and serializable.
 * </p>
 * 
 * @param <E>
 *            type of value of node
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-20T09:36:22+08:00
 * @since 0.0.0, 2016-04-20T09:36:22+08:00
 */
public class BiTreeNode<E> extends OfTree<E, BiTreeNode<E>> {

    private static final long serialVersionUID = 1L;

    @Nullable
    private BiTreeNode<E> left;

    @Nullable
    private BiTreeNode<E> right;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public BiTreeNode() {
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
    public BiTreeNode(@Nullable E value) {
        super(value);
    }

    /**
     * <p>
     * Constructs an instance with given value, left node and right node. Child nodes and its parent (this) will be
     * bidirectional link.
     * </p>
     * 
     * @param value
     *            given value
     * @param left
     *            left node
     * @param right
     *            right node
     * @since 0.0.0
     */
    public BiTreeNode(@Nullable E value, @Nullable BiTreeNode<E> left, @Nullable BiTreeNode<E> right) {
        super(value);
        linkLeft(left);
        linkRight(right);
    }

    /**
     * <p>
     * Constructs an instance with given value, value of previous node and value of next node. Child nodes and its
     * parent (this) will be bidirectional link.
     * </p>
     * 
     * @param value
     *            given value
     * @param leftValue
     *            value of left node
     * @param rightValue
     *            value of right node
     * @since 0.0.0
     */
    public BiTreeNode(@Nullable E value, @Nullable E leftValue, @Nullable E rightValue) {
        super(value);
        linkLeft(createNode(leftValue));
        linkRight(createNode(rightValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Node<E>> T createNode(E value) {
        return (T)new BiTreeNode<E>(value);
    }

    /**
     * {@inheritDoc}
     * <p>
     * For bi-tree node, it returns number of non-null child nodes of this nodes.
     * </p>
     * 
     * @return number of non-null child nodes of this nodes
     * @since 0.0.0
     */
    @Override
    public int getChildrenNumber() {
        if (getLeft() == null && getRight() == null) {
            return 0;
        } else if (getLeft() == null || getRight() == null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void bidirectionalLink() {
        BiTreeNode<E> l = getLeft();
        BiTreeNode<E> r = getRight();
        if (l != null) {
            l.setParent(this);
        }
        if (r != null) {
            r.setParent(this);
        }
    }

    /**
     * <p>
     * Gets left node.
     * </p>
     * 
     * @return left node
     * @since 0.0.0
     */
    public @Nullable BiTreeNode<E> getLeft() {
        return left;
    }

    /**
     * <p>
     * set left node.
     * </p>
     * 
     * @param left
     *            left node
     * @since 0.0.0
     */
    public void setLeft(@Nullable BiTreeNode<E> left) {
        this.left = left;
    }

    /**
     * <p>
     * Gets value of left node. If left node is null, return null.
     * </p>
     * 
     * @return value of left node
     * @since 0.0.0
     */
    public @Nullable E getLeftValue() {
        return getValue(getLeft());
    }

    /**
     * <p>
     * Sets left node created by given value.
     * </p>
     * 
     * @param leftValue
     *            given value
     * @since 0.0.0
     */
    public void setLeftByValue(@Nullable E leftValue) {
        setLeft(createNode(leftValue));
    }

    /**
     * <p>
     * Gets right node.
     * </p>
     * 
     * @return right node
     * @since 0.0.0
     */
    public @Nullable BiTreeNode<E> getRight() {
        return right;
    }

    /**
     * <p>
     * set right node.
     * </p>
     * 
     * @param right
     *            right node
     * @since 0.0.0
     */
    public void setRight(@Nullable BiTreeNode<E> right) {
        this.right = right;
    }

    /**
     * <p>
     * Gets value of right node. If right node is null, return null.
     * </p>
     * 
     * @return value of right node
     * @since 0.0.0
     */
    public @Nullable E getRightValue() {
        return getValue(getRight());
    }

    /**
     * <p>
     * Sets right node created by given value.
     * </p>
     * 
     * @param rightValue
     *            given value
     * @since 0.0.0
     */
    public void setRightByValue(@Nullable E rightValue) {
        setRight(createNode(rightValue));
    }

    private static <E> void bidirectionalLinkLeft(@Nullable BiTreeNode<E> asParent, @Nullable BiTreeNode<E> asLeft) {
        if (asParent != null) {
            asParent.setLeft(asLeft);
        }
        if (asLeft != null) {
            asLeft.setParent(asParent);
        }
    }

    private static <E> void bidirectionalLinkRight(@Nullable BiTreeNode<E> asParent, @Nullable BiTreeNode<E> asRight) {
        if (asParent != null) {
            asParent.setRight(asRight);
        }
        if (asRight != null) {
            asRight.setParent(asParent);
        }
    }

    /**
     * <p>
     * Bidirectional-links given node as left node of this node. If given node is null, the left node of this node will
     * be set null. Linked given node or null (if given node is null) will be returned.
     * </p>
     * 
     * @param left
     *            given node as left node of this node
     * @return linked given node or null
     * @since 0.0.0
     */
    public @Nullable BiTreeNode<E> linkLeft(@Nullable BiTreeNode<E> left) {
        bidirectionalLinkLeft(this, left);
        return left;
    }

    /**
     * <p>
     * Bidirectional-links a node created by given value as left node of this node. Created and linked node will be
     * returned.
     * </p>
     * 
     * @param leftValue
     *            given value
     * @return created and linked node
     * @since 0.0.0
     */
    public BiTreeNode<E> linkLeftByValue(@Nullable E leftValue) {
        return linkLeft(createNode(leftValue));
    }

    /**
     * <p>
     * Bidirectional-links given node as right node of this node. If given node is null, the right node of this node
     * will be set null. Linked given node or null (if given node is null) will be returned.
     * </p>
     * 
     * @param right
     *            given node as right node of this node
     * @return linked given node or null
     * @since 0.0.0
     */
    public @Nullable BiTreeNode<E> linkRight(@Nullable BiTreeNode<E> right) {
        bidirectionalLinkRight(this, right);
        return right;
    }

    /**
     * <p>
     * Bidirectional-links a node created by given value as right node of this node. Created and linked node will be
     * returned.
     * </p>
     * 
     * @param rightValue
     *            given value
     * @return created and linked node
     * @since 0.0.0
     */
    public BiTreeNode<E> linkRightByValue(@Nullable E rightValue) {
        return linkRight(createNode(rightValue));
    }

    /**
     * <p>
     * Bidirectional-links given node as brother node of this node. If given node is null, the brother node of this node
     * will be set null. Linked given node or null (if given node is null) will be returned.
     * </p>
     * <p>
     * If this node doesn't have a parent node, do nothing and return null.
     * </p>
     * 
     * @param brother
     *            given node as brother node of this node
     * @return linked given node or null
     * @since 0.0.0
     */
    public @Nullable BiTreeNode<E> linkBrother(@Nullable BiTreeNode<E> brother) {
        if (isLeft()) {
            getParent().linkRight(brother);
            return brother;
        } else if (isRight()) {
            getParent().linkLeft(brother);
            return brother;
        }
        return null;
    }

    /**
     * <p>
     * Bidirectional-links a node created by given value as brother node of this node. Created and linked node will be
     * returned.
     * </p>
     * <p>
     * If this node doesn't have a parent node, do nothing and return null.
     * </p>
     * 
     * @param brotherValue
     *            given value
     * @return created and linked node or null
     * @since 0.0.0
     */
    public BiTreeNode<E> linkBrotherByValue(@Nullable E brotherValue) {
        return linkBrother(createNode(brotherValue));
    }

    /**
     * <p>
     * Returns whether this node has left node.
     * </p>
     * 
     * @return whether this node has left node
     * @since 0.0.0
     */
    public boolean hasLeft() {
        return getLeft() != null;
    }

    /**
     * <p>
     * Returns whether this node has right node.
     * </p>
     * 
     * @return whether this node has right node
     * @since 0.0.0
     */
    public boolean hasRight() {
        return getRight() != null;
    }

    /**
     * <p>
     * Bidirectional-cuts off the relation between left node and this node. If left node is null, do nothing.
     * </p>
     * 
     * @return this
     * @since 0.0.0
     */
    public BiTreeNode<E> cutOffLeft() {
        if (hasLeft()) {
            BiTreeNode<E> nullNode = null;
            getLeft().setParent(nullNode);
            setLeft(nullNode);
        }
        return this;
    }

    /**
     * <p>
     * Bidirectional-cuts off the relation between right node and this node. If right node is null, do nothing.
     * </p>
     * 
     * @return this
     * @since 0.0.0
     */
    public BiTreeNode<E> cutOffRight() {
        if (hasRight()) {
            BiTreeNode<E> nullNode = null;
            getRight().setParent(nullNode);
            setRight(nullNode);
        }
        return this;
    }

    @Override
    public boolean isLeaf() {
        return !hasLeft() && !hasRight();
    }

    /**
     * <p>
     * Returns whether this node is left node.
     * </p>
     * 
     * @return whether this node is left node
     * @since 0.0.0
     */
    public boolean isLeft() {
        return isChild() && getParent().getLeft() == this;
    }

    /**
     * <p>
     * Returns whether this node is right node.
     * </p>
     * 
     * @return whether this node is right node
     * @since 0.0.0
     */
    public boolean isRight() {
        return isChild() && getParent().getRight() == this;
    }

    /**
     * <p>
     * Returns whether this node is left leaf node.
     * </p>
     * 
     * @return whether this node is left leaf node
     * @since 0.0.0
     */
    public boolean isLeftLeaf() {
        return isLeaf() && isLeft();
    }

    /**
     * <p>
     * Returns whether this node is right leaf node.
     * </p>
     * 
     * @return whether this node is right leaf node
     * @since 0.0.0
     */
    public boolean isRightLeaf() {
        return isLeaf() && isRight();
    }

    /**
     * <p>
     * Returns whether this node is left node of given node.
     * </p>
     * 
     * @param parent
     *            given node
     * @return whether this node is left node of given node
     * @since 0.0.0
     */
    public boolean isLeftOf(@Nullable BiTreeNode<E> parent) {
        if (parent == null) {
            return false;
        }
        return this == parent.getLeft();
    }

    /**
     * <p>
     * Returns whether this node is right node of given node.
     * </p>
     * 
     * @param parent
     *            given node
     * @return whether this node is right node of given node
     * @since 0.0.0
     */
    public boolean isRightOf(@Nullable BiTreeNode<E> parent) {
        if (parent == null) {
            return false;
        }
        return this == parent.getRight();
    }

    /**
     * <p>
     * Returns whether this node has brother node.
     * </p>
     * 
     * @return whether this node has brother node
     * @since 0.0.0
     */
    public boolean hasBrother() {
        return isLeft() ? getParent().hasRight() : (isRight() ? getParent().hasLeft() : false);
    }

    /**
     * <p>
     * Returns brother node of this node if it has.
     * </p>
     * 
     * @return brother node of this node
     * @since 0.0.0
     */
    public @Nullable BiTreeNode<E> getBrother() {
        if (isLeft()) {
            return getParent().getRight();
        } else if (isRight()) {
            return getParent().getLeft();
        } else {
            return null;
        }
    }

    @Override
    public Iterator<BiTreeNode<E>> iteratorInPreorder() {
        return new PreorderIterator();
    }

    /**
     * <p>
     * Returns an iterator to iterate each node under this node which is considered as root in inorder.
     * </p>
     * 
     * @return an iterator to iterate each node under this node which is considered as root in inorder
     * @since 0.0.0
     */
    public Iterator<BiTreeNode<E>> iteratorInInorder() {
        return new InorderIterator();
    }

    @Override
    public Iterator<BiTreeNode<E>> iteratorInPostorder() {
        return new PostorderIterator();
    }

    @Override
    public Iterator<BiTreeNode<E>> iteratorInLevelorder() {
        return new LevelorderIterator();
    }

    /**
     * <p>
     * Returns an iterator to iterate each level start from this node. The node list of each level is read-only.
     * </p>
     * 
     * @return an iterator to iterate each level start from this node
     * @since 0.0.0
     */
    public Iterator<List<BiTreeNode<E>>> levelIterator() {
        return new LevelIterator();
    }

    /**
     * <p>
     * Counts depth of this node which as root.
     * </p>
     * 
     * @return depth of this node which as root
     * @since 0.0.0
     */
    public int countDepth() {
        int depth = 0;
        int currentStack = 0;
        int countStack = 1;
        int levelLastStack = 1;
        Deque<BiTreeNode<E>> queue = new LinkedList<>();
        queue.addLast(this);
        while (!queue.isEmpty()) {
            BiTreeNode<E> node = queue.pollFirst();
            currentStack++;
            if (node.hasLeft()) {
                queue.addLast(node.getLeft());
                countStack++;
            }
            if (node.hasRight()) {
                queue.addLast(node.getRight());
                countStack++;
            }
            if (currentStack == levelLastStack) {
                depth++;
                levelLastStack = countStack;
            }
        }
        return depth;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id=" + System.identityHashCode(this) + ",value=" + getValue() + ",left="
                + System.identityHashCode(getLeft()) + ",right=" + System.identityHashCode(getRight()) + "}";
    }

    class PreorderIterator implements Iterator<BiTreeNode<E>> {

        private final Deque<BiTreeNode<E>> stack = new LinkedList<>();

        PreorderIterator() {
            stack.addLast(BiTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public BiTreeNode<E> next() {
            if (!stack.isEmpty()) {
                BiTreeNode<E> node = stack.pollLast();
                if (node.hasRight()) {
                    stack.addLast(node.getRight());
                }
                if (node.hasLeft()) {
                    stack.addLast(node.getLeft());
                }
                return node;
            }
            return null;
        }
    }

    class InorderIterator implements Iterator<BiTreeNode<E>> {

        private final Deque<BiTreeNode<E>> stack = new LinkedList<>();

        InorderIterator() {
            BiTreeNode<E> node = BiTreeNode.this;
            while (node != null) {
                stack.addLast(node);
                node = node.getLeft();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public BiTreeNode<E> next() {
            if (!stack.isEmpty()) {
                BiTreeNode<E> node = stack.pollLast();
                BiTreeNode<E> right = node.getRight();
                while (right != null) {
                    stack.addLast(right);
                    right = right.getLeft();
                }
                return node;
            }
            return null;
        }
    }

    class PostorderIterator implements Iterator<BiTreeNode<E>> {

        private final Deque<BiTreeNode<E>> stack = new LinkedList<>();

        PostorderIterator() {
            stack.addLast(BiTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Nullable
        private BiTreeNode<E> pre = null;

        @Override
        public BiTreeNode<E> next() {
            while (!stack.isEmpty()) {
                BiTreeNode<E> cur = stack.peekLast();
                if (cur.isLeaf() || cur.isParentOf(pre)) {
                    pre = cur;
                    stack.pollLast();
                    return cur;
                } else {
                    if (cur.hasRight()) {
                        stack.addLast(cur.getRight());
                    }
                    if (cur.hasLeft()) {
                        stack.addLast(cur.getLeft());
                    }
                }
            }
            return null;
        }
    }

    class LevelorderIterator implements Iterator<BiTreeNode<E>> {

        private final Deque<BiTreeNode<E>> queue = new LinkedList<>();

        LevelorderIterator() {
            queue.addLast(BiTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public BiTreeNode<E> next() {
            while (!queue.isEmpty()) {
                BiTreeNode<E> node = queue.pollFirst();
                if (node.hasLeft()) {
                    queue.addLast(node.getLeft());
                }
                if (node.hasRight()) {
                    queue.addLast(node.getRight());
                }
                return node;
            }
            return null;
        }
    }

    class LevelIterator implements Iterator<List<BiTreeNode<E>>> {

        private final Deque<BiTreeNode<E>> queue = new LinkedList<>();
        private final Deque<BiTreeNode<E>> next = new LinkedList<>();

        private int depth = 0;
        private int currentStack = 0;
        private int countStack = 1;
        private int levelLastStack = 1;

        LevelIterator() {
            queue.addLast(BiTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public List<BiTreeNode<E>> next() {
            while (!queue.isEmpty()) {
                BiTreeNode<E> node = queue.pollFirst();
                next.addLast(node);
                currentStack++;
                if (node.hasLeft()) {
                    queue.addLast(node.getLeft());
                    countStack++;
                }
                if (node.hasRight()) {
                    queue.addLast(node.getRight());
                    countStack++;
                }
                if (currentStack == levelLastStack) {
                    depth++;
                    levelLastStack = countStack;
                    List<BiTreeNode<E>> result = Collections
                            .unmodifiableList(new ArrayList<>((LinkedList<BiTreeNode<E>>)next));
                    next.clear();
                    return result;
                }
            }
            return null;
        }
    }
}
