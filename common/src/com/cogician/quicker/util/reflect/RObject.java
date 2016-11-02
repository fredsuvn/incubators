package com.cogician.quicker.util.reflect;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.cogician.quicker.util.Consts;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-05T20:52:20+08:00
 * @since 0.0.0, 2016-08-05T20:52:20+08:00
 */
public class RObject implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private Object object;

    public Constructor<?> getConstructor() {
        return getConstructor(Consts.emptyClassArray());
    }

    public Constructor<?> getConstructor(Class<?>... parameters) {
        return ReflectionQuicker.getDeclaredConstructor(object.getClass(), parameters);
    }

    public Field getField(String fieldName) {
        return ReflectionQuicker.getDeclaredField(object.getClass(), fieldName);
    }

    public Method getMethod(String methodName) {
        return getMethod(methodName, Consts.emptyClassArray());
    }

    public Method getMethod(String methodName, Class<?>... parameters) {
        return ReflectionQuicker.getDeclaredMethod(object.getClass(), methodName, parameters);
    }

    public Object get(String fieldName) throws ReflectionException {
        return ReflectionQuicker.getFieldValue(object, fieldName);
    }

    public void set(String fieldName, Object value) throws ReflectionException {
        Field f = getField(fieldName);
        if (null == f) {
            throw new ReflectionException("Field not found: " + fieldName + ".");
        }
        try {
            f.set(object, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }
}
