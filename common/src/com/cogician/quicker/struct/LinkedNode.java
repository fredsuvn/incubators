package com.cogician.quicker.struct;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Node of a bidirectional linked list.
 * </p>
 * <p>
 * This class has set and link methods to operate its previous and next nodes. Especially, set methods only set previous
 * and next nodes but don't set those corresponding next and previous nodes to current node; link method not only set
 * previous and next nodes but also set those corresponding next and previous nodes (bidirectional link).
 * </p>
 * <p>
 * This class is cloneable and serializable.
 * </p>
 *
 * @param <E>
 *            value type of node
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-18T10:17:19+08:00
 * @since 0.0.0, 2016-04-18T10:17:19+08:00
 */
public class LinkedNode<E> extends Node<E> implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    @Nullable
    private LinkedNode<E> previous;

    @Nullable
    private LinkedNode<E> next;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public LinkedNode() {
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
    public LinkedNode(@Nullable E value) {
        super(value);
    }

    /**
     * <p>
     * Constructs an instance with given value, previous node and next node.
     * </p>
     * 
     * @param value
     *            given value
     * @param prev
     *            previous node
     * @param next
     *            next node
     * @since 0.0.0
     */
    public LinkedNode(@Nullable E value, @Nullable LinkedNode<E> prev, @Nullable LinkedNode<E> next) {
        super(value);
        setPrevious(prev);
        setNext(next);
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
    public LinkedNode(@Nullable E value, @Nullable E prevValue, @Nullable E nextValue) {
        super(value);
        setPrevious(createNode(prevValue));
        setNext(createNode(nextValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Node<E>> T createNode(E value) {
        return (T)new LinkedNode<>(value);
    }

    /**
     * <p>
     * Gets previous node.
     * </p>
     * 
     * @return previous node
     * @since 0.0.0
     */
    public @Nullable LinkedNode<E> getPrevious() {
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
    public void setPrevious(@Nullable LinkedNode<E> prev) {
        this.previous = prev;
    }

    /**
     * <p>
     * Gets value of previous node. If previous node is null, return null.
     * </p>
     * 
     * @return value of previous node
     * @since 0.0.0
     */
    public @Nullable E getPreviousValue() {
        return getValue(getPrevious());
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
    public @Nullable LinkedNode<E> getNext() {
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
    public void setNext(@Nullable LinkedNode<E> next) {
        this.next = next;
    }

    /**
     * <p>
     * Gets value of next node. If next node is null, return null.
     * </p>
     * 
     * @return value of next node
     * @since 0.0.0
     */
    public @Nullable E getNextValue() {
        return getValue(getNext());
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

    private static <E> void bidirectionalLink(@Nullable LinkedNode<E> asPrev, @Nullable LinkedNode<E> asNext) {
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
    public @Nullable LinkedNode<E> linkPrevious(@Nullable LinkedNode<E> prev) {
        bidirectionalLink(prev, this);
        return prev;
    }

    /**
     * <p>
     * Bidirectional-links given nodes in previous direction. If any element of given nodes is null, the link operation
     * will be broken when first null element encountered, and previous node of the last linked node will be set null.
     * If given nodes is null or empty, the previous node of this node will be set null. Last linked node (or null) will
     * be returned.
     * </p>
     * 
     * @param preves
     *            given nodes as previous nodes of this node
     * @return last linked node (or null)
     * @since 0.0.0
     */
    public @Nullable LinkedNode<E> linkPreviouses(@SuppressWarnings("unchecked") @Nullable LinkedNode<E>... preves) {
        if (Checker.isEmpty(preves)) {
            return linkPrevious(null);
        }
        LinkedNode<E> last = this;
        for (int i = 0; i < preves.length; i++) {
            if (preves[i] == null) {
                return last.linkPrevious(null);
            } else {
                last = last.linkPrevious(preves[i]);
            }
        }
        return last;
    }

    /**
     * <p>
     * Bidirectional-links given nodes in previous direction. If any element of given nodes is null, the link operation
     * will be broken when first null element encountered, and previous node of the last linked node will be set null.
     * If given nodes is null or empty, the previous node of this node will be set null. Last linked node (or null) will
     * be returned.
     * </p>
     * 
     * @param preves
     *            given nodes as previous nodes of this node
     * @return last linked node (or null)
     * @since 0.0.0
     */
    public @Nullable LinkedNode<E> linkPreviouses(@Nullable List<LinkedNode<E>> preves) {
        if (Checker.isEmpty(preves)) {
            return linkPrevious(null);
        }
        LinkedNode<E> last = this;
        for (int i = 0; i < preves.size(); i++) {
            if (preves.get(i) == null) {
                return last.linkPrevious(null);
            } else {
                last = last.linkPrevious(preves.get(i));
            }
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
    public LinkedNode<E> linkPreviousByValue(@Nullable E prevValue) {
        return linkPrevious(createNode(prevValue));
    }

    /**
     * <p>
     * Bidirectional-links nodes created by given values in previous direction. If given values is null or empty, the
     * previous node of this node will be set null. Last linked node (or null) will be returned.
     * </p>
     * 
     * @param prevValues
     *            given values
     * @return last linked node (or null)
     * @since 0.0.0
     */
    public LinkedNode<E> linkPreviousesByValues(@SuppressWarnings("unchecked") @Nullable E... prevValues) {
        if (Checker.isEmpty(prevValues)) {
            return linkPrevious(null);
        }
        LinkedNode<E> last = this;
        for (int i = 0; i < prevValues.length; i++) {
            last = last.linkPrevious(createNode(prevValues[i]));
        }
        return last;
    }

    /**
     * <p>
     * Bidirectional-links nodes created by given values in previous direction. If given values is null or empty, the
     * previous node of this node will be set null. Last linked node (or null) will be returned.
     * </p>
     * 
     * @param prevValues
     *            given values
     * @return last linked node (or null)
     * @since 0.0.0
     */
    public LinkedNode<E> linkPreviousesByValues(@Nullable List<E> prevValues) {
        if (Checker.isEmpty(prevValues)) {
            return linkPrevious(null);
        }
        LinkedNode<E> last = this;
        for (int i = 0; i < prevValues.size(); i++) {
            last = last.linkPrevious(createNode(prevValues.get(i)));
        }
        return last;
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
    public @Nullable LinkedNode<E> linkNext(@Nullable LinkedNode<E> next) {
        bidirectionalLink(this, next);
        return next;
    }

    /**
     * <p>
     * Bidirectional-links given nodes in next direction. If any element of given nodes is null, the link operation will
     * be broken when first null element encountered, and next node of the last linked node will be set null. If given
     * nodes is null or empty, the next node of this node will be set null. Last linked node (or null) will be returned.
     * </p>
     * 
     * @param nexts
     *            given nodes as next nodes of this node
     * @return last linked node (or null)
     * @since 0.0.0
     */
    public @Nullable LinkedNode<E> linkNexts(@SuppressWarnings("unchecked") @Nullable LinkedNode<E>... nexts) {
        if (Checker.isEmpty(nexts)) {
            return linkNext(null);
        }
        LinkedNode<E> last = this;
        for (int i = 0; i < nexts.length; i++) {
            if (nexts[i] == null) {
                return last.linkNext(null);
            } else {
                last = last.linkNext(nexts[i]);
            }
        }
        return last;
    }

    /**
     * <p>
     * Bidirectional-links given nodes in next direction. If any element of given nodes is null, the link operation will
     * be broken when first null element encountered, and next node of the last linked node will be set null. If given
     * nodes is null or empty, the next node of this node will be set null. Last linked node (or null) will be returned.
     * </p>
     * 
     * @param nexts
     *            given nodes as next nodes of this node
     * @return last linked node (or null)
     * @since 0.0.0
     */
    public @Nullable LinkedNode<E> linkNexts(@Nullable List<LinkedNode<E>> nexts) {
        if (Checker.isEmpty(nexts)) {
            return linkNext(null);
        }
        LinkedNode<E> last = this;
        for (int i = 0; i < nexts.size(); i++) {
            if (nexts.get(i) == null) {
                return last.linkNext(null);
            } else {
                last = last.linkNext(nexts.get(i));
            }
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
    public LinkedNode<E> linkNextByValue(@Nullable E nextValue) {
        return linkNext(createNode(nextValue));
    }

    /**
     * <p>
     * Bidirectional-links nodes created by given values in next direction. If given values is null or empty, the next
     * node of this node will be set null. Last linked node (or null) will be returned.
     * </p>
     * 
     * @param nextValues
     *            given values
     * @return last linked node (or null)
     * @since 0.0.0
     */
    public LinkedNode<E> linkNextsByValues(@SuppressWarnings("unchecked") @Nullable E... nextValues) {
        if (Checker.isEmpty(nextValues)) {
            return linkNext(null);
        }
        LinkedNode<E> last = this;
        for (int i = 0; i < nextValues.length; i++) {
            last = last.linkNext(createNode(nextValues[i]));
        }
        return last;
    }

    /**
     * <p>
     * Bidirectional-links nodes created by given values in next direction. If given values is null or empty, the next
     * node of this node will be set null. Last linked node (or null) will be returned.
     * </p>
     * 
     * @param nextValues
     *            given values
     * @return last linked node (or null)
     * @since 0.0.0
     */
    public LinkedNode<E> linkNextsByValues(@Nullable List<E> nextValues) {
        if (Checker.isEmpty(nextValues)) {
            return linkNext(null);
        }
        LinkedNode<E> last = this;
        for (int i = 0; i < nextValues.size(); i++) {
            last = last.linkNext(createNode(nextValues.get(i)));
        }
        return last;
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
        LinkedNode<E> nullNode = null;
        setPrevious(nullNode);
        setNext(nullNode);
    }

    /**
     * <p>
     * Cuts off the link between previous node and this node.
     * </p>
     * 
     * @since 0.0.0
     */
    public void cutOffPrevious() {
        getPrevious().linkNext(null);
        linkPrevious(null);
    }

    /**
     * <p>
     * Cuts off the link between this node and next node.
     * </p>
     * 
     * @since 0.0.0
     */
    public void cutOffNext() {
        getNext().linkPrevious(null);
        linkNext(null);
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
    public LinkedNode<E> getHead() {
        LinkedNode<E> cur = this;
        LinkedNode<E> prev = cur.getPrevious();
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
    public LinkedNode<E> getTail() {
        LinkedNode<E> cur = this;
        LinkedNode<E> next = cur.getNext();
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
    public boolean isPreviousOf(@Nullable LinkedNode<E> prev) {
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
    public boolean isNextOf(@Nullable LinkedNode<E> next) {
        if (next == null) {
            return false;
        }
        return this == next.getNext();
    }

    /**
     * <p>
     * Returns an iterator to iterate previous nodes from this node in previous order. If the list is circular in
     * previous direction, returned iterator will end before it meets this node again.
     * </p>
     * 
     * @return an iterator to iterate previous nodes from this node in previous order
     * @since 0.0.0
     */
    public Iterator<LinkedNode<E>> iteratorInPrevious() {
        return new Iterator<LinkedNode<E>>() {
            private LinkedNode<E> next = LinkedNode.this;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public LinkedNode<E> next() {
                LinkedNode<E> ret = next;
                if (next == null) {
                    throw new NoSuchElementException();
                }
                LinkedNode<E> n = next.getPrevious();
                if (n == null || n == LinkedNode.this) {
                    next = null;
                } else {
                    next = n;
                }
                return ret;
            }
        };
    }

    /**
     * <p>
     * Returns an iterator to iterate next nodes from this node in next order. If the list is circular in next
     * direction, returned iterator will end before it meets this node again.
     * </p>
     * 
     * @return an iterator to iterate next nodes from this node in next order
     * @since 0.0.0
     */
    public Iterator<LinkedNode<E>> iteratorInNext() {
        return new Iterator<LinkedNode<E>>() {
            private LinkedNode<E> next = LinkedNode.this;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public LinkedNode<E> next() {
                LinkedNode<E> ret = next;
                if (next == null) {
                    throw new NoSuchElementException();
                }
                LinkedNode<E> n = next.getNext();
                if (n == null || n == LinkedNode.this) {
                    next = null;
                } else {
                    next = n;
                }
                return ret;
            }
        };
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id=" + System.identityHashCode(this) + ",value=" + getValue() + ",previous="
                + System.identityHashCode(getPrevious()) + ",next=" + System.identityHashCode(getNext()) + "}";
    }
}
