package com.cogician.quicker;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-04-19 14:46:13
 */
public class StartCraft {
    public static void main(final String[] args) throws Exception {
        // String driverName = "com.ibm.db2.jcc.DB2Driver";
        // String url = "jdbc:db2://66.3.33.128:50001/crm128";
        // String userName = "crm";
        // String password = "crm";
        // SimpleSQLConnection conn = SimpleSQLFactory.getConnection(driverName,
        // url, userName,
        // password);
        // conn.query()
        // .sql("select * from ADMIN_AUTH_ACCOUNT")
        // .queryAsStream()
        // .forEach(
        // (s) ->
        // {
        // System.out.println(s.toMap().toString());
        // });
        // ;

        // Class.forName("com.ibm.db2.jcc.DB2Driver");
        // Connection conn =
        // DriverManager.getConnection("jdbc:db2://66.3.33.128:50001/crm128",
        // "crm", "crm");
        // PreparedStatement ps =
        // conn.prepareStatement("select count(*) from OCRM_F_IMG where fr_id = ?");
        // ResultSet rs = ps.executeQuery();
        // if (rs.next())
        // {
        // System.out.println(rs.getString(1));
        // }
        // char c = (char)0xffff;
        // short s = (short)0xffff;
        // System.out.println((int)c);
        // System.out.println((int)s);
        // Pattern p = Pattern.compile("\\d");
        // Matcher m = p.matcher("333");
        // System.out.println(m.find());
        // LocalDateTime d = LocalDateTime.now();
        // LocalDateTime d2 = d.plusHours(1000000);
        // Instant i = Clock.systemDefaultZone().instant();
        // Date dt = new Date();
        // System.out.println(String.format("%1$tY-%2$5tm-%1$td", d, d2));
        // System.out.println(String.format("%+5e", -500.0));
        // System.out.println(new CommonRuntimeException("").fullMessage());
        // File f = new File("ttt.ccc");
        // if (!f.exists()) {
        // f.createNewFile();
        // } else {
        // f.delete();
        // f.createNewFile();
        // }
        // FileOutputStream fOut = new FileOutputStream(f);
        // fOut.write("abc\nabc2\nabc3\r\nabc4\r\nabc5\rabc6\rabc7\n\rabc8\n\rabc9".getBytes("UTF-8"));
        // fOut.flush();
        // fOut.close();
        // try (BufferedReader reader = new BufferedReader(new
        // InputStreamReader(
        // new FileInputStream("ttt.ccc")))) {
        // String line = null;
        // while((line = reader.readLine()) != null){
        // System.out.println(line);
        // }
        // }
        // System.out.println(StartCraft.class.getClassLoader());
        // System.out.println(System.class.getClassLoader());
        // System.out.println(String.class.getClassLoader());
//        System.out.println("Integer.MAX_VALUE * 1L * Integer.MAX_VALUE");
//        long l = Integer.MAX_VALUE * 1L * Integer.MAX_VALUE;
//        showBs(l);
//        System.out.println("-----------------");
//        System.out.println("Integer.MAX_VALUE * 1L * Integer.MAX_VALUE in byte:");
//        l = Integer.MAX_VALUE * 1L * Integer.MAX_VALUE;
//        showBsByte(l);
//        System.out.println("-----------------");
//        System.out.println("Long.MAX_VALUE");
//        showBs(Long.MAX_VALUE);
//        System.out.println("-----------------");
//        System.out.println("Long.MAX_VALUE in byte");
//        showBsByte(Long.MAX_VALUE);
//        System.out.println("-----------------");
//        System.out.println("Integer.MAX_VALUE");
//        showBs(Integer.MAX_VALUE);
//        System.out.println("-----------------");
//        System.out.println("Long.MAX_VALUE * Long.MAX_VALUE");
//        showBs(new BigInteger(Long.toString(Long.MAX_VALUE)).multiply(new BigInteger(Long.toString(Long.MAX_VALUE))));

        // ForEachPredicate<String, List<String>> strFn = (i, str, list)->{
        // if (str.equals("")){
        // list.size();
        // list.get(i);
        // }
        // return true;
        // };

        // ByteBuffer buffer = null;
        
        //Map<String, String> map = new HashMap<>();
        Map<String, String> map = new TreeMap<>();
        map.put("1", "qwer");
        map.put("2", "qwer");
        map.put("2", "qwerd");
        map.put("3", "qwer");
        map.put("4", "qwer");
        map.put("5", "qwer");
        for (Map.Entry<String, String> entry : map.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
            entry.setValue("aaa");
        }
        System.out.println("*****************");
        for (Map.Entry<String, String> entry : map.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void showBs(long l) {
        for (int i = 0; l > 0; i++) {
            switch (i) {
                case 0:
                    System.out.println(l + "Bit");
                    l /= 8;
                    break;
                case 1:
                    System.out.println(l + "B");
                    l /= 1024;
                    break;
                case 2:
                    System.out.println(l + "KB");
                    l /= 1024;
                    break;
                case 3:
                    System.out.println(l + "MB");
                    l /= 1024;
                    break;
                case 4:
                    System.out.println(l + "GB");
                    l /= 1024;
                    break;
                case 5:
                    System.out.println(l + "TB");
                    l /= 1024;
                    break;
                case 6:
                    System.out.println(l + "PB");
                    l /= 1024;
                    break;
                case 7:
                    System.out.println(l + "EB");
                    l /= 1024;
                    break;
                default:
                    System.out.println(l + "EB" + (i - 7));
                    l /= 1024;
                    break;
            }
        }
    }
    
    public static void showBsByte(long l) {
        for (int i = 1; l > 0; i++) {
            switch (i) {
                case 1:
                    System.out.println(l + "B");
                    l /= 1024;
                    break;
                case 2:
                    System.out.println(l + "KB");
                    l /= 1024;
                    break;
                case 3:
                    System.out.println(l + "MB");
                    l /= 1024;
                    break;
                case 4:
                    System.out.println(l + "GB");
                    l /= 1024;
                    break;
                case 5:
                    System.out.println(l + "TB");
                    l /= 1024;
                    break;
                case 6:
                    System.out.println(l + "PB");
                    l /= 1024;
                    break;
                case 7:
                    System.out.println(l + "EB");
                    l /= 1024;
                    break;
                default:
                    System.out.println(l + "EB" + (i - 7));
                    l /= 1024;
                    break;
            }
        }
    }

    public static void showBs(BigInteger l) {
        for (int i = 0; l.compareTo(BigInteger.ZERO) > 0; i++) {
            switch (i) {
                case 0:
                    System.out.println(l + "Bit");
                    l = l.divide(new BigInteger("8"));
                    break;
                case 1:
                    System.out.println(l + "B");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 2:
                    System.out.println(l + "KB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 3:
                    System.out.println(l + "MB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 4:
                    System.out.println(l + "GB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 5:
                    System.out.println(l + "TB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 6:
                    System.out.println(l + "PB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 7:
                    System.out.println(l + "EB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 8:
                    System.out.println(l + "ZB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 9:
                    System.out.println(l + "YB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                case 10:
                    System.out.println(l + "BB");
                    l = l.divide(new BigInteger("1024"));
                    break;
                default:
                    System.out.println(l + "BB" + (i - 10));
                    l = l.divide(new BigInteger("1024"));
                    break;
            }
        }
    }
}

// class MyLoader extends ClassLoader {
// @Override
// protected Class<?> findClass(final String name)
// throws ClassNotFoundException {
//
// }
// }
