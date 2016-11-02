package com.cogician.quicker.util;

import java.util.Random;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * Static quick utility class provides static methods for random.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-11-09 09:25:14
 * @since 0.0.0
 */
public class RandomQuicker {

    /**
     * <p>
     * Random generator, used in this utility.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final ThreadLocal<Random> generator = ThreadLocal.withInitial(RandomQuicker::random);

    private static final Random getGenerator() {
        return generator.get();
    }

    private static int seedCount = (int)System.nanoTime();

    /**
     * <p>
     * Returns a random object by a simple RSA algorithm.
     * </p>
     * 
     * @return a random object
     * @since 0.0.0
     */
    public static Random random() {
        Random ret = new Random();
        long nano = System.nanoTime();
        int hash = System.identityHashCode(ret);
        int weakSeed = ((hash & 0x000000ff) << 24) | (int)((nano & 0x00000000000000ffL) << 16)
                | ((hash & 0xff000000) >> 16) | (int)((nano & 0xff00000000000000L) >>> 56);
        int[] rsa = MathQuicker.simpleRSAGroup(weakSeed);
        long n = rsa[2] * rsa[3];
        long seed = (long)(Math.pow(seedCount++, rsa[0]) % n);
        ret.setSeed(seed);
        return ret;
    }

    /**
     * <p>
     * Returns a double between 0.0 inclusive and 1.0 inclusive.
     * </p>
     * 
     * @return a double between 0.0 inclusive and 1.0 inclusive
     * @since 0.0.0
     */
    public static double next() {
        double d = getGenerator().nextDouble();
        if (d == 0.0) {
            return 1.0;
        }
        return getGenerator().nextDouble();
    }

    /**
     * <p>
     * Returns next random bytes of given length in bytes.
     * </p>
     * 
     * @param length
     *            given length in bytes, positive
     * @return next random bytes of given length in bytes
     * @throws IllegalArgumentException
     *             if {@code length} is negative
     * @since 0.0.0
     */
    public static byte[] nextBytes(int length) {
        Checker.checkLength(length);
        byte[] array = new byte[length];
        getGenerator().nextBytes(array);
        return array;
    }

    /**
     * <p>
     * Returns next random boolean.
     * </p>
     * 
     * @return next random boolean
     * @since 0.0.0
     */
    public static boolean nextBoolean() {
        return getGenerator().nextBoolean();
    }

    private static byte nextByteFromInt(int i) {
        return (byte)(i >> getGenerator().nextInt(25));
    }

    /**
     * <p>
     * Returns next random byte.
     * </p>
     * 
     * @return next random byte
     * @since 0.0.0
     */
    public static byte nextByte() {
        return nextByteFromInt(getGenerator().nextInt());
    }

    /**
     * <p>
     * Returns next random short.
     * </p>
     * 
     * @return next random short
     * @since 0.0.0
     */
    public static short nextShort() {
        int i1 = nextByte() & 0xFF;
        int i2 = nextByte() & 0xFF;
        return (short)((i1 << 8) | i2);
    }

    /**
     * <p>
     * Returns next random char.
     * </p>
     * 
     * @return next random char
     * @since 0.0.0
     */
    public static char nextChar() {
        return (char)nextShort();
    }

    /**
     * <p>
     * Returns next random int.
     * </p>
     * 
     * @return next random int
     * @since 0.0.0
     */
    public static int nextInt() {
        return getGenerator().nextInt();
    }

    /**
     * <p>
     * Returns next random long.
     * </p>
     * 
     * @return next random long
     * @since 0.0.0
     */
    public static long nextLong() {
        return getGenerator().nextLong();
    }

    /**
     * <p>
     * Returns next random float, including NaN and infinite.
     * </p>
     * 
     * @return next random float
     * @since 0.0.0
     */
    public static float nextFloat() {
        return Float.intBitsToFloat(nextInt());
    }

    /**
     * <p>
     * Returns next random double, including NaN and infinite.
     * </p>
     * 
     * @return next random double
     * @since 0.0.0
     */
    public static double nextDouble() {
        return Double.longBitsToDouble(nextLong());
    }

    /**
     * <p>
     * Returns next random byte in specified bounds.
     * </p>
     * 
     * @param from
     *            start bound inclusive, {@code from} <= {@code to}
     * @param to
     *            end bound inclusive, {@code from} <= {@code to}
     * @return random byte in specified bounds
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static byte nextByte(byte from, byte to) throws IllegalArgumentException {
        return (byte)nextInt(from, to);
    }

    /**
     * <p>
     * Returns next random short in specified bounds.
     * </p>
     * 
     * @param from
     *            start bound inclusive, {@code from} <= {@code to}
     * @param to
     *            end bound inclusive, {@code from} <= {@code to}
     * @return random short in specified bounds
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static short nextShort(short from, short to) throws IllegalArgumentException {
        return (short)nextInt(from, to);
    }

    /**
     * <p>
     * Returns next random char in specified bounds.
     * </p>
     * 
     * @param from
     *            start bound inclusive, {@code from} <= {@code to}
     * @param to
     *            end bound inclusive, {@code from} <= {@code to}
     * @return random char in specified bounds
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static char nextChar(char from, char to) throws IllegalArgumentException {
        return (char)nextInt(from, to);
    }

    /**
     * <p>
     * Returns next random int in specified bounds.
     * </p>
     * 
     * @param from
     *            start bound inclusive, {@code from} <= {@code to}
     * @param to
     *            end bound inclusive, {@code from} <= {@code to}
     * @return random int in specified bounds
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static int nextInt(int from, int to) throws IllegalArgumentException {
        return (int)nextLong(from, to);
    }

    /**
     * <p>
     * Returns next random long in specified bounds.
     * </p>
     * 
     * @param from
     *            start bound inclusive, {@code from} <= {@code to}
     * @param to
     *            end bound inclusive, {@code from} <= {@code to}
     * @return random long in specified bounds
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static long nextLong(long from, long to) throws IllegalArgumentException {
        return Quicker.inBounds((long)Math.floor(nextDouble(from, (double)to + 1)), from, to);
    }

    /**
     * <p>
     * Returns next random float in specified bounds.
     * </p>
     * 
     * @param from
     *            start bound inclusive, {@code from} <= {@code to}
     * @param to
     *            end bound inclusive, {@code from} <= {@code to}
     * @return random float in specified bounds
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static float nextFloat(float from, float to) throws IllegalArgumentException {
        return (float)nextDouble(from, to);
    }

    /**
     * <p>
     * Returns next random double in specified bounds.
     * </p>
     * 
     * @param from
     *            start bound inclusive, {@code from} <= {@code to}
     * @param to
     *            end bound inclusive, {@code from} <= {@code to}
     * @return random double in specified bounds
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static double nextDouble(double from, double to) throws IllegalArgumentException {
        if (from == to) {
            return from;
        }
        Checker.checkBoundsOrder(from, to);
        double r = next();
        if (from >= 0 || to <= 0) {
            return r * (to - from) + from;
        } else {
            double multiple = to / (-from);
            if (Double.isInfinite(multiple)) {
                return nextDouble(0, to);
            } else {
                double pos = next() * (1 + multiple);
                if (pos < 1.0) {
                    return nextDouble(from, 0);
                } else {
                    return nextDouble(0, to);
                }
            }
        }
    }

    /**
     * <p>
     * Returns a random string of which length is specified.
     * </p>
     * 
     * @param length
     *            specified length, 0 or positive
     * @return a random string, not null
     * @since 0.0.0
     */
    public static String nextString(int length) {
        Checker.checkLength(length);
        char[] cs = new char[length];
        for (int i = 0; i < length; i++) {
            cs[i] = nextChar();
        }
        return new String(cs);
    }

    /**
     * <p>
     * Returns a random string of which length is specified, and of which content consist of random characters from
     * given start char inclusive to given end char inclusive.
     * </p>
     * 
     * @param length
     *            specified length, 0 or positive
     * @param from
     *            given start char inclusive, {@code from} <= {@code to}
     * @param to
     *            given end char inclusive, {@code from} <= {@code to}
     * @return a random string, not null
     * @throws IllegalArgumentException
     *             if {@code length} is negative and/or {@code from} > {@code to}
     * @since 0.0.0
     */
    public static String nextString(int length, char from, char to) throws IllegalArgumentException {
        Checker.checkLength(length);
        Checker.checkBoundsOrder(from, to);
        char[] cs = new char[length];
        for (int i = 0; i < length; i++) {
            cs[i] = nextChar(from, to);
        }
        return new String(cs);
    }

    /**
     * <p>
     * Returns a random string of which length is specified, and of which content consist of random characters from
     * given character array.
     * </p>
     * 
     * @param length
     *            specified length, 0 or positive
     * @param set
     *            given character array, not null or 0-length
     * @return a random string, not null
     * @throws NullPointerException
     *             if {@code set} is null
     * @throws IllegalArgumentException
     *             if {@code length} is negative and/or {@code set} is 0-length
     * @since 0.0.0
     */
    public static String nextString(int length, char[] set) throws NullPointerException, IllegalArgumentException {
        Checker.checkLength(length);
        Checker.checkNull(set);
        char[] cs = new char[length];
        for (int i = 0; i < length; i++) {
            cs[i] = set[nextInt(0, set.length - 1)];
        }
        return new String(cs);
    }

    /**
     * <p>
     * Returns a random string of which length of words is specified, and of which content of words consist of random
     * string from given words array (a null word will be seen as an empty string).
     * </p>
     * 
     * @param length
     *            specified length of words, 0 or positive
     * @param words
     *            given words array, not null or 0-length
     * @return a random string, not null
     * @throws NullPointerException
     *             if {@code set} is null
     * @throws IllegalArgumentException
     *             if {@code length} is negative and/or {@code set} is 0-length
     * @since 0.0.0
     */
    public static String nextString(int length, String[] words) throws NullPointerException, IllegalArgumentException {
        Checker.checkPositiveOr0(length);
        Checker.checkNull(words);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String word = words[nextInt(0, words.length - 1)];
            sb.append(word == null ? "" : word);
        }
        return sb.toString();
    }
}
