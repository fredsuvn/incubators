package com.cogician.quicker.sql;

import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.cogician.quicker.Bitsss;
import com.cogician.quicker.InputStreamAdapter;
import com.cogician.quicker.MemAllocator;
import com.cogician.quicker.ReaderAdapter;

/**
 * A class for getting output parameters of procedure.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-13 14:05:29
 */
public class SimpleOutputParameters {
    /**
     * Statement for getting output parameters, not null.
     */
    private final CallableStatement cs;

    /**
     * Constructs with special statement for getting output parameters.
     *
     * @param cs
     *            special statement for getting output parameters, not null
     * @throws NullPointerException
     *             if special statement is null
     */
    SimpleOutputParameters(final CallableStatement cs) {
        this.cs = Objects.requireNonNull(cs);
    }

    /**
     * Returns statement of this instance, the data of getter methods come from
     * returned statement.
     *
     * @return statement of this instance, the data of getter methods come from
     *         returned statement
     */
    public CallableStatement getCallableStatement() {
        return cs;
    }

    /**
     * Gets object value of special parameter name. The type of the Java object
     * will be the default Java object type corresponding to the column's SQL
     * type.
     *
     * @param paramName
     *            special parameter name, not null
     * @return optional object value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Object> getObject(final String paramName) {
        try {
            return Optional.ofNullable(cs.getObject(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets int value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional int value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Integer> getInt(final String paramName) {
        try {
            return Optional.ofNullable(cs.getInt(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets float value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional float value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Float> getFloat(final String paramName) {
        try {
            return Optional.ofNullable(cs.getFloat(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets long value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional long value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Long> getLong(final String paramName) {
        try {
            return Optional.ofNullable(cs.getLong(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets double value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional double value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Double> getDouble(final String paramName) {
        try {
            return Optional.ofNullable(cs.getDouble(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets big decimal value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional big decimal value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<BigDecimal> getBigDecimal(final String paramName) {
        try {
            return Optional.ofNullable(cs.getBigDecimal(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets string value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional string value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<String> getString(final String paramName) {
        try {
            return Optional.ofNullable(cs.getString(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets instant value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional instant value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Instant> getInstant(final String paramName) {
        try {
            final Date date = cs.getDate(Objects.requireNonNull(paramName));
            return date == null ? Optional.empty() : Optional.ofNullable(date
                    .toInstant());
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets date time value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional date time value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<LocalDateTime> getDateTime(final String paramName) {
        try {
            final Timestamp timestamp = cs.getTimestamp(Objects
                    .requireNonNull(paramName));
            return timestamp == null ? Optional.empty() : Optional
                    .ofNullable(timestamp.toLocalDateTime());
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets binary stream value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional binary stream value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<InputStreamAdapter> getBinaryStream(final String paramName) {
        try {
            final Blob blob = cs.getBlob(Objects.requireNonNull(paramName));
            return blob == null ? Optional.empty() : Optional
                    .ofNullable(new InputStreamAdapter(blob.getBinaryStream()));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets character stream value of special parameter name name.
     *
     * @param label
     *            special parameter name name, not null
     * @return optional character stream value, not null
     * @throws NullPointerException
     *             if special column label name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<ReaderAdapter> getCharacterStream(final String label) {
        try {
            final Reader in = cs.getCharacterStream(Objects
                    .requireNonNull(label));
            return in == null ? Optional.empty() : Optional
                    .ofNullable(new ReaderAdapter(in));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets binary bytes value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional binary bytes value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<byte[]> getBytes(final String paramName) {
        try {
            return Optional.ofNullable(cs.getBytes(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets binary value of special parameter name name as data block.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional binary value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<Bitsss> getBinaryData(final String paramName) {
        try {
            final byte[] bytes = cs.getBytes(Objects.requireNonNull(paramName));
            return bytes == null ? Optional.empty() : Optional
                    .ofNullable(MemAllocator.wrap(bytes));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * Gets cursor value of special parameter name name.
     *
     * @param paramName
     *            special parameter name name, not null
     * @return optional cursor value, not null
     * @throws NullPointerException
     *             if special parameter name name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Optional<ResultSet> getCursor(final String paramName) {
        try {
            return Optional.ofNullable((ResultSet) cs.getObject(Objects
                    .requireNonNull(paramName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }
}
