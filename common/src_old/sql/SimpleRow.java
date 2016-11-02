package com.cogician.quicker.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.cogician.quicker.Bitsss;
import com.cogician.quicker.Checker;
import com.cogician.quicker.InputStreamAdapter;
import com.cogician.quicker.MemAllocator;
import com.cogician.quicker.ReaderAdapter;

/**
 * Represents a row of a result set stream. The row is one-off, it will be
 * invalid when next row was read or related result set is closed.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-26 16:10:03
 * @since 0.0.0
 */
public class SimpleRow {
    /**
     * Result set, the row info will be read from this result set, not null.
     */
    private final ResultSet result;

    /**
     * Buffered when {@linkplain #toMap()} is called.
     */
    private Map<String, Object> map = null;

    /**
     * Constructs with special result set, the row info will be read from
     * special result set.
     *
     * @param result
     *            special result set, not null
     * @throws NullPointerException
     *             if special result set is null
     */
    SimpleRow(final ResultSet result) {
        this.result = Objects.requireNonNull(result);
    }

    /**
     * Clear buffered of map.
     */
    void clearBuffered() {
        this.map = null;
    }

    /**
     * Returns result set of this row, the row info will be read from returned
     * result set, so if the returned set is closed, this row will be invalid.
     *
     * @return result set of this row， not null
     */
    public ResultSet getResultSet() {
        return result;
    }

    /**
     * Returns row number.
     *
     * @return row number
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public int getRowNumber() {
        try {
            return result.getRow();
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets object value of special column label. The type of the Java object
     * will be the default Java object type corresponding to the column's SQL
     * type.
     *
     * @param label
     *            special column label, not null
     * @return optional object value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Object> getObject(final String label) {
        try {
            return Optional.ofNullable(result.getObject(Objects
                    .requireNonNull(label)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets int value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional int value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Integer> getInt(final String label) {
        try {
            return Optional.ofNullable(result.getInt(Objects
                    .requireNonNull(label)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets float value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional float value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Float> getFloat(final String label) {
        try {
            return Optional.ofNullable(result.getFloat(Objects
                    .requireNonNull(label)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets long value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional long value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Long> getLong(final String label) {
        try {
            return Optional.ofNullable(result.getLong(Objects
                    .requireNonNull(label)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets double value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional double value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Double> getDouble(final String label) {
        try {
            return Optional.ofNullable(result.getDouble(Objects
                    .requireNonNull(label)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets big decimal value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional big decimal value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<BigDecimal> getBigDecimal(final String label) {
        try {
            return Optional.ofNullable(result.getBigDecimal(Objects
                    .requireNonNull(label)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets string value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional string value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<String> getString(final String label) {
        try {
            return Optional.ofNullable(result.getString(Objects
                    .requireNonNull(label)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets instant value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional instant value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Instant> getInstant(final String label) {
        try {
            final Date date = result.getDate(Objects.requireNonNull(label));
            return date == null ? Optional.empty() : Optional.ofNullable(date
                    .toInstant());
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets date time value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional date time value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<LocalDateTime> getDateTime(final String label) {
        try {
            final Timestamp timestamp = result.getTimestamp(Objects
                    .requireNonNull(label));
            return timestamp == null ? Optional.empty() : Optional
                    .ofNullable(timestamp.toLocalDateTime());
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets binary stream value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional binary stream value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<InputStreamAdapter> getBinaryStream(final String label) {
        try {
            final InputStream in = result.getBinaryStream(Objects
                    .requireNonNull(label));
            return in == null ? Optional.empty() : Optional
                    .ofNullable(new InputStreamAdapter(in));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets character stream value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional character stream value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<ReaderAdapter> getCharacterStream(final String label) {
        try {
            final Reader in = result.getCharacterStream(Objects
                    .requireNonNull(label));
            return in == null ? Optional.empty() : Optional
                    .ofNullable(new ReaderAdapter(in));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets binary bytes value of special column label name.
     *
     * @param label
     *            special column label name, not null
     * @return optional binary bytes value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<byte[]> getBytes(final String label) {
        try {
            return Optional.ofNullable(result.getBytes(Objects
                    .requireNonNull(label)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets binary value of special column label name as data block.
     *
     * @param label
     *            special column label name, not null
     * @return optional binary value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Bitsss> getBinaryData(final String label) {
        try {
            final byte[] bytes = result.getBytes(Objects.requireNonNull(label));
            return bytes == null ? Optional.empty() : Optional
                    .ofNullable(MemAllocator.wrap(bytes));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates object value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special object value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateObject(final String label, final Object value) {
        try {
            result.updateObject(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates int value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special int value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateInt(final String label, final int value) {
        try {
            result.updateInt(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates float value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special float value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateFloat(final String label, final float value) {
        try {
            result.updateFloat(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates long value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special long value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateLong(final String label, final long value) {
        try {
            result.updateLong(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates double value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special double value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateDouble(final String label, final double value) {
        try {
            result.updateDouble(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates big decimal value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special big decimal value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateBigDecimal(final String label, final BigDecimal value) {
        try {
            result.updateBigDecimal(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates string value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special string value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateString(final String label, final String value) {
        try {
            result.updateString(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates instant value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special instant value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateInstant(final String label, final Instant value) {
        try {
            if (null == value) {
                result.updateDate(Objects.requireNonNull(label), null);
            } else {
                result.updateDate(Objects.requireNonNull(label),
                        new Date(value.toEpochMilli()));
            }
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates date time value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special date time value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateDateTime(final String label, final LocalDateTime value) {
        try {
            if (null == value) {
                result.updateTimestamp(Objects.requireNonNull(label), null);
            } else {
                result.updateTimestamp(Objects.requireNonNull(label),
                        new Timestamp(value.toInstant(ZoneOffset.UTC)
                                .toEpochMilli()));
            }
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates binary stream value of special label if this row is updatable,
     * see {@linkplain Connection#prepareStatement(String, int, int)}. The
     * updated data may be buffered but underline database will be actually
     * updated after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special binary stream value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateBinaryStream(final String label, final InputStream value) {
        try {
            result.updateBinaryStream(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates binary stream value of special label with special length of bytes
     * if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special binary stream value
     * @param length
     *            special length in bytes, positive
     * @throws NullPointerException
     *             if special column label name is null
     * @throws IllegalArgumentException
     *             if special length of bytes is not positive
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateBinaryStream(final String label, final InputStream value,
            final long length) {
        try {
            Checker.checkLengthPositive(length);
            result.updateBinaryStream(Objects.requireNonNull(label), value,
                    length);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates character stream value of special label if this row is updatable,
     * see {@linkplain Connection#prepareStatement(String, int, int)}. The
     * updated data may be buffered but underline database will be actually
     * updated after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special character stream value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateCharacterStream(final String label, final Reader value) {
        try {
            result.updateCharacterStream(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates character stream value of special label with special length, if
     * this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special character stream value
     * @param length
     *            special length, positive
     * @throws NullPointerException
     *             if special column label name is null
     * @throws IllegalArgumentException
     *             if special length of bytes is not positive
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateCharacterStream(final String label, final Reader value,
            final long length) {
        try {
            Checker.checkLengthPositive(length);
            result.updateCharacterStream(Objects.requireNonNull(label), value,
                    length);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates binary bytes value of special label if this row is updatable, see
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special binary bytes value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateBytes(final String label, final byte[] value) {
        try {
            result.updateBytes(Objects.requireNonNull(label), value);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates data block value of special label if this row is updatable, if
     * length of data block is not multiple of bytes, an
     * {@linkplain SimpleSQLException} will be thrown. See
     * {@linkplain Connection#prepareStatement(String, int, int)}. The updated
     * data may be buffered but underline database will be actually updated
     * after {@linkplain #updateRow()} called.
     *
     * @param label
     *            special column label, not null
     * @param value
     *            special data block value
     * @throws NullPointerException
     *             if special column label name is null
     * @throws IllegalArgumentException
     *             if length of data block is not mutiple of bytes
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateBinaryData(final String label, final Bitsss value) {
        try {
            if (value == null) {
                result.updateBytes(Objects.requireNonNull(label), null);
            } else {
                Checker.checkLengthIsMutipleOfByte(value.lengthInBits());
                result.updateBytes(Objects.requireNonNull(label),
                        value.toByteArray());
            }
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Updates the underlying database with the new contents of the current row.
     * See {@linkplain ResultSet#updateRow()}.
     *
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public void updateRow() {
        try {
            result.updateRow();
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Convert this row to a map, keys are column labels, corresponding column
     * values are values. The values's types are the type of the Java object
     * will be the default Java object type corresponding to the column's SQL
     * type.
     *
     * @return a map, not null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Map<String, Object> toMap() {
        if (map != null) {
            return map;
        }
        map = new HashMap<>();
        try {
            final ResultSetMetaData meta = result.getMetaData();
            final int count = meta.getColumnCount();
            for (int i = 0; i < count; i++) {
                final String label = meta.getColumnLabel(i);
                final Object obj = result.getObject(label);
                map.put(label, obj);
            }
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
        return map;
    }
}
