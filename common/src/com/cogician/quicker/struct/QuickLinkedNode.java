package com.cogician.quicker.struct;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.cogician.quicker.Quicker;
import com.cogician.quicker.Quicker.Flow;
import com.cogician.quicker.util.QuickCollections;

/**
 * <p>
 * Represents a bidirectional linked node.
 * </p>
 *
 * @param <E>
 *            value type of node
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-18T10:17:19+08:00
 * @since 0.0.0, 2016-04-18T10:17:19+08:00
 */
public class QuickLinkedNode<E> extends AbstractNode<E> {

    private QuickLinkedNode<E> previous;

    private QuickLinkedNode<E> next;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public QuickLinkedNode() {
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
    public QuickLinkedNode(@Nullable E value) {
        super(value);
    }

    /**
     * <p>
     * Constructs an instance with given value, value of previous node and value of next node.
     * </p>
     * 
     * @param value
     *            given value
     * @param prevValue
     *            value of previous node
     * @param nextValue
     *            value of next node
     * @since 0.0.0
     */
    public QuickLinkedNode(@Nullable E value, @Nullable E prevValue, @Nullable E nextValue) {
        super(value);
        setPrevious(createNode(prevValue));
        setNext(createNode(nextValue));
    }

    @Override
    protected QuickLinkedNode<E> createNode(E value) {
        return new QuickLinkedNode<>(value);
    }

    /**
     * <p>
     * Gets previous node.
     * </p>
     * 
     * @return previous node
     * @since 0.0.0
     */
    public @Nullable QuickLinkedNode<E> getPrevious() {
        return previous;
    }

    /**
     * <p>
     * Sets previous node.
     * </p>
     * 
     * @param prev
     *            previous node
     * @since 0.0.0
     */
    public void setPrevious(@Nullable QuickLinkedNode<E> prev) {
        this.previous = prev;
    }

    /**
     * <p>
     * Sets previous node created by given value.
     * </p>
     * 
     * @param prevValue
     *            given value
     * @since 0.0.0
     */
    public void setPreviousByValue(@Nullable E prevValue) {
        setPrevious(createNode(prevValue));
    }

    /**
     * <p>
     * Gets next node.
     * </p>
     * 
     * @return next node
     * @since 0.0.0
     */
    public @Nullable QuickLinkedNode<E> getNext() {
        return next;
    }

    /**
     * <p>
     * Sets next node.
     * </p>
     * 
     * @param next
     *            next node
     * @since 0.0.0
     */
    public void setNext(@Nullable QuickLinkedNode<E> next) {
        this.next = next;
    }

    /**
     * <p>
     * Sets next node created by given value.
     * </p>
     * 
     * @param nextValue
     *            given value
     * @since 0.0.0
     */
    public void setNextByValue(@Nullable E nextValue) {
        setNext(createNode(nextValue));
    }

    private static <E> void bidirectionalLink(@Nullable QuickLinkedNode<E> asPrev,
            @Nullable QuickLinkedNode<E> asNext) {
        if (asPrev != null) {
            asPrev.setNext(asNext);
        }
        if (asNext != null) {
            asNext.setPrevious(asPrev);
        }
    }

    /**
     * <p>
     * Bidirectional-links given node as previous node of this node. If given node is null, the previous node of this
     * node will be set null. Given node (or null) will be returned.
     * </p>
     * 
     * @param prev
     *            given node as previous node of this node
     * @return given node (or null)
     * @since 0.0.0
     */
    public @Nullable QuickLinkedNode<E> linkPrevious(@Nullable QuickLinkedNode<E> prev) {
        bidirectionalLink(prev, this);
        return prev;
    }

    /**
     * <p>
     * Bidirectional-links given node list as previous nodes in previous direction. If a null node in the given list,
     * this link operation will break. Last previous node (which is head) will be returned.
     * </p>
     * 
     * @param prevs
     *            given node list
     * @return last previous node
     * @since 0.0.0
     */
    public @Nullable QuickLinkedNode<E> linkPrevious(List<QuickLinkedNode<E>> prevs) {
        QuickLinkedNode<E> last = this;
        for (int i = 0; i < prevs.size(); i++) {
            QuickLinkedNode<E> cur = prevs.get(i);
            if (cur == null) {
                break;
            }
            bidirectionalLink(cur, last);
            last = cur;
        }
        return last;
    }

    /**
     * <p>
     * Bidirectional-links a node created by given value as previous node of this node. Created and linked node will be
     * returned.
     * </p>
     * 
     * @param prevValue
     *            given value
     * @return created and linked node
     * @since 0.0.0
     */
    public QuickLinkedNode<E> linkPreviousByValue(@Nullable E prevValue) {
        return linkPrevious(createNode(prevValue));
    }

    /**
     * <p>
     * Bidirectional-links nodes created by given value list as previous nodes in previous direction. Last previous node
     * (which is head) will be returned.
     * </p>
     * 
     * @param prevValues
     *            given value list
     * @return last previous node
     * @since 0.0.0
     */
    public QuickLinkedNode<E> linkPreviousByValues(List<E> prevValues) {
        return linkPrevious(
                QuickCollections.functionalList(i -> new QuickLinkedNode<>(prevValues.get(i)), prevValues.size()));
    }

    /**
     * <p>
     * Bidirectional-links given node as next node of this node. If given node is null, the next node of this node will
     * be set null. Given node (or null) will be returned.
     * </p>
     * 
     * @param next
     *            given node as next node of this node
     * @return given node (or null)
     * @since 0.0.0
     */
    public @Nullable QuickLinkedNode<E> linkNext(@Nullable QuickLinkedNode<E> next) {
        bidirectionalLink(this, next);
        return next;
    }

    /**
     * <p>
     * Bidirectional-links given node list as next nodes in next direction. If a null node in the given list, this link
     * operation will break. Last next node (which is tail) will be returned.
     * </p>
     * 
     * @param nexts
     *            given node list
     * @return last next node (or null)
     * @since 0.0.0
     */
    public @Nullable QuickLinkedNode<E> linkNext(List<QuickLinkedNode<E>> nexts) {
        QuickLinkedNode<E> last = this;
        for (int i = 0; i < nexts.size(); i++) {
            QuickLinkedNode<E> cur = nexts.get(i);
            if (cur == null) {
                break;
            }
            bidirectionalLink(last, cur);
            last = cur;
        }
        return last;
    }

    /**
     * <p>
     * Bidirectional-links a node created by given value as next node of this node. Created and linked node will be
     * returned.
     * </p>
     * 
     * @param nextValue
     *            given value
     * @return created and linked node
     * @since 0.0.0
     */
    public QuickLinkedNode<E> linkNextByValue(@Nullable E nextValue) {
        return linkNext(createNode(nextValue));
    }

    /**
     * <p>
     * Bidirectional-links nodes created by given value list as next nodes in next direction. Last next node (which is
     * tail) will be returned.
     * </p>
     * 
     * @param nextValues
     *            given value list
     * @return last next node
     * @since 0.0.0
     */
    public QuickLinkedNode<E> linkNextByValues(List<E> nextValues) {
        return linkNext(
                QuickCollections.functionalList(i -> new QuickLinkedNode<>(nextValues.get(i)), nextValues.size()));
    }

    /**
     * <p>
     * Deletes this node from the linked list where this node is, and reference of previous and next node in this node
     * will also be cleared. Previous and next node of this node will be bidirectional-linked if they are not null.
     * </p>
     * 
     * @since 0.0.0
     */
    public void delete() {
        bidirectionalLink(getPrevious(), getNext());
        QuickLinkedNode<E> nullNode = null;
        setPrevious(nullNode);
        setNext(nullNode);
    }

    /**
     * <p>
     * Returns whether this node has previous node.
     * </p>
     * 
     * @return whether this node has previous node
     * @since 0.0.0
     */
    public boolean hasPrevious() {
        return getPrevious() != null;
    }

    /**
     * <p>
     * Returns whether this node has next node.
     * </p>
     * 
     * @return whether this node has next node
     * @since 0.0.0
     */
    public boolean hasNext() {
        return getNext() != null;
    }

    /**
     * <p>
     * Returns whether this node is head (no previous node).
     * </p>
     * 
     * @return whether this node is head
     * @since 0.0.0
     */
    public boolean isHead() {
        return !hasPrevious();
    }

    /**
     * <p>
     * Returns whether this node is tail (no next node).
     * </p>
     * 
     * @return whether this node is tail
     * @since 0.0.0
     */
    public boolean isTail() {
        return !hasNext();
    }

    /**
     * <p>
     * Returns whether this node in a circular list in previous nodes direction.
     * </p>
     * 
     * @return whether this node in a circular list in previous nodes direction
     * @since 0.0.0
     */
    public boolean isPreviousCircular() {
        if (getPrevious() == null) {
            return false;
        }
        return getHead() == this;
    }

    /**
     * <p>
     * Returns whether this node in a circular list in next nodes direction.
     * </p>
     * 
     * @return whether this node in a circular list in next nodes direction
     * @since 0.0.0
     */
    public boolean isNextCircular() {
        if (getNext() == null) {
            return false;
        }
        return getTail() == this;
    }

    /**
     * <p>
     * Returns head node of nodes list which this node in. If the list is circular in previous nodes direction, return
     * this.
     * </p>
     * 
     * @return head node
     * @since 0.0.0
     */
    public QuickLinkedNode<E> getHead() {
        QuickLinkedNode<E> cur = this;
        QuickLinkedNode<E> prev = cur.getPrevious();
        while (prev != null && prev != this) {
            cur = prev;
            prev = prev.getPrevious();
        }
        if (prev == this) {
            return prev;
        }
        return cur;
    }

    /**
     * <p>
     * Returns tail node of nodes list which this node in. If the list is circular in next nodes direction, return this.
     * </p>
     * 
     * @return tail node
     * @since 0.0.0
     */
    public QuickLinkedNode<E> getTail() {
        QuickLinkedNode<E> cur = this;
        QuickLinkedNode<E> next = cur.getNext();
        while (next != null && next != this) {
            cur = next;
            next = next.getNext();
        }
        if (next == this) {
            return next;
        }
        return cur;
    }

    /**
     * <p>
     * Returns whether this node is previous node of given node.
     * </p>
     * 
     * @param prev
     *            given node
     * @return whether this node is previous node of given node
     * @since 0.0.0
     */
    public boolean isPreviousOf(@Nullable QuickLinkedNode<E> prev) {
        if (prev == null) {
            return false;
        }
        return this == prev.getPrevious();
    }

    /**
     * <p>
     * Returns whether this node is next node of given node.
     * </p>
     * 
     * @param next
     *            given node
     * @return whether this node is next node of given node
     * @since 0.0.0
     */
    public boolean isNextOf(@Nullable QuickLinkedNode<E> next) {
        if (next == null) {
            return false;
        }
        return this == next.getNext();
    }

    /**
     * <p>
     * Returns a flow to traverse nodes in specified direction.
     * </p>
     * 
     * @param isNextDirection
     *            true in next direction, false in previous
     * @param isCircular
     *            whether support circularly output
     * @return a flow to traverse nodes in specified direction
     * @since 0.0.0
     */
    public Flow<QuickLinkedNode<E>> flow(boolean isNextDirection, boolean isCircular) {
        return Quicker.flow(new NodesIterator(isNextDirection, isCircular));
    }

    /**
     * <p>
     * Returns a flow to traverse nodes in previous direction. The flow is not circular, that means it will end before
     * it meets this node (as head) again.
     * </p>
     * 
     * @return a flow to traverse nodes in previous direction
     * @since 0.0.0
     */
    public Flow<QuickLinkedNode<E>> flowPrevious() {
        return flow(false, false);
    }

    /**
     * <p>
     * Returns a flow to traverse nodes in next direction. The flow is not circular, that means it will end before it
     * meets this node (as head) again.
     * </p>
     * 
     * @return a flow to traverse nodes in next direction
     * @since 0.0.0
     */
    public Flow<QuickLinkedNode<E>> flowNext() {
        return flow(true, false);
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
        return "{value:" + getValue() + ",previous:" + (hasPrevious() ? getPrevious().getValue() : null) + ",next:"
                + (hasNext() ? getNext().getValue() : null) + "}";
    }

    private class NodesIterator implements Iterator<QuickLinkedNode<E>> {
        private QuickLinkedNode<E> next = QuickLinkedNode.this;

        private final boolean isNextDirection;
        private final boolean isCircular;

        NodesIterator(boolean isNextDirection, boolean isCircular) {
            this.isNextDirection = isNextDirection;
            this.isCircular = isCircular;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public QuickLinkedNode<E> next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            QuickLinkedNode<E> ret = next;
            QuickLinkedNode<E> n = isNextDirection ? next.getNext() : next.getPrevious();
            if (n == QuickLinkedNode.this) {
                next = isCircular ? n : null;
            } else {
                next = n;
            }
            return ret;
        }

    }
}
