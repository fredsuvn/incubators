/*
 * File: Subable.java
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-01-29T14:25:46+08:00
 * @since 0.0.0, 2016-01-29T14:25:46+08:00
 */

package com.cogician.quicker;

/**
 * <p>
 * The root interface of sub-able types. The sub-able type is a type that can be sub-divided into sub-portions and each
 * portion has same declared type as its parent's such as substring of string, sublist of list. This interface has no
 * methods or fields, only identifying the semantics of being sub-able, so that subtypes should provide detail methods.
 * </p>
 * <p>
 * Generally, subtypes of this interface commonly provides followed kinds of methods:
 * </p>
 * <p>
 * <ul>
 * <li><b>View Methods:</b> Returns a view of sub-portion of current instance. Returned view is backed by current
 * instance, that is, any operation of content of the view (such as adding, deleting, modifying...) will synchronize the
 * current instance, and vice-versa.</li>
 * <li><b>Shallow Sub-portion Methods:</b> Returns a shallow-copy of sub-portion of current instance. Returned object is
 * independent of current instance but the content is shallow-copied, that is, the non-structural change of content will
 * synchronize each other but structural modifications (such as adding or deleting new portion) is ineffective.</li>
 * <li><b>Deep Sub-portion Methods:</b> Returns a deep-copy of sub-portion of current instance. Returned object cut off
 * the relation to current instance.</li>
 * </ul>
 * </p>
 * <p>
 * Some subtypes cannot adding or deleting portion. For these methods, portion view methods and shallow sub-portion
 * methods may merge into one type of methods.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-01-29T14:25:46+08:00
 * @since 0.0.0, 2016-01-29T14:25:46+08:00
 */
public interface Subable {

}
