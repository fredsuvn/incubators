package com.cogician.quicker.struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import com.cogician.quicker.Quicker;
import com.cogician.quicker.Quicker.Flow;

/**
 * <p>
 * Represents a binary tree node.
 * </p>
 * 
 * @param <E>
 *            type of value of node
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-20T09:36:22+08:00
 * @since 0.0.0, 2016-04-20T09:36:22+08:00
 */
public class QuickBiTreeNode<E> extends AbstractNode<E> {

    private QuickBiTreeNode<E> parent;

    private QuickBiTreeNode<E> left;

    private QuickBiTreeNode<E> right;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public QuickBiTreeNode() {
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
    public QuickBiTreeNode(@Nullable E value) {
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
    public QuickBiTreeNode(@Nullable E value, @Nullable QuickBiTreeNode<E> left, @Nullable QuickBiTreeNode<E> right) {
        super(value);
        linkLeft(left);
        linkRight(right);
    }

    @Override
    protected QuickBiTreeNode<E> createNode(E value) {
        return new QuickBiTreeNode<E>(value);
    }

    /**
     * <p>
     * Gets parent node.
     * </p>
     * 
     * @return parent node
     * @since 0.0.0
     */
    public @Nullable QuickBiTreeNode<E> getParent() {
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
    public void setParent(@Nullable QuickBiTreeNode<E> parent) {
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
     * Returns whether this node has at least one child node.
     * </p>
     * 
     * @return whether this node has at least one child node
     * @since 0.0.0
     */
    public boolean hasChild() {
        return hasLeft() || hasRight();
    }

    /**
     * <p>
     * Gets left node.
     * </p>
     * 
     * @return left node
     * @since 0.0.0
     */
    public @Nullable QuickBiTreeNode<E> getLeft() {
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
    public void setLeft(@Nullable QuickBiTreeNode<E> left) {
        this.left = left;
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
    public @Nullable QuickBiTreeNode<E> getRight() {
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
    public void setRight(@Nullable QuickBiTreeNode<E> right) {
        this.right = right;
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

    private void bidirectionalLinkLeft(@Nullable QuickBiTreeNode<E> asParent, @Nullable QuickBiTreeNode<E> asLeft) {
        if (asParent != null) {
            asParent.setLeft(asLeft);
        }
        if (asLeft != null) {
            asLeft.setParent(asParent);
        }
    }

    private void bidirectionalLinkRight(@Nullable QuickBiTreeNode<E> asParent, @Nullable QuickBiTreeNode<E> asRight) {
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
    public @Nullable QuickBiTreeNode<E> linkLeft(@Nullable QuickBiTreeNode<E> left) {
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
    public QuickBiTreeNode<E> linkLeftByValue(@Nullable E leftValue) {
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
    public @Nullable QuickBiTreeNode<E> linkRight(@Nullable QuickBiTreeNode<E> right) {
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
    public QuickBiTreeNode<E> linkRightByValue(@Nullable E rightValue) {
        return linkRight(createNode(rightValue));
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
        QuickBiTreeNode<E> l = getLeft();
        QuickBiTreeNode<E> r = getRight();
        if (l != null) {
            l.setParent(this);
        }
        if (r != null) {
            r.setParent(this);
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
    public QuickBiTreeNode<E> getRoot() {
        QuickBiTreeNode<E> node = this;
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
    public boolean isParentOf(@Nullable QuickBiTreeNode<E> child) {
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
    public boolean isChildOf(@Nullable QuickBiTreeNode<E> parent) {
        if (parent == null) {
            return false;
        }
        return parent.getLeft() == this || parent.getRight() == this;
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
     * Returns whether this node is left node.
     * </p>
     * 
     * @return whether this node is left node
     * @since 0.0.0
     */
    public boolean isLeft() {
        return hasParent() && getParent().getLeft() == this;
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
        return hasParent() && getParent().getRight() == this;
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
    public boolean isLeftOf(@Nullable QuickBiTreeNode<E> parent) {
        if (parent == null) {
            return false;
        }
        return parent.getLeft() == this;
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
    public boolean isRightOf(@Nullable QuickBiTreeNode<E> parent) {
        if (parent == null) {
            return false;
        }
        return parent.getRight() == this;
    }

    /**
     * <p>
     * Returns a flow to traverse children of this node in preorder.
     * </p>
     * 
     * @return a flow to traverse children of this node in preorder
     * @since 0.0.0
     */
    public Flow<QuickBiTreeNode<E>> flowPreorder() {
        return Quicker.flow(new PreorderIterator());
    }

    /**
     * <p>
     * Returns a flow to traverse children of this node in inorder.
     * </p>
     * 
     * @return a flow to traverse children of this node in inorder
     * @since 0.0.0
     */
    public Flow<QuickBiTreeNode<E>> flowInorder() {
        return Quicker.flow(new InorderIterator());
    }

    /**
     * <p>
     * Returns a flow to traverse children of this node in postorder.
     * </p>
     * 
     * @return a flow to traverse children of this node in postorder
     * @since 0.0.0
     */
    public Flow<QuickBiTreeNode<E>> flowPostorder() {
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
    public Flow<QuickBiTreeNode<E>> flowLevelorder() {
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
    public Flow<List<QuickBiTreeNode<E>>> flowLevel() {
        return Quicker.flow(new LevelIterator());
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
        Deque<QuickBiTreeNode<E>> queue = new LinkedList<>();
        queue.addLast(this);
        while (!queue.isEmpty()) {
            QuickBiTreeNode<E> node = queue.pollFirst();
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

    /**
     * <p>
     * Converts this node into a tree node.
     * </p>
     * 
     * @return converted tree node
     * @since 0.0.0
     */
    public QuickTreeNode<E> toTreeNode() {
        QuickTreeNode<E> result = new QuickTreeNode<>(getValue());
        if (hasLeft()) {
            result.children().add(new QuickTreeNode<E>(getLeft().getValue()));
        }
        if (hasRight()) {
            result.children().add(new QuickTreeNode<E>(getRight().getValue()));
        }
        result.children().add(new QuickTreeNode<E>());
        return result;
    }

    /**
     * <p>
     * Returns a string represents this node.
     * </p>
     * 
     * @return a string represents this node
     * @since 0.0.0
     */
    @Override
    public String toString() {
        return "{value:" + getValue() + ",left:" + (hasLeft() ? getLeft().getValue() : null) + ",right:"
                + (hasRight() ? getRight().getValue() : null) + "}";
    }

    class PreorderIterator implements Iterator<QuickBiTreeNode<E>> {

        private final Deque<QuickBiTreeNode<E>> stack = new LinkedList<>();

        PreorderIterator() {
            stack.addLast(QuickBiTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public QuickBiTreeNode<E> next() {
            if (!stack.isEmpty()) {
                QuickBiTreeNode<E> node = stack.pollLast();
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

    class InorderIterator implements Iterator<QuickBiTreeNode<E>> {

        private final Deque<QuickBiTreeNode<E>> stack = new LinkedList<>();

        InorderIterator() {
            QuickBiTreeNode<E> node = QuickBiTreeNode.this;
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
        public QuickBiTreeNode<E> next() {
            if (!stack.isEmpty()) {
                QuickBiTreeNode<E> node = stack.pollLast();
                QuickBiTreeNode<E> right = node.getRight();
                while (right != null) {
                    stack.addLast(right);
                    right = right.getLeft();
                }
                return node;
            }
            return null;
        }
    }

    class PostorderIterator implements Iterator<QuickBiTreeNode<E>> {

        private final Deque<QuickBiTreeNode<E>> stack = new LinkedList<>();

        PostorderIterator() {
            stack.addLast(QuickBiTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Nullable
        private QuickBiTreeNode<E> pre = null;

        @Override
        public QuickBiTreeNode<E> next() {
            while (!stack.isEmpty()) {
                QuickBiTreeNode<E> cur = stack.peekLast();
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

    class LevelorderIterator implements Iterator<QuickBiTreeNode<E>> {

        private final Deque<QuickBiTreeNode<E>> queue = new LinkedList<>();

        LevelorderIterator() {
            queue.addLast(QuickBiTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public QuickBiTreeNode<E> next() {
            while (!queue.isEmpty()) {
                QuickBiTreeNode<E> node = queue.pollFirst();
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

    class LevelIterator implements Iterator<List<QuickBiTreeNode<E>>> {

        private final Deque<QuickBiTreeNode<E>> queue = new LinkedList<>();
        private final Deque<QuickBiTreeNode<E>> next = new LinkedList<>();

        private int depth = 0;
        private int currentStack = 0;
        private int countStack = 1;
        private int levelLastStack = 1;

        LevelIterator() {
            queue.addLast(QuickBiTreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public List<QuickBiTreeNode<E>> next() {
            while (!queue.isEmpty()) {
                QuickBiTreeNode<E> node = queue.pollFirst();
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
                    List<QuickBiTreeNode<E>> result = Collections
                            .unmodifiableList(new ArrayList<>((LinkedList<QuickBiTreeNode<E>>)next));
                    next.clear();
                    return result;
                }
            }
            return null;
        }
    }
}
