package com.cogician.quicker.sql;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import com.cogician.quicker.Bitsss;
import com.cogician.quicker.OutOfBoundsException;

/**
 * Represents a SQL will be executed. The SQL is prepared like
 *
 * <pre>
 * select t.* from table t where t.id = ? and t.name = ?
 * </pre>
 *
 * The "?" is a index number start from 1, will be set in setXxx(int, Object)
 * method. Or like:
 *
 * <pre>
 * select t.* from table t where t.id = ?id and t.name = ?name
 * </pre>
 *
 * The string after "?" is parameter, use setXxx(String, Object) to setting.
 * <p>
 * It has a shortcut if condition substrings are too more to manipulation. For
 * example:
 *
 * <pre>
 * select t.* from table t where 1 = 1 [and t.id = ?] [and t.name = ?]
 * </pre>
 *
 * or
 *
 * <pre>
 * select t.* from table t where 1 = 1 [and t.id = ?id] [and t.name = ?name]
 * </pre>
 * 
 * If the index or parameter in "[" and "]" are set, the condition strings will
 * be valid, else the condition strings invalid. "[" and "]" are default flag to
 * mark optional condition string
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-01 17:32:35
 */
public class SimpleSQL implements AutoCloseable {
    private final String DEFAULT_ALTERNATIVE_FLAG = "[";

    /**
     * Flag whether this sql is valid.
     */
    private boolean valid;

    /**
     *
     */
    StringBuilder sql = new StringBuilder();

    /**
     * Returns whether this sql is valid. The simple SQL will be invalid after
     * doing final method.
     *
     * @return whether this sql is valid
     */
    public boolean isValid();

    /**
     * Returns sql of this instance to modify or do other operation, like
     * "select * from tableA", or "select * from tableB where id = ?", or
     * "{call PROC(?, ?, ?)}".
     *
     * @return sql of this instance to modify or do other operation
     */
    public StringBuilder sql();

    /**
     * Set object value. The type of the Java object will be the default Java
     * object type corresponding to the column's SQL type.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            object value
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setObject(int index, Object value);

    /**
     * Set int value.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            int value
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setInt(int index, int value);

    /**
     * Set long value.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            long value
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setLong(int index, long value);

    /**
     * Set big decimal value.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            big decimal value
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setBigDecimal(int index, BigDecimal value);

    /**
     * Set string value.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            string value
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setString(int index, String value);

    /**
     * Set instant value.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            instant value
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setInstant(int index, Instant value);

    /**
     * Set date time value.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            date time value
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setLocalDateTime(int index, LocalDateTime value);

    /**
     * Set binary value.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            binary value
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setBinaryStream(int index, InputStream value);

    /**
     * Set binary stream.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            binary stream
     * @param length
     *            length in bytes, positive
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws NullPointerException
     *             if binary is null
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setBinaryStream(int index, InputStream value, long length);

    /**
     * Set binary bytes.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            binary bytes
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setBinaryBytes(int index, byte[] value);

    /**
     * Set binary bytes.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            binary bytes
     * @param startIndex
     *            start index of array in bounds
     * @param length
     *            length in bytes, positive
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws NullPointerException
     *             if binary is null
     * @throws OutOfBoundsException
     *             if out of bounds
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setBinaryBytes(int index, byte[] value, int startIndex,
            int length);

    /**
     * Set binary data value. The block's length should be multiple of bytes
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param value
     *            binary data value, length should be multiple of bytes
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void setBinaryData(int index, Bitsss value);

    /**
     * Close this sql and result set.
     *
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public void close();
}
