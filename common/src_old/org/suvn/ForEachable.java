/*
 * File: ForEachable.java
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-08T14:06:35+08:00
 * @since 0.0.0, 2016-02-08T14:06:35+08:00
 */

package com.cogician.quicker;

import java.util.function.ObjIntConsumer;

/**<p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-08T14:06:35+08:00
 * @since 0.0.0, 2016-02-08T14:06:35+08:00
 */
public interface ForEachable<T, E> {
    
    public void forEach(T t, ObjIntConsumer<E> action);
}
