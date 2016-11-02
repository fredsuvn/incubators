package com.cogician.quicker.bigarray;

import com.cogician.quicker.Quicker;
import com.cogician.quicker.util.RandomQuicker;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-03-17T10:33:36+08:00
 * @since 0.0.0, 2016-03-17T10:33:36+08:00
 */
public class TestBigArray {

    /**
     * <p>
     * 
     * </p>
     * 
     * @param args
     * @since 0.0.0
     */
    public static void main(String[] args) {
        testWrapper();
    }

    public static void testObj() {
        int[] length = {100};
        ObjectArray<String> strs = new ObjectArray<>(length[0], "haha");
        System.out.println(strs);
        Quicker.each(10, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            String value = RandomQuicker.nextString(3);
            strs.set(index, value);
            String result = strs.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(strs.clone(), str -> {
            System.out.println(str);
        });

        length[0] = 10;
        ObjectArray<String> strs2 = new ObjectArray<>(length[0], "hei");
        System.out.println(strs2);
        // List<Integer> list = new ArrayList<>();
        Quicker.each(2, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            String value = RandomQuicker.nextString(3);
            strs2.set(index, value);
            String result = strs2.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(strs2.clone(), str -> {
            System.out.println(str);
        });

        length[0] = 4;
        ObjectArray<String> strs3 = new ObjectArray<>(length[0], "hei");
        System.out.println(strs3);
        Quicker.each(1, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            String value = RandomQuicker.nextString(3);
            strs3.set(index, value);
            String result = strs3.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(strs3.clone(), str -> {
            System.out.println(str);
        });
    }

    public static void testByte() {
        int[] length = {100};
        ByteArray strs = new ByteArray(length[0], (byte)8);
        System.out.println(strs);
        Quicker.each(10, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            byte value = RandomQuicker.nextByte();
            strs.set(index, value);
            byte result = strs.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(strs.clone(), str -> {
            System.out.println(str);
        });

        length[0] = 10;
        ByteArray str2 = new ByteArray(length[0], (byte)16);
        System.out.println(str2);
        Quicker.each(2, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            byte value = RandomQuicker.nextByte();
            str2.set(index, value);
            byte result = str2.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(str2.clone(), str -> {
            System.out.println(str);
        });

        length[0] = 4;
        ByteArray str3 = new ByteArray(length[0], (byte)32);
        System.out.println(str3);
        Quicker.each(1, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            byte value = RandomQuicker.nextByte();
            str3.set(index, value);
            byte result = str3.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(str3.clone(), str -> {
            System.out.println(str);
        });
    }

    public static void testWrapper() {
        int[] length = {100};
        ByteArray strs = new ByteArray(new byte[length[0] + 100], 100, length[0] + 100);
        System.out.println(strs);
        Quicker.each(10, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            byte value = RandomQuicker.nextByte();
            strs.set(index, value);
            byte result = strs.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(strs, str -> {
            System.out.println(str);
        });

        length[0] = 10;
        ByteArray str2 = new ByteArray(new byte[length[0]]);
        System.out.println(str2);
        Quicker.each(2, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            byte value = RandomQuicker.nextByte();
            str2.set(index, value);
            byte result = str2.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(str2.clone(), str -> {
            System.out.println(str);
        });

        length[0] = 4;
        ByteArray str3 = new ByteArray(new byte[length[0]]);
        System.out.println(str3);
        Quicker.each(1, () -> {
            int index = RandomQuicker.nextInt(0, length[0] - 1);
            byte value = RandomQuicker.nextByte();
            str3.set(index, value);
            byte result = str3.get(index);
            System.out.println(
                    "index= " + index + ", set= " + value + ", get= " + result + ", they are " + (value == result));
        });
        Quicker.each(str3.clone(), str -> {
            System.out.println(str);
        });
    }
}
