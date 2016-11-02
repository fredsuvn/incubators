package com.cogician.quicker.util;

import java.util.Arrays;

import com.cogician.quicker.Checker;
import com.cogician.quicker.OutOfBoundsException;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * Static quick utility class provides static methods for math.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-12-28 09:47:11
 * @since 0.0.0
 */
public class MathQuicker {

    /**
     * <p>
     * Returns whether given number is a prime.
     * </p>
     * 
     * @param n
     *            given number
     * @return whether given number is a prime
     * @since 0.0.0
     */
    public static boolean isPrime(int n) {
        if (n <= 3) {
            return n > 1;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Returns whether given number is a prime.
     * </p>
     * 
     * @param n
     *            given number
     * @return whether given number is a prime
     * @since 0.0.0
     */
    public static boolean isPrime(long n) {
        if (n <= 3L) {
            return n > 1L;
        }
        if (n % 2L == 0L || n % 3L == 0L) {
            return false;
        }
        for (long i = 5L; i * i <= n; i += 6L) {
            if (n % i == 0L || n % (i + 2L) == 0L) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Returns whether given number is a composite number.
     * </p>
     * 
     * @param n
     *            given number
     * @return whether given number is a composite number
     * @since 0.0.0
     */
    public static boolean isComposite(int n) {
        if (n <= 4) {
            return n == 4;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return true;
        }
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Returns whether given number is a composite number.
     * </p>
     * 
     * @param n
     *            given number
     * @return whether given number is a composite number
     * @since 0.0.0
     */
    public static boolean isComposite(long n) {
        if (n <= 4L) {
            return n == 4L;
        }
        if (n % 2L == 0L || n % 3L == 0L) {
            return true;
        }
        for (long i = 5L; i * i <= n; i += 6L) {
            if (n % i == 0L || n % (i + 2L) == 0L) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Gets maximum prime number of specified number. Returns -1L if no maximum prime number.
     * </p>
     * 
     * @param l
     *            specified number
     * @return maximum prime number of specified number
     * @since 0.0.0
     */
    public static int maxPrime(int n) {
        if (n < 2) {
            return -1;
        }
        for (; n >= 2; n--) {
            if (isPrime(n)) {
                return n;
            }
        }
        return -1;
    }

    /**
     * <p>
     * Gets maximum prime number of specified number. Returns -1L if no maximum prime number.
     * </p>
     * 
     * @param n
     *            specified number
     * @return maximum prime number of specified number
     * @since 0.0.0
     */
    public static long maxPrime(long n) {
        if (n < 2L) {
            return -1L;
        }
        for (; n >= 2; n--) {
            if (isPrime(n)) {
                return n;
            }
        }
        return -1L;
    }

    /**
     * <p>
     * Returns whether given number is even number.
     * </p>
     * 
     * @param n
     *            given number
     * @return whether given number is even number
     * @since 0.0.0
     */
    public static boolean isEven(int n) {
        return n % 2 == 0;
    }

    /**
     * <p>
     * Returns whether given number is even number.
     * </p>
     * 
     * @param n
     *            given number
     * @return whether given number is even number
     * @since 0.0.0
     */
    public static boolean isEven(long n) {
        return n % 2 == 0;
    }

    /**
     * <p>
     * Returns whether given number is odd number.
     * </p>
     * 
     * @param n
     *            given number
     * @return whether given number is odd number
     * @since 0.0.0
     */
    public static boolean isOdd(int n) {
        return !isEven(n);
    }

    /**
     * <p>
     * Returns whether given number is odd number.
     * </p>
     * 
     * @param n
     *            given number
     * @return whether given number is odd number
     * @since 0.0.0
     */
    public static boolean isOdd(long n) {
        return !isEven(n);
    }

    /**
     * <p>
     * Returns greatest common divisor. Negative argument will be seen as its absolute value.
     * </p>
     * 
     * @param m
     *            first argument
     * @param n
     *            second argument
     * @return greatest common divisor
     * @since 0.0.0
     */
    public static int gcd(int m, int n) {
        m = Math.abs(m);
        n = Math.abs(n);
        int max = Math.max(m, n);
        int min = Math.min(m, n);
        int remainder = max % min;
        while (remainder != 0) {
            max = min;
            min = remainder;
            remainder = max % min;
        }
        return min;
    }

    /**
     * <p>
     * Returns greatest common divisor. Negative argument will be seen as its absolute value.
     * </p>
     * 
     * @param m
     *            first argument
     * @param n
     *            second argument
     * @return greatest common divisor
     * @since 0.0.0
     */
    public static long gcd(long m, long n) {
        m = Math.abs(m);
        n = Math.abs(n);
        long max = Math.max(m, n);
        long min = Math.min(m, n);
        long remainder = max % min;
        while (remainder != 0) {
            max = min;
            min = remainder;
            remainder = max % min;
        }
        return min;
    }

    /**
     * <p>
     * Returns least common multiple. Negative argument will be seen as its absolute value.
     * </p>
     * 
     * @param m
     *            first argument
     * @param n
     *            second argument
     * @return least common multiple
     * @since 0.0.0
     */
    public static int lcm(int m, int n) {
        return Math.abs(m * n / gcd(m, n));
    }

    /**
     * <p>
     * Returns least common multiple. Negative argument will be seen as its absolute value.
     * </p>
     * 
     * @param m
     *            first argument
     * @param n
     *            second argument
     * @return least common multiple
     * @since 0.0.0
     */
    public static long lcm(long m, long n) {
        return Math.abs(m * n / gcd(m, n));
    }

    /**
     * <p>
     * Expansion euclid algorithm to return an array [g, x, y]. Let the gcd(m, n) be the greatest common divisor:
     * 
     * <pre>
     * g=gcd(m, n);
     * g, x, y meet:
     * mx+ny=g;
     * </pre>
     * 
     * </p>
     * 
     * @param m
     *            first argument
     * @param n
     *            second argument
     * @return an array [g, x, y] to meet mx+ny=g
     * @since 0.0.0
     */
    public static int[] exgcd(int m, int n) {
        m = Math.abs(m);
        n = Math.abs(n);

        int r00 = 1, r01 = 0; // [1 0]
        int r10 = 0, r11 = 1; // [0 1]

        int g = 0;
        int x = r00;
        int y = r01;
        while (n != 0) {
            int remainder = m % n;
            int quotient = m / n;

            if (remainder == 0) {
                x = r10;
                y = r11;
            } else {
                int tmp0 = r00 - quotient * r10;
                int tmp1 = r01 - quotient * r11;
                r00 = r10;
                r01 = r11;
                r10 = tmp0;
                r11 = tmp1;
            }
            m = n;
            n = remainder;
        }
        g = m;
        return new int[]{g, x, y};
    }

    /**
     * <p>
     * Expansion euclid algorithm to return an array [g, x, y]. Let the gcd(m, n) be the greatest common divisor:
     * 
     * <pre>
     * g=gcd(m, n);
     * g, x, y meet:
     * mx+ny=g;
     * </pre>
     * 
     * </p>
     * 
     * @param m
     *            first argument
     * @param n
     *            second argument
     * @return an array [g, x, y] to meet mx+ny=g
     * @since 0.0.0
     */
    public static long[] exgcd(long m, long n) {
        m = Math.abs(m);
        n = Math.abs(n);

        long r00 = 1, r01 = 0; // [1 0]
        long r10 = 0, r11 = 1; // [0 1]

        long g = 0;
        long x = r00;
        long y = r01;
        while (n != 0) {
            long remainder = m % n;
            long quotient = m / n;

            if (remainder == 0) {
                x = r10;
                y = r11;
            } else {
                long tmp0 = r00 - quotient * r10;
                long tmp1 = r01 - quotient * r11;
                r00 = r10;
                r01 = r11;
                r10 = tmp0;
                r11 = tmp1;
            }
            m = n;
            n = remainder;
        }
        g = m;
        return new long[]{g, x, y};
    }

    /**
     * <p>
     * Returns a group of simple RSA numbers which meet following condition:
     * 
     * <pre>
     * (D * E) mod ((P-1) * (Q-1)) = 1
     * </pre>
     * 
     * </p>
     * <p>
     * Given seed is the seed of P. It must greater than or equal to 5 (if not, it will be seen as 5). If P == 5 (or
     * seen as 5), this method will return:
     * 
     * <pre>
     * int[] ret = {3, 3, 5, 3};
     * int d = ret[0];
     * int e = ret[1];
     * int p = ret[2];
     * int q = ret[3];
     * </pre>
     * 
     * </p>
     * <p>
     * The returned array has 4 elements and in order of: D, E, P, Q.
     * </p>
     * 
     * @param seed
     *            given seed
     * @return an array [D, E, P, Q] to meet (D * E) mod ((P-1) * (Q-1)) = 1
     * @since 0.0.0
     */
    public static int[] simpleRSAGroup(int seed) {
        long initial = Quicker.atLeast(seed, 5);
        while (true) {
            if (initial * initial > Integer.MAX_VALUE) {
                initial /= 2;
                continue;
            }
            int p = maxPrime(Quicker.atLeast((int)initial, 5));
            int q = maxPrime(Quicker.atLeast((int)initial / 2, 3));
            if (p == q) {
                initial--;
                continue;
            }
            int pq = (p - 1) * (q - 1);
            int e = maxPrime((int)pq / 2);
            int d = exgcd(e, pq)[1];
            d = d < 0 ? pq + d : d;
            return new int[]{d, e, p, q};
        }
    }

    /**
     * <p>
     * Returns a^b.
     * </p>
     * 
     * @param a
     *            the base
     * @param b
     *            the exponent
     * @return a^b
     * @since 0.0.0
     */
    public static long pow(long a, long b) {
        long result = a;
        for (long i = b; i > 1; i--) {
            result *= result;
        }
        return result;
    }

    /**
     * <p>
     * Returns multiplication of given array between from index inclusive and to index exclusive.
     * </p>
     * 
     * @param ints
     *            given array
     * @param from
     *            from index inclusive
     * @param to
     *            to index exclusive
     * @return multiplication of given array between from index inclusive and to index exclusive
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if from < to
     * @throws ArrayIndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public static long multiple(int[] ints, int from, int to)
            throws NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Checker.checkNull(ints);
        Checker.checkBoundsOrder(from, to);
        long result = 0;
        for (int i = from; i < to; i++) {
            result *= ints[i];
        }
        return result;
    }

    /**
     * <p>
     * Splits given value to a group of int. The multiplication of the group int will be equal or closest greater than
     * given value, but the max size of each int cannot over given limit. Returns that int array in the order of small
     * to large.
     * </p>
     * 
     * @param v
     *            given value
     * @param limit
     *            given limit
     * @return int array in the order of small to large
     * @throws OutOfBoundsException
     *             if given value is too long
     * @since 0.0.0
     */
    public static int[] minPolynomial(long v, int limit) throws OutOfBoundsException {
        if (v < limit) {
            return new int[]{(int)v};
        }
        long length = 0;
        long rest = v;
        while (rest > limit) {
            length++;
            rest = rest / limit;
        }
        if (length >= Integer.MAX_VALUE) {
            throw new OutOfBoundsException();
        }
        int[] result = new int[(int)(length + 1)];
        Arrays.fill(result, 1, result.length, limit);
        if (rest * pow(limit, length) == v) {
            result[0] = (int)rest;
        } else {
            result[0] = (int)rest + 1;
            narrow(v, limit, 1, result);
        }
        return result;
    }

    private static void narrow(long v, int limit, int now, int[] ints) {
        if (now == ints.length) {
            return;
        }
        long rest = v;
        for (int i = 0; i < ints.length; i++) {
            if (i != now) {
                rest /= ints[i];
            }
        }
        if (v % rest != 0) {
            rest++;
        }
        if (rest < limit) {
            ints[now] = (int)rest;
            narrow(v, limit, ++now, ints);
        } else {
            narrow(v, limit, ints.length, ints);
        }
    }

    public static void main(String[] args) {
        System.out.println(gcd(12, 6));
        System.out.println(gcd(12, 8));
        System.out.println(lcm(12, 8));
        System.out.println(maxPrime(1000000));
        System.out.println(Arrays.toString(exgcd(3, 2)));
        System.out.println(540 * 1674 % 1769);
        System.out.println(Arrays.toString(simpleRSAGroup(5)));
        System.out.println(Long.MAX_VALUE);
        System.out.println((double)Long.MAX_VALUE);
        int[] a0 = {0};
        int[] a1 = {0};
        int[] a2 = {0};
        Quicker.each(100000, () -> {
            int r = RandomQuicker.nextInt(-1, 1);
            // System.out.println(r);
            switch (r) {
                case 0:
                    a0[0]++;
                    break;
                case 1:
                    a1[0]++;
                    break;
                case -1:
                    a2[0]++;
                    break;
            }
        });
        System.out.println("0: " + a0[0]);
        System.out.println("1: " + a1[0]);
        System.out.println("-1: " + a2[0]);

        System.out.println(ToStringQuicker.toString(minPolynomial(20000, 99)));
        System.out.println(ToStringQuicker.toString(minPolynomial(270, 100)));
    }
}
