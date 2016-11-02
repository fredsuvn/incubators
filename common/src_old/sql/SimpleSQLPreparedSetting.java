package com.cogician.quicker.sql;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import org.suvn.common.DataBlock;
import org.suvn.common.OutOfBoundsException;

/**
 * Prepared setting for simple SQL.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-26 15:32:10
 */
class SimpleSQLPreparedSetting
{
    /**
     * Statement for setting precompiled SQL.
     */
    private PreparedStatement ps;

    /**
     * Statement for setting precompiled procedure.
     */
    private CallableStatement cs;

    /**
     * COnstructs with a statement for setting precompiled SQL.
     *
     * @param ps
     *            precompiled statement, not null
     * @throws NullPointerException
     *             if given statement is null
     */
    SimpleSQLPreparedSetting(final PreparedStatement ps)
    {
        this.ps = Objects.requireNonNull(ps);
    }

    /**
     * COnstructs with a statement for setting precompiled procedure.
     *
     * @param cs
     *            precompiled statement, not null
     * @throws NullPointerException
     *             if given statement is null
     */
    SimpleSQLPreparedSetting(final CallableStatement cs)
    {
        this.cs = cs;
    }

    private static class PreparedStatement

    /**
     * Set object value. The type of the Java object will be the default Java object type
     * corresponding to the column's SQL type.
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
    public void setBinaryBytes(int index, byte[] value, int startIndex, int length);

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
    public void setBinaryData(int index, DataBlock value);

    /**
     * Register output parameter of procedure.
     *
     * @param index
     *            index of designated parameter, start from 1
     * @param sqlType
     *            database type, see {@linkplain java.sql.Types}
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public void registerOutParameter(int index, int sqlType);
}
