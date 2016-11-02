package com.cogician.quicker;

/**
 * Represents a switcher like switch case statement, for example:
 *
 * <pre>
 * Codes:
 * 
 * Switcher<Integer> s = new SwitcherImple<Integer>();
 * s.add(5, (i)->{System.out.println(i)}, true);
 * s.add(null, (i)->{System.out.println("default!")}, true);
 * s.test(5);
 * 
 * are equal to:
 * 
 * int i = 5;
 * switch(i)
 * {
 *     case 5:
 *     {
 *         System.out.println(i);
 *         break;
 *     }
 *     default:
 *     {
 *         System.out.println("default!");
 *         break;
 *     }
 * }
 * </pre>
 *
 * This interface enhances the switch case statement.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-26 18:02:41
 */
public interface Linked<N> extends IndexedSubable<Linked<N>>, Ordered {
    public N first();

    public N last();

    public long length();

    public N curent();

    public long currentIndex();

    public boolean hasNext();

    public N next();

    public N get(long index);

    public void add(N node);

    public void insertBefore(long index, N node);

    public void insertAfter(long index, N node);

    public Linked<N> subLinked(int startIndex, int endIndex);

    public N previous();
}
