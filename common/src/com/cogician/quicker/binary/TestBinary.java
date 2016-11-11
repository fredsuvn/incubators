package com.cogician.quicker.binary;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel.MapMode;

import com.cogician.quicker.Quicker;
import com.cogician.quicker.util.tostring.QuickToStrings;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-24T09:27:27+08:00
 * @since 0.0.0, 2016-08-24T09:27:27+08:00
 */
public class TestBinary {

    /**
     * <p>
     * 
     * </p>
     * 
     * @param args
     * @since 0.0.0
     */
    public static void main(String[] args) throws Exception {
        testRandomaAndMapped();
        // testBigArrayBinary();
        // testInputerOutputer();
    }

    public static void testByteArrayBinary() {
        Binary bin = Binary.wrap(new byte[10086], 0, 10086, ByteOrderProcessor.LITTLE_ENDIAN);
        bin.setInt(5522222, 7777);
        System.out.println(bin.getInt(55));
        System.out.println(bin.getFloat(55));
        System.out.println(Float.floatToRawIntBits(bin.getFloat(55)));
        bin.setDouble(0, 3.1415926);
        System.out.println(bin.getDouble(0));
        System.out.println(bin.getLong(0));

        bin.setLong(666, 12306);
        System.out.println(bin.getLong(666));
        bin.setFloat(666, 6.66f);
        System.out.println(bin.getFloat(666));

        System.out.println(bin.getBinary(0, 8).getDouble(0));
        System.out.println(bin.subBinary(0, 8).getDouble(0));
        Binary bin2 = Binary.wrap(new byte[10086], 0, 10086, ByteOrderProcessor.LITTLE_ENDIAN);
        bin.copy(0, bin2, 100, 8);
        System.out.println(bin2.getDouble(100));

        Binary bin3 = Binary.alloc(10086, ByteOrderProcessor.LITTLE_ENDIAN);
        bin.copy(0, bin3, 100, 8);
        System.out.println(bin3.getDouble(100));
    }

    public static void testBigArrayBinary() {
        Binary bin = Binary.alloc(10086, ByteOrderProcessor.LITTLE_ENDIAN);
        bin.setInt(55, 7777);
        System.out.println(bin.getInt(55));
        System.out.println(bin.getFloat(55));
        System.out.println(Float.floatToRawIntBits(bin.getFloat(55)));
        bin.setDouble(0, 3.1415926);
        System.out.println(bin.getDouble(0));
        System.out.println(bin.getLong(0));

        bin.setLong(666, 12306);
        System.out.println(bin.getLong(666));
        bin.setFloat(666, 6.66f);
        System.out.println(bin.getFloat(666));

        System.out.println(bin.getBinary(0, 8).getDouble(0));
        System.out.println(bin.subBinary(0, 8).getDouble(0));
        Binary bin2 = Binary.alloc(10086, ByteOrderProcessor.LITTLE_ENDIAN);
        bin.copy(0, bin2, 100, 8);
        System.out.println(bin2.getDouble(100));

        Binary bin3 = Binary.wrap(new byte[10086], 0, 10086, ByteOrderProcessor.LITTLE_ENDIAN);
        bin.copy(0, bin3, 100, 8);
        System.out.println(bin3.getDouble(100));
    }

    public static void testOutputer() throws EOFException, IOException {
        Outputer out = new QuickOutputer("oooo.txt");
        for (int i = 0; i < 10086; i++) {
            out.write((Integer.toString(i) + "\r\n").getBytes("UTF-8"));
        }
        out.close();
    }

    public static void testInputer() throws EOFException, IOException {
        Inputer in = new QuickInputer("oooo.txt");
        // while (!in.end()) {
        // Binary bin = in.readBinary(10086);
        // for (long i = 0; i < bin.length(); i++){
        // System.out.print(bin.getASCII(i));
        // }
        // }
        Binary bin = Binary.alloc(10086);
        in.read(bin);
        System.out.println(bin.getASCII(555));
        System.out.println(bin.getASCII(556));
        System.out.println(bin.getASCII(557));
        System.out.println(bin.getASCII(558));
        System.out.println(bin.getASCII(559));
        System.out.println(bin.getASCII(560));
        System.out.println(bin.getASCII(561));
        System.out.println(bin.getASCII(562));
        in.close();
    }

    public static void testFileBinary() throws NullPointerException, IllegalArgumentException, IOException {
        FileBinary fb = FileBinary.open("fileBinaryTest.txt", "rws");
        Outputer out = fb.fileOutputer(10086);
        out.writeASCII('h');
        out.flush();
        out.close();
        fb.setASCII(22, 't');
        System.out.println(fb.getASCII(22));
        Inputer in = fb.fileInputer(10086);
        System.out.println(in.readHex());
        Inputer in2 = fb.fileInputer(100860);
        System.out.println(in2.readASCII());
        fb.close();
    }

    public static void testMappedFileBinary() throws NullPointerException, IllegalArgumentException, IOException {
        FileBinary fb = FileBinary.map("tt.txt", MapMode.READ_WRITE);
        // FileBinary fb = FileBinary.open("tt.txt", "rw");
        System.out.println(fb.length());
        for (long i = 0; i < fb.length(); i++) {
            System.out.print(fb.getASCII(i));
        }
        // System.out.println(fb.getHex(79667));
    }

    public static void testRandomaAndMapped() throws NullPointerException, IOException {
        String rdFileName = "test_rd.txt";
        File rdFile = new File(rdFileName);
//        if (rdFile.exists()) {
//            rdFile.delete();
//        }
        FileBinary rd = FileBinary.open(rdFile, "rw");
//        Outputer outRd = rd.fileOutputer(0);
//        for (long i = 0; i < 10000; i++) {
//            outRd.write(Binary.wrap("This is a test file!\r\n".getBytes()));
//        }
//        outRd.close();
        Binary br = Binary.wrap(new byte[(int)rdFile.length()]);
        rd.copy(0, br, 0, rd.length());
        Quicker.clockMillis();
        for (long i = 0; i < rdFile.length(); i++) {
            br.getASCII(i);
            // System.out.print(rd.getASCII(i));
        }
        System.out.println("Random read: " + Quicker.clockMillis());
        Quicker.clockMillis();
        for (long i = 0; i < rdFile.length(); i++) {
            br.setASCII(i, 'R');
            // System.out.print(rd.getASCII(i));
        }
        System.out.println("Random write: " + Quicker.clockMillis());

        String mpFileName = "test_mp.txt";
        File mpFile = new File(mpFileName);
//        if (mpFile.exists()) {
//            mpFile.delete();
//        }
        FileBinary mp = FileBinary.map(mpFile, MapMode.READ_WRITE);
//        Outputer outMp = mp.fileOutputer(0);
//        for (long i = 0; i < 10000; i++) {
//            outMp.write(Binary.wrap("This is a test file!\r\n".getBytes()));
//        }
//        outMp.close();
        mp.copy(0, br, 0, rd.length());
        Quicker.clockMillis();
        for (long i = 0; i < mpFile.length(); i++) {
            br.getASCII(i);
            // System.out.print(rd.getASCII(i));
        }
        System.out.println("Mapped read: " + Quicker.clockMillis());
        Quicker.clockMillis();
        for (long i = 0; i < mpFile.length(); i++) {
            br.setASCII(i, 'R');
            // System.out.print(rd.getASCII(i));
        }
        System.out.println("Mapped write: " + Quicker.clockMillis());

        byte[] array = new byte[(int)mpFile.length()];
        long count = 0;
        Quicker.clockMillis();
        for (long i = 0; i < array.length; i++) {
            char c = (char)array[(int)i];
            count += c;
            // System.out.print(rd.getASCII(i));
        }
        System.out.println("Array read: " + Quicker.clockMillis());
        Quicker.clockMillis();
        for (long i = 0; i < array.length; i++) {
            array[(int)i] = 'R';
            count += array[(int)i];
            // System.out.print(rd.getASCII(i));
        }
        System.out.println("Array write: " + Quicker.clockMillis());
        System.out.println(count);
    }

    public static void testByteQueue() {
        ByteQueue bq = new ByteQueue();
        bq.addLast((byte)5);
        bq.addLast((byte)5);
        bq.addLast((byte)2);
        bq.addLast((byte)0);
        bq.addLast((byte)0);
        bq.addLast((byte)0);
        bq.addLast((byte)0);
        bq.addLast((byte)0);
        bq.getLast();
        bq.pollLast();
        bq.pollFirst();
        System.out.println(QuickToStrings.toString(bq.toArray()));
    }
}
