package com.cogician.quicker;

/**
 * This class represents a counter.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-03-24 15:18:50
 * @since 0.0.0
 */
public class Counter {
    /**
     * Counter value;
     */
    private long counter;

    /**
     * Constructs with 0 value.
     */
    public Counter() {
        setCounter(0);
    }

    /**
     * Constructs with special counter.
     *
     * @param counter
     *            special counter
     */
    public Counter(final long counter) {
        this.setCounter(counter);
    }

    /**
     * Constructs with special counter.
     *
     * @param counter
     *            special counter
     */
    public Counter(final int counter) {
        this.setCounter(counter);
    }

    /**
     * Returns value
     *
     * @return value
     */
    public long getCounter() {
        return counter;
    }

    /**
     * Returns value as int type, if value is more than
     * {@linkplain Integer#MAX_VALUE}, return {@linkplain Integer#MAX_VALUE}.
     *
     * @return value as int type
     */
    public int getCounterAsInt() {
        return counter > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) counter;
    }

    /**
     * Sets value.
     *
     * @param counter
     *            special value
     */
    public void setCounter(final long counter) {
        this.counter = counter;
    }

    /**
     * Sets value.
     *
     * @param counter
     *            special value
     */
    public void setCounter(final int counter) {
        this.counter = counter;
    }

    /**
     * value++
     */
    public void increment() {
        counter++;
    }

    /**
     * value += special value.
     *
     * @param value
     *            special value
     */
    public void increment(final long value) {
        counter += value;
    }

    /**
     * value--
     */
    public void decrement() {
        counter--;
    }

    /**
     * value -= special value.
     *
     * @param value
     *            special value
     */
    public void decrement(final long value) {
        counter -= value;
    }

    /**
     * Hash code of value.
     *
     * @return hash code of value
     */
    @Override
    public int hashCode() {
        return Long.hashCode(counter);
    }

    /**
     * Returns whether this counter's value is equals special one's.
     *
     * @param obj
     *            special one
     * @return whether this counter's value is equals special one's
     */
    @Override
    public boolean equals(final Object obj) {
        if (!Checker.isSameType(this, obj)) {
            return false;
        } else {
            return ((Counter) obj).counter == counter;
        }
    }

    /**
     * Value's string.
     */
    @Override
    public String toString() {
        return Long.toString(counter);
    }
}
