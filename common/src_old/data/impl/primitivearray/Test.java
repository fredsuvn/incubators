package com.cogician.quicker.binary.data.impl.primitivearray;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cogician.quicker.binary.Bits;
import com.cogician.quicker.util.BitsQuicker;

/**
 * <p>
 * Tests.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-11-06 15:02:14
 * @since 0.0.0
 */
@Deprecated
class Test {

    public static void main(final String[] args) {
        System.out.println("Test start...");
        test();
    }

    private static final long BITS_TOTAL_NUM = 8L * 16L * 32L * 64L * 1000L;

    private static final Map<String, List<String>> ERRORS = new HashMap<String, List<String>>();

    private static int getLength(final long bits, final int wide) {
        return (int) (bits / wide);
    }

    private static Random r = new Random();

    private static long randomValueLong() {
        return r.nextLong();
    }

    private static int randomValueInt() {
        return r.nextInt();
    }

    private static int randomInBounds(final int bound) {
        return r.nextInt(bound);
    }

    private static byte randomValueByte() {
        return (byte) r.nextInt();
    }

    private static char randomValueChar() {
        return (char) r.nextInt();
    }

    private static short randomValueShort() {
        return (short) r.nextInt();
    }

    private static boolean randomValueBoolean() {
        return r.nextBoolean();
    }

    private static String outFileName = "out.txt";

    // public static void doTest(String fileName) {
    // outFileName = fileName;
    // test();
    // }
    
    private static void testByteCost(int length){
        long l1;
        long l2;
        
        byte[] array = new byte[length];
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= array.length - 1; i++){
            byte v = array[i];
            array[i] = ++v;
        }
        l2 = System.currentTimeMillis();
        System.out.println("byte[]: " + (l2 - l1) + ", length: " + array.length);
        
        ByteBuffer bb = ByteBuffer.allocate(length);
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= bb.capacity() - 1; i++){
            byte v = bb.get(i);
            bb.put(i, ++v);
        }
        l2 = System.currentTimeMillis();
        System.out.println("ByteBuffer: " + (l2 - l1) + ", length: " + bb.capacity());
        
        ByteBuffer bbd = ByteBuffer.allocateDirect(length);
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= bbd.capacity() - 1; i++){
            byte v = bbd.get(i);
            bb.put(i, ++v);
        }
        l2 = System.currentTimeMillis();
        System.out.println("ByteBufferDirect: " + (l2 - l1) + ", length: " + bbd.capacity());
        
        Bits ba = new ByteArrayBaseAccessor(new byte[length]);
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= ba.lengthInBytes() - 1; i++){
            byte v = ba.readByte(i);
            ba.writeByte(i, ++v);
        }
        l2 = System.currentTimeMillis();
        System.out.println("ByteArrayBaseAccessor: " + (l2 - l1) + ", length: " + ba.lengthInBytes());
        
        Bits aba = new AlignedByteArrayBaseAccessor(new byte[length]);
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= ba.lengthInBytes() - 4; i++){
            byte v = aba.readByte(i);
            aba.writeByte(i, ++v);
        }
        l2 = System.currentTimeMillis();
        System.out.println("AlignedByteArrayBaseAccessor: " + (l2 - l1) + ", length: " + ba.lengthInBytes());
    }
    
    private static void testIntCost(int length){
        long l1;
        long l2;
        
        ByteBuffer bb = ByteBuffer.allocate(length);
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= bb.capacity() - 4; i++){
            int v = bb.getInt(i);
            bb.putInt(i, ++v);
        }
        l2 = System.currentTimeMillis();
        System.out.println("ByteBuffer: " + (l2 - l1) + ", length: " + bb.capacity());
        
        ByteBuffer bbd = ByteBuffer.allocateDirect(length);
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= bbd.capacity() - 4; i++){
            int v = bbd.getInt(i);
            bb.putInt(i, ++v);
        }
        l2 = System.currentTimeMillis();
        System.out.println("ByteBufferDirect: " + (l2 - l1) + ", length: " + bbd.capacity());
        
        Bits ba = new ByteArrayBaseAccessor(new byte[length]);
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= ba.lengthInBytes() - 4; i++){
            int v = ba.readInt(i);
            ba.writeInt(i, ++v);
        }
        l2 = System.currentTimeMillis();
        System.out.println("ByteArrayBaseAccessor: " + (l2 - l1) + ", length: " + ba.lengthInBytes());
        
        Bits aba = new AlignedByteArrayBaseAccessor(new byte[length]);
        l1 = System.currentTimeMillis();
        for (int i = 0; i <= ba.lengthInBytes() - 4; i++){
            int v = aba.readInt(i);
            aba.writeInt(i, ++v);
        }
        l2 = System.currentTimeMillis();
        System.out.println("AlignedByteArrayBaseAccessor: " + (l2 - l1) + ", length: " + ba.lengthInBytes());
    }
    
    private static void testCreateObjCost(int count, int length){
        long l1;
        long l2;
        
        
        l1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++){
            byte[] array = new byte[length];
            array = null;
            System.gc();
        }
        l2 = System.currentTimeMillis();
        System.out.println("byte[]: " + (l2 - l1));
        
        
        l1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++){
            ByteBuffer bb = ByteBuffer.allocate(length);
            bb = null;
            System.gc();
        }
        l2 = System.currentTimeMillis();
        System.out.println("ByteBuffer: " + (l2 - l1));
        
        
//        l1 = System.currentTimeMillis();
//        for (int i = 0; i < count; i++){
//            ByteBuffer bbd = ByteBuffer.allocateDirect(length);
//            bbd = null;
//            System.gc();
//        }
//        l2 = System.currentTimeMillis();
//        System.out.println("ByteBufferDirect: " + (l2 - l1));
        
        
        l1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++){
            Bits ba = new ByteArrayBaseAccessor(new byte[length]);
            ba = null;
            System.gc();
        }
        l2 = System.currentTimeMillis();
        System.out.println("ByteArrayBaseAccessor: " + (l2 - l1));
        
        
        l1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++){
            Bits aba = new AlignedByteArrayBaseAccessor(new byte[length]);
            aba = null;
            System.gc();
        }
        l2 = System.currentTimeMillis();
        System.out.println("AlignedByteArrayBaseAccessor: " + (l2 - l1));
    }

    private static void test() {
//        // Test read write
//        test1to32Bits();
//        test33to64Bits();
//        testBoolean();
//        testByte();
//        testShort();
//        testChar();
//        testInt();
//        testLong();
//        outError();
//
//        // Test efficiency
//        for (int i = 0; i < 10; i++) {
//            testEfficiency();
//        }
        
        int length = 102400000;
        
//        System.out.println("byte read write:");
//        testByteCost(length);
//        System.out.println("int read write:");
//        testIntCost(length);
//        System.out.println("create obj:");
//        testCreateObjCost(1000, length);
//        long l1;
//        long l2;
//        //Object h = new ByteArrayBaseAccessor(new byte[100]);
//        byte[] bb = new byte[100];
//        l1 = System.currentTimeMillis();
//        for (int i = 0; i < 10000000; i++){
//            //Object obj = new ByteArrayBaseAccessor(new byte[8]);
//            //Object obj = new Object();
//            //Object obj = new byte[8];
//            Object obj = ByteBuffer.allocate(8);
//            Object obj = new BigInteger(bb);
//            //obj.toString();
//        }
//        l2 = System.currentTimeMillis();
//        System.out.println("Object: " + (l2 - l1));
        
        long l1 = Integer.MAX_VALUE;
        long l2 = l1 * l1;
        System.out.println(Long.toBinaryString(l2));
        System.out.println(Long.toBinaryString(Long.MAX_VALUE));
        System.out.println(Long.MAX_VALUE - l2);
    }

    private static Bits createBaseAccessorRandom(final Object array) {
        Bits acc = null;
        if (array.getClass().equals(boolean[].class)) {
            acc = new BooleanArrayBaseAccessor((boolean[]) array,
                    randomInBounds(10));
        } else if (array.getClass().equals(byte[].class)) {
            acc = new ByteArrayBaseAccessor((byte[]) array,
                    randomInBounds(100), randomInBounds(8));
        } else if (array.getClass().equals(short[].class)) {
            acc = new ShortArrayBaseAccessor((short[]) array,
                    randomInBounds(100), randomInBounds(16));
        } else if (array.getClass().equals(char[].class)) {
            acc = new CharArrayBaseAccessor((char[]) array,
                    randomInBounds(100), randomInBounds(16));
        } else if (array.getClass().equals(float[].class)) {
            acc = new FloatArrayBaseAccessor((float[]) array,
                    randomInBounds(100), randomInBounds(32));
        } else if (array.getClass().equals(int[].class)) {
            acc = new IntArrayBaseAccessor((int[]) array, randomInBounds(100),
                    randomInBounds(32));
        } else if (array.getClass().equals(long[].class)) {
            acc = new LongArrayBaseAccessor((long[]) array,
                    randomInBounds(100), randomInBounds(64));
        } else if (array.getClass().equals(double[].class)) {
            acc = new DoubleArrayBaseAccessor((double[]) array,
                    randomInBounds(100), randomInBounds(64));
        }
        return acc;
    }

    private static Bits createBaseAccessor(final Object array) {
        Bits acc = null;
        if (array.getClass().equals(boolean[].class)) {
            acc = new BooleanArrayBaseAccessor((boolean[]) array);
        } else if (array.getClass().equals(byte[].class)) {
            acc = new ByteArrayBaseAccessor((byte[]) array);
        } else if (array.getClass().equals(short[].class)) {
            acc = new ShortArrayBaseAccessor((short[]) array);
        } else if (array.getClass().equals(char[].class)) {
            acc = new CharArrayBaseAccessor((char[]) array);
        } else if (array.getClass().equals(float[].class)) {
            acc = new FloatArrayBaseAccessor((float[]) array);
        } else if (array.getClass().equals(int[].class)) {
            acc = new IntArrayBaseAccessor((int[]) array);
        } else if (array.getClass().equals(long[].class)) {
            acc = new LongArrayBaseAccessor((long[]) array);
        } else if (array.getClass().equals(double[].class)) {
            acc = new DoubleArrayBaseAccessor((double[]) array);
        }
        return acc;
    }

    private static void test1to32Bits() {
        Bits acc = null;
        for (int i = 1; i <= 32; i++) {
            // boolean
            final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
            acc = createBaseAccessorRandom(bs);
            testReadWriteBitsAsInt(acc, i);
            // byte
            final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
            acc = createBaseAccessorRandom(bbs);
            testReadWriteBitsAsInt(acc, i);
            // short
            final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
            acc = createBaseAccessorRandom(ss);
            testReadWriteBitsAsInt(acc, i);
            // char
            final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
            acc = createBaseAccessorRandom(cs);
            testReadWriteBitsAsInt(acc, i);
            // int
            final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
            acc = createBaseAccessorRandom(is);
            testReadWriteBitsAsInt(acc, i);
            // float
            final float[] fs = new float[getLength(BITS_TOTAL_NUM, 32)];
            acc = createBaseAccessorRandom(fs);
            testReadWriteBitsAsInt(acc, i);
            // long
            final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
            acc = createBaseAccessorRandom(ls);
            testReadWriteBitsAsInt(acc, i);
            // double
            final double[] ds = new double[getLength(BITS_TOTAL_NUM, 64)];
            acc = createBaseAccessorRandom(ds);
            testReadWriteBitsAsInt(acc, i);
        }
    }

    private static void test33to64Bits() {
        Bits acc = null;
        for (int i = 1; i <= 64; i++) {
            // boolean
            final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
            acc = createBaseAccessorRandom(bs);
            testReadWriteBitsAsLong(acc, i);
            // byte
            final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
            acc = createBaseAccessorRandom(bbs);
            testReadWriteBitsAsLong(acc, i);
            // short
            final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
            acc = createBaseAccessorRandom(ss);
            testReadWriteBitsAsLong(acc, i);
            // char
            final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
            acc = createBaseAccessorRandom(cs);
            testReadWriteBitsAsLong(acc, i);
            // int
            final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
            acc = createBaseAccessorRandom(is);
            testReadWriteBitsAsLong(acc, i);
            // float
            final float[] fs = new float[getLength(BITS_TOTAL_NUM, 32)];
            acc = createBaseAccessorRandom(fs);
            testReadWriteBitsAsLong(acc, i);
            // long
            final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
            acc = createBaseAccessorRandom(ls);
            testReadWriteBitsAsLong(acc, i);
            // double
            final double[] ds = new double[getLength(BITS_TOTAL_NUM, 64)];
            acc = createBaseAccessorRandom(ds);
            testReadWriteBitsAsLong(acc, i);
        }
    }

    private static void testBoolean() {
        Bits acc = null;
        // boolean
        final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
        acc = createBaseAccessorRandom(bs);
        testReadWriteBoolean(acc);
        // byte
        final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
        acc = createBaseAccessorRandom(bbs);
        testReadWriteBoolean(acc);
        // short
        final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(ss);
        testReadWriteBoolean(acc);
        // char
        final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(cs);
        testReadWriteBoolean(acc);
        // int
        final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(is);
        testReadWriteBoolean(acc);
        // float
        final float[] fs = new float[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(fs);
        testReadWriteBoolean(acc);
        // long
        final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ls);
        testReadWriteBoolean(acc);
        // double
        final double[] ds = new double[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ds);
        testReadWriteBoolean(acc);
    }

    private static void testByte() {
        Bits acc = null;
        // boolean
        final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
        acc = createBaseAccessorRandom(bs);
        testReadWriteByte(acc);
        // byte
        final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
        acc = createBaseAccessorRandom(bbs);
        testReadWriteByte(acc);
        // short
        final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(ss);
        testReadWriteByte(acc);
        // char
        final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(cs);
        testReadWriteByte(acc);
        // int
        final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(is);
        testReadWriteByte(acc);
        // float
        final float[] fs = new float[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(fs);
        testReadWriteByte(acc);
        // long
        final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ls);
        testReadWriteByte(acc);
        // double
        final double[] ds = new double[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ds);
        testReadWriteByte(acc);
    }

    private static void testShort() {
        Bits acc = null;
        // boolean
        final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
        acc = createBaseAccessorRandom(bs);
        testReadWriteShort(acc);
        // byte
        final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
        acc = createBaseAccessorRandom(bbs);
        testReadWriteShort(acc);
        // short
        final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(ss);
        testReadWriteShort(acc);
        // char
        final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(cs);
        testReadWriteShort(acc);
        // int
        final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(is);
        testReadWriteShort(acc);
        // float
        final float[] fs = new float[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(fs);
        testReadWriteShort(acc);
        // long
        final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ls);
        testReadWriteShort(acc);
        // double
        final double[] ds = new double[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ds);
        testReadWriteShort(acc);
    }

    private static void testChar() {
        Bits acc = null;
        // boolean
        final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
        acc = createBaseAccessorRandom(bs);
        testReadWriteChar(acc);
        // byte
        final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
        acc = createBaseAccessorRandom(bbs);
        testReadWriteChar(acc);
        // short
        final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(ss);
        testReadWriteChar(acc);
        // char
        final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(cs);
        testReadWriteChar(acc);
        // int
        final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(is);
        testReadWriteChar(acc);
        // float
        final float[] fs = new float[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(fs);
        testReadWriteChar(acc);
        // long
        final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ls);
        testReadWriteChar(acc);
        // double
        final double[] ds = new double[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ds);
        testReadWriteChar(acc);
    }

    private static void testInt() {
        Bits acc = null;
        // boolean
        final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
        acc = createBaseAccessorRandom(bs);
        testReadWriteInt(acc);
        // byte
        final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
        acc = createBaseAccessorRandom(bbs);
        testReadWriteInt(acc);
        // short
        final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(ss);
        testReadWriteInt(acc);
        // char
        final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(cs);
        testReadWriteInt(acc);
        // int
        final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(is);
        testReadWriteInt(acc);
        // float
        final float[] fs = new float[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(fs);
        testReadWriteInt(acc);
        // long
        final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ls);
        testReadWriteInt(acc);
        // double
        final double[] ds = new double[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ds);
        testReadWriteInt(acc);
    }

    private static void testLong() {
        Bits acc = null;
        // boolean
        final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
        acc = createBaseAccessorRandom(bs);
        testReadWriteLong(acc);
        // byte
        final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
        acc = createBaseAccessorRandom(bbs);
        testReadWriteLong(acc);
        // short
        final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(ss);
        testReadWriteLong(acc);
        // char
        final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessorRandom(cs);
        testReadWriteLong(acc);
        // int
        final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(is);
        testReadWriteLong(acc);
        // float
        final float[] fs = new float[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessorRandom(fs);
        testReadWriteLong(acc);
        // long
        final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ls);
        testReadWriteLong(acc);
        // double
        final double[] ds = new double[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessorRandom(ds);
        testReadWriteLong(acc);
    }

    private static void setError(final String className, final String errorInfo) {
        List<String> classErrors = ERRORS.get(className);
        if (classErrors == null) {
            classErrors = new ArrayList<>();
            ERRORS.put(className, classErrors);
        }
        classErrors.add(errorInfo + ", in " + className);
    }

    private static void testReadWriteBitsAsInt(final Bits acc,
            final int bitsNum) {
        out("Test class(testReadWriteBitsAsInt):-----------------------------------"
                + acc.getClass().getName());
        final long s1 = System.currentTimeMillis();
        for (long l = 0L; l + bitsNum <= acc.lengthInBits(); l++) {
            final int v = randomValueInt();
            acc.writeBits(l, v, bitsNum);
            final int v1 = BitsQuicker.partOfInt(v, 0, bitsNum);
            final int v2 = acc.readBitsAsInt(l, bitsNum);
            final String info = "testReadWriteBitsAsInt, v1= "
                    + Integer.toBinaryString(v1) + ", v2= "
                    + Integer.toBinaryString(v2) + ", v1==v2: "
                    + (v1 == v2 ? "true" : "false") + ", at: " + l;
            // out(info);
            if (v1 != v2) {
                setError(acc.getClass().getName(), info);
            }
        }
        final long s2 = System.currentTimeMillis();
        out("cost: " + (s2 - s1)
                + "*********************************************END!");
    }

    private static void testReadWriteBitsAsLong(final Bits acc,
            final int bitsNum) {
        out("Test class(testReadWriteBitsAsLong):-----------------------------------"
                + acc.getClass().getName());
        final long s1 = System.currentTimeMillis();
        for (long l = 0L; l + bitsNum <= acc.lengthInBits(); l++) {
            final long v = randomValueLong();
            acc.writeBits(l, v, bitsNum);
            final long v1 = BitsQuicker.partOfLong(v, 0, bitsNum);
            final long v2 = acc.readBitsAsLong(l, bitsNum);
            final String info = "testReadWriteBitsAsLong, v1= "
                    + Long.toBinaryString(v1) + ", v2= "
                    + Long.toBinaryString(v2) + ", v1==v2: "
                    + (v1 == v2 ? "true" : "false") + ", at: " + l;
            // out(info);
            if (v1 != v2) {
                setError(acc.getClass().getName(), info);
            }
        }
        final long s2 = System.currentTimeMillis();
        out("cost: " + (s2 - s1)
                + "*********************************************END!");
    }

    private static void testReadWriteBoolean(final Bits acc) {
        out("Test class(testReadWriteBoolean):-----------------------------------"
                + acc.getClass().getName());
        final long s1 = System.currentTimeMillis();
        for (long l = 0L; l + 1 <= acc.lengthInBits(); l++) {
            final boolean v1 = randomValueBoolean();
            acc.writeBoolean(l, v1);
            final boolean v2 = acc.readBoolean(l);
            final String info = "testReadWriteBoolean, v1= " + v1 + ", v2= "
                    + v2 + ", v1==v2: " + (v1 == v2 ? "true" : "false")
                    + ", at: " + l;
            // out(info);
            if (v1 != v2) {
                setError(acc.getClass().getName(), info);
            }
        }
        final long s2 = System.currentTimeMillis();
        out("cost: " + (s2 - s1)
                + "*********************************************END!");
    }

    private static void testReadWriteByte(final Bits acc) {
        out("Test class(testReadWriteByte):-----------------------------------"
                + acc.getClass().getName());
        final long s1 = System.currentTimeMillis();
        for (long l = 0L; l + 8 <= acc.lengthInBits(); l++) {
            final byte v1 = randomValueByte();
            acc.writeByte(l, v1);
            final byte v2 = acc.readByte(l);
            final String info = "testReadWriteByte, v1= " + v1 + ", v2= " + v2
                    + ", v1==v2: " + (v1 == v2 ? "true" : "false") + ", at: "
                    + l;
            // out(info);
            if (v1 != v2 || v1 == 0 || v2 == 0) {
                setError(acc.getClass().getName(), info);
            }
        }
        final long s2 = System.currentTimeMillis();
        out("cost: " + (s2 - s1)
                + "*********************************************END!");
    }

    private static void testReadWriteShort(final Bits acc) {
        out("Test class(testReadWriteShort):-----------------------------------"
                + acc.getClass().getName());
        final long s1 = System.currentTimeMillis();
        for (long l = 0L; l + 16 <= acc.lengthInBits(); l++) {
            final short v1 = randomValueShort();
            acc.writeShort(l, v1);
            final short v2 = acc.readShort(l);
            final String info = "testReadWriteShort, v1= " + v1 + ", v2= " + v2
                    + ", v1==v2: " + (v1 == v2 ? "true" : "false") + ", at: "
                    + l;
            // out(info);
            if (v1 != v2 || v1 == 0 || v2 == 0) {
                setError(acc.getClass().getName(), info);
            }
        }
        final long s2 = System.currentTimeMillis();
        out("cost: " + (s2 - s1)
                + "*********************************************END!");
    }

    private static void testReadWriteChar(final Bits acc) {
        out("Test class(testReadWriteChar):-----------------------------------"
                + acc.getClass().getName());
        final long s1 = System.currentTimeMillis();
        for (long l = 0L; l + 16 <= acc.lengthInBits(); l++) {
            final char v1 = randomValueChar();
            acc.writeChar(l, v1);
            final char v2 = acc.readChar(l);
            final String info = "testReadWriteChar, v1= " + v1 + ", v2= " + v2
                    + ", v1==v2: " + (v1 == v2 ? "true" : "false") + ", at: "
                    + l;
            // out(info);
            if (v1 != v2 || v1 == 0 || v2 == 0) {
                setError(acc.getClass().getName(), info);
            }
        }
        final long s2 = System.currentTimeMillis();
        out("cost: " + (s2 - s1)
                + "*********************************************END!");
    }

    private static void testReadWriteInt(final Bits acc) {
        out("Test class(testReadWriteInt):-----------------------------------"
                + acc.getClass().getName());
        final long s1 = System.currentTimeMillis();
        for (long l = 0L; l + 32 <= acc.lengthInBits(); l++) {
            final int v1 = randomValueInt();
            acc.writeInt(l, v1);
            final int v2 = acc.readInt(l);
            final String info = "testReadWriteInt, v1= " + v1 + ", v2= " + v2
                    + ", v1==v2: " + (v1 == v2 ? "true" : "false") + ", at: "
                    + l;
            // out(info);
            if (v1 != v2 || v1 == 0 || v2 == 0) {
                setError(acc.getClass().getName(), info);
            }
        }
        final long s2 = System.currentTimeMillis();
        out("cost: " + (s2 - s1)
                + "*********************************************END!");
    }

    private static void testReadWriteLong(final Bits acc) {
        out("Test class(testReadWriteLong):-----------------------------------"
                + acc.getClass().getName());
        final long s1 = System.currentTimeMillis();
        for (long l = 0L; l + 64 <= acc.lengthInBits(); l++) {
            final long v1 = randomValueLong();
            acc.writeLong(l, v1);
            final long v2 = acc.readLong(l);
            final String info = "testReadWriteLong, v1= " + v1 + ", v2= " + v2
                    + ", v1==v2: " + (v1 == v2 ? "true" : "false") + ", at: "
                    + l;
            // out(info);
            if (v1 != v2 || v1 == 0 || v2 == 0) {
                setError(acc.getClass().getName(), info);
            }
        }
        final long s2 = System.currentTimeMillis();
        out("cost: " + (s2 - s1)
                + "*********************************************END!");
    }

    private static void testEfficiency() {
        out("Test Efficiency----------------------");
        Bits acc = null;
        long s1;
        long s2;

        // boolean
        final boolean[] bs = new boolean[getLength(BITS_TOTAL_NUM, 1)];
        acc = createBaseAccessor(bs);
        s1 = System.currentTimeMillis();
        for (int i = 0; i < bs.length; i++) {
            final boolean x = bs[i];
            bs[i] = !x;
        }
        s2 = System.currentTimeMillis();
        out("boolean array cost " + (s2 - s1));
        s1 = System.currentTimeMillis();
        for (long l = 0; l < acc.lengthInBits(); l++) {
            final boolean x = acc.readBoolean(l);
            acc.writeBoolean(l, !x);
        }
        s2 = System.currentTimeMillis();
        out("boolean accessor cost " + (s2 - s1));

        // byte
        final byte[] bbs = new byte[getLength(BITS_TOTAL_NUM, 8)];
        acc = createBaseAccessor(bbs);
        s1 = System.currentTimeMillis();
        for (int i = 0; i < bbs.length; i++) {
            byte x = bbs[i];
            bbs[i] = ++x;
        }
        s2 = System.currentTimeMillis();
        out("byte array cost " + (s2 - s1));
        s1 = System.currentTimeMillis();
        for (long l = 0; l + 8 <= acc.lengthInBits(); l += 8) {
            byte x = acc.readByte(l);
            acc.writeByte(l, ++x);
        }
        s2 = System.currentTimeMillis();
        out("byte accessor cost " + (s2 - s1));

        // short
        final short[] ss = new short[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessor(ss);
        s1 = System.currentTimeMillis();
        for (int i = 0; i < ss.length; i++) {
            short x = ss[i];
            ss[i] = ++x;
        }
        s2 = System.currentTimeMillis();
        out("short array cost " + (s2 - s1));
        s1 = System.currentTimeMillis();
        for (long l = 0; l + 16 <= acc.lengthInBits(); l += 16) {
            short x = acc.readShort(l);
            acc.writeShort(l, ++x);
        }
        s2 = System.currentTimeMillis();
        out("short accessor cost " + (s2 - s1));

        // char
        final char[] cs = new char[getLength(BITS_TOTAL_NUM, 16)];
        acc = createBaseAccessor(cs);
        s1 = System.currentTimeMillis();
        for (int i = 0; i < cs.length; i++) {
            char x = cs[i];
            cs[i] = ++x;
        }
        s2 = System.currentTimeMillis();
        out("char array cost " + (s2 - s1));
        s1 = System.currentTimeMillis();
        for (long l = 0; l + 16 <= acc.lengthInBits(); l += 16) {
            char x = acc.readChar(l);
            acc.writeChar(l, ++x);
        }
        s2 = System.currentTimeMillis();
        out("char accessor cost " + (s2 - s1));

        // int
        final int[] is = new int[getLength(BITS_TOTAL_NUM, 32)];
        acc = createBaseAccessor(is);
        s1 = System.currentTimeMillis();
        for (int i = 0; i < is.length; i++) {
            int x = is[i];
            is[i] = ++x;
        }
        s2 = System.currentTimeMillis();
        out("int array cost " + (s2 - s1));
        s1 = System.currentTimeMillis();
        for (long l = 0; l + 32 <= acc.lengthInBits(); l += 32) {
            int x = acc.readInt(l);
            acc.writeInt(l, ++x);
        }
        s2 = System.currentTimeMillis();
        out("int accessor cost " + (s2 - s1));

        // long
        final long[] ls = new long[getLength(BITS_TOTAL_NUM, 64)];
        acc = createBaseAccessor(ls);
        s1 = System.currentTimeMillis();
        for (int i = 0; i < ls.length; i++) {
            long x = ls[i];
            ls[i] = ++x;
        }
        s2 = System.currentTimeMillis();
        out("long array cost " + (s2 - s1));
        s1 = System.currentTimeMillis();
        for (long l = 0; l + 64 <= acc.lengthInBits(); l += 64) {
            long x = acc.readLong(l);
            acc.writeLong(l, ++x);
        }
        s2 = System.currentTimeMillis();
        out("long accessor cost " + (s2 - s1));

        out("Test Efficiency Ending----------------------");
    }

    private static void outError() {
        out("Output errors:-------------------------------------");
        ERRORS.forEach((key, list) -> {
            list.forEach(info -> {
                out(info);
            });
        });
        out("ENDING-------------------------------------------------");
    }

    private static FileWriter fw = null;

    private static FileWriter getFw() {
        if (null == fw) {
            try {
                fw = new FileWriter(outFileName);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return fw;
    }

    private static void out(final String str) {
        try {
            getFw().write(str + "\r\n");
            getFw().flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
