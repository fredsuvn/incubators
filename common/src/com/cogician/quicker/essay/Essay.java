package com.cogician.quicker.essay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.cogician.quicker.Quicker;
import com.cogician.quicker.util.ThreadQuicker;
import com.cogician.quicker.util.ToStringQuicker;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-30T20:59:35+08:00
 * @since 0.0.0, 2016-07-30T20:59:35+08:00
 */
public class Essay {

    public static final String JSR8 = "JSR8 (The Java Language Specification SE8)";

    public static final String SHOW_ANSWER = "The answer is: ";

    public static void main(String[] args) throws Exception {
        testMappedBuffer();
    }

    public static void ifNullEqualsNull() {
        System.out.println("To test if a null equals the other: return null == null");
        System.out.println(SHOW_ANSWER + (null == null));
    }

    public static void whatsIntMaxPlusIntMax() {
        System.out.println("What is Integer.MAX_VALUE + Integer.MAX_VALUE ?");
        System.out.println(SHOW_ANSWER + (Integer.MAX_VALUE + Integer.MAX_VALUE));
        System.out.println("What is Integer.MAX_VALUE + 1 ?");
        System.out.println(SHOW_ANSWER + (Integer.MAX_VALUE + 1));
    }

    public static void whatsAddMinus() {
        System.out.println("What is 100 + - 100 ?");
        System.out.println(SHOW_ANSWER + (100 + -100));
        System.out.println("What is 100 - - - 100 ?");
        System.out.println(SHOW_ANSWER + (100 - - -100));
        System.out.println("What is num---num ?");
        int num = 100;
        System.out.println(SHOW_ANSWER + (num-- - num));
        System.out.println("What is - - 100) ?");
        System.out.println(SHOW_ANSWER + (- -100));
        System.out.println("What is - + 100) ?");
        System.out.println(SHOW_ANSWER + (-+100));
    }

    public static void connectNet() throws Exception {
        Scanner sc = new Scanner(new URL("http://www.baidu.com").openStream());
        while (sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }
        sc.close();
    }

    public static void showPath() {
        File dir = new File("");
        System.out.println(dir.getAbsolutePath());
        System.out.println(Object.class.getResource("/com/cogician/quicker/config.properties").getPath());
        System.out.println(System.getProperty("user.dir"));
    }

    public static void showStack() {
        System.out.println(ToStringQuicker.toString(Thread.currentThread().getStackTrace()));
    }

    public static void testForeach() {
        byte[] bs = new byte[1024];
        for (byte b : bs) {
            System.out.println(b);
        }

        List<String> list = new ArrayList<>();
        list.add("aaa");
        for (String str : list) {
            System.out.println(str);
        }
    }

    public static void testTryCatch() {
        try {

        } catch (Throwable t) {
            ;
        }
        Random random = new Random();
        int times = Integer.MAX_VALUE / 10;

        Quicker.clockMillis();
        for (int i = 0; i < times; i++) {
            try {
                int j = random.nextInt();
                if (j == 0) {
                    j = 1;
                }
                if (j == 0) {
                    throw new RuntimeException();
                }
            } catch (Throwable t) {
                ;
            }
        }
        System.out.println(Quicker.clockMillis());

        Quicker.clockMillis();
        try {
            for (int i = 0; i < times; i++) {
                int j = random.nextInt();
                if (j == 0) {
                    j = 1;
                }
                if (j == 0) {
                    throw new RuntimeException();
                }
            }
        } catch (Throwable t) {
            ;
        }
        System.out.println(Quicker.clockMillis());

    }

    public static void testTCP() throws Exception {
        ServerSocket server = new ServerSocket(9999);
        System.out.println(server);
        while (true) {
            Socket socket = server.accept();
            System.out.println(socket);
            // OutputStream out = socket.getOutputStream();
            // PrintWriter pw = new PrintWriter(out, true);
            // pw.println("Welcome!");
            // pw.flush();
            // System.out.println("write");
            // out.write("Hello XiangQingyang!".getBytes("UTF-8"));
            byte[] bs = new byte[10240];
            InputStream in = socket.getInputStream();
            in.read(bs);
            System.out.println(new String(bs, "UTF-8"));
            // System.out.flush();
            // Quicker.log().info(new String(bs, "UTF-8"));
            socket.close();
        }
        // ServerSocket ss = new ServerSocket(9999);
        // while (true) {
        // Socket s = ss.accept();
        // OutputStream out = s.getOutputStream();
        // PrintWriter pw = new PrintWriter(out, true);
        // pw.println("println!");
        // s.close();
        // }
        // ss.close();
    }

    public static void testInputStream() throws Exception {
        InputStream in = new FileInputStream("quicker.log");
        byte[] bs = new byte[1024];
        int num = 0;
        while ((num = in.read(bs)) != -1) {
            System.out.println(num + ": " + new String(bs, "UTF-8"));
            Arrays.fill(bs, (byte)0);
        }
        System.out.println("end: " + in.read(bs));
        in.close();
    }

    public static void testRandomSync() throws Exception {
        File file = new File("oooo.txt");
        RandomAccessFile f = new RandomAccessFile("oooo.txt", "rw");
        System.out.println(f.readLine());
        RandomAccessFile f2 = new RandomAccessFile("oooo.txt", "rw");
        f2.writeByte('H');
        System.out.println("flie.length(): " + file.length());
        f2.getFD().sync();
        f.seek(0);
        System.out.println(f.readLine());
        System.out.println(f.length());
        f2.seek(f2.length());
        f2.write('E');
        System.out.println("flie.length(): " + file.length());
        System.out.println(f.length());
        f2.getFD().sync();
        System.out.println(f.length());
        ThreadQuicker.start(() -> {
            try {
                f2.seek(f2.length());
                for (int i = 0; i < 10086; i++) {
                    f2.write('T');
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        f.seek(f.length() - 5000);
        for (int i = 0; i < 10086; i++) {
            System.out.println((char)f.read());
        }
        System.out.println("flie.length(): " + file.length());
        f.close();
        f2.close();
    }
    
    public static void testMappedBuffer() throws IOException{
        File file  = new File("t_map.txt");
//        if (file.exists()){
//            file.delete();
//        }
        MappedByteBuffer map = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE).map(MapMode.READ_WRITE
                , 0, 100860);
        map.put(1, (byte)'c');
    }
}
