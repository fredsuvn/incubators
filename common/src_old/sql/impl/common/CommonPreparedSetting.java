package com.cogician.quicker.sql.impl.common;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.cogician.quicker.Bitsss;
import com.cogician.quicker.Checker;
import com.cogician.quicker.OutOfBoundsException;
import com.cogician.quicker.sql.SimpleSQLException;

/**
 * To set parameters of sql or procedure, implementation of
 * {@linkplain SimpleSQLPreparedSetting}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-01 16:10:07
 */
class CommonPreparedSetting implements SimpleSQLPreparedSetting {
    /**
     * Whether this instance is valid.
     */
    private boolean isValid = true;

    /**
     * Parameters.
     */
    private Map<Integer, Object> params = null;

    /**
     * Length addition of input stream parameters.
     */
    private Map<Integer, Long> inputStreamParamsLengths = null;

    /**
     * Registered parameters.
     */
    private Map<Integer, Integer> registerParams = null;

    /**
     * Gets parameters content of this instance.
     *
     * @return parameters content of this instance
     */
    private Map<Integer, Object> getParamsMap() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    /**
     * Gets length info of input stream parameters content of this instance.
     *
     * @return length info of input stream parameters content of this instance
     */
    private Map<Integer, Long> getLengthOfInputStreamMap() {
        if (inputStreamParamsLengths == null) {
            inputStreamParamsLengths = new HashMap<>();
        }
        return inputStreamParamsLengths;
    }

    /**
     * Gets registered parameters.
     *
     * @return registered parameters
     */
    private Map<Integer, Integer> getRegisterParamsMap() {
        if (registerParams == null) {
            registerParams = new HashMap<>();
        }
        return registerParams;
    }

    /**
     * Returns output parameters.
     *
     * @return output parameters
     */
    protected Optional<Map<Integer, Integer>> getOutputParameters() {
        return Optional.ofNullable(registerParams);
    }

    /**
     * Checks whether this instance is valid.
     *
     * @throws SimpleSQLException
     *             if invalid
     */
    protected void checkValid() {
        if (!isValid) {
            throw new SimpleSQLException("This simple sql is invalid!");
        }
    }

    /**
     * Set simple sql invalid.
     */
    protected void invalid() {
        isValid = false;
    }

    /**
     * Returns whether this sql is valid. The simple SQL will be invalid after
     * doing final method.
     *
     * @return whether this sql is valid
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setObject(final int index, final Object value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        getParamsMap().put(index, value);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setInt(final int index, final int value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        getParamsMap().put(index, value);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setLong(final int index, final long value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        getParamsMap().put(index, value);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setBigDecimal(final int index, final BigDecimal value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        getParamsMap().put(index, value);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setString(final int index, final String value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        getParamsMap().put(index, value);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setInstant(final int index, final Instant value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        if (value == null) {
            getParamsMap().put(index, value);
        } else {
            getParamsMap().put(index, new java.sql.Date(value.toEpochMilli()));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setLocalDateTime(final int index, final LocalDateTime value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        if (value == null) {
            getParamsMap().put(index, value);
        } else {
            getParamsMap().put(
                    index,
                    new java.sql.Timestamp(value.toInstant(ZoneOffset.UTC)
                            .toEpochMilli()));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setBinaryStream(final int index, final InputStream value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        getParamsMap().put(index, value);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws NullPointerException
     *             if binary is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setBinaryStream(final int index, final InputStream value,
            final long length) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        Checker.checkLengthPositive(length);
        getParamsMap().put(index, Objects.requireNonNull(value));
        getLengthOfInputStreamMap().put(index, length);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setBinaryBytes(final int index, final byte[] value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        getParamsMap().put(index, value);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws NullPointerException
     *             if binary is null
     * @throws OutOfBoundsException
     *             if out of bounds
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setBinaryBytes(final int index, final byte[] value,
            final int startIndex, final int length) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        Checker.checkIndex(startIndex);
        Checker.checkLengthPositive(length);
        Checker.checkInArrayBounds(startIndex, length, value.length);
        getParamsMap().put(index,
                Arrays.copyOfRange(value, startIndex, startIndex + length));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void setBinaryData(final int index, final Bitsss value) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        if (value == null) {
            getParamsMap().put(index, value);
        } else {
            if (value.lengthInBits() % Byte.SIZE != 0) {
                throw new IllegalArgumentException(
                        "Data block's length should be multiple of bytes!");
            }
            getParamsMap().put(index, value.toByteArray());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if exist illegal parameter
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public void registerOutParameter(final int index, final int sqlType) {
        checkValid();
        Checker.checkPositive(index, "Index should be positive");
        getRegisterParamsMap().put(index, sqlType);
    }

    /**
     * Create prepared statement with special connection and parameters.
     *
     * @param conn
     *            special connection, not null
     * @param sql
     *            sql, not null
     * @return prepared statement with special connection and parameters, not
     *         null
     * @throws NullPointerException
     *             if connection or sql is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public PreparedStatement preparedStatement(final Connection conn,
            final String sql) {
        return preparedStatement(conn, sql, true, 0, 0);
    }

    /**
     * Create prepared statement with special connection, parameters, result set
     * type and result set concurrency.
     * <p>
     * The result set type set include: {@linkplain ResultSet#TYPE_FORWARD_ONLY}, {@linkplain ResultSet#TYPE_SCROLL_INSENSITIVE} and
     * {@linkplain ResultSet#TYPE_SCROLL_SENSITIVE}.
     * <p>
     * The result set concurrency include:
     * {@linkplain ResultSet#CONCUR_READ_ONLY} and
     * {@linkplain ResultSet#CONCUR_UPDATABLE}.
     *
     * @param conn
     *            special connection, not null
     * @param sql
     *            sql, not null
     * @param resultSetType
     *            result set type in bounds
     * @param resultSetConcurrency
     *            result set concurrency in bounds
     * @return prepared statement with special connection and parameters, not
     *         null
     * @throws IllegalArgumentException
     *             if result set type or concurrency is illegal
     * @throws NullPointerException
     *             if connection or sql is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public PreparedStatement preparedStatement(final Connection conn,
            final String sql, final int resultSetType,
            final int resultSetConcurrency) {
        return preparedStatement(conn, sql, false, resultSetType,
                resultSetConcurrency);
    }

    /**
     * Create prepared statement with special connection, parameters, result set
     * type and result set concurrency, or used default result type and
     * concurrency.
     * <p>
     * The result set type set include: {@linkplain ResultSet#TYPE_FORWARD_ONLY}, {@linkplain ResultSet#TYPE_SCROLL_INSENSITIVE} and
     * {@linkplain ResultSet#TYPE_SCROLL_SENSITIVE}.
     * <p>
     * The result set concurrency include:
     * {@linkplain ResultSet#CONCUR_READ_ONLY} and
     * {@linkplain ResultSet#CONCUR_UPDATABLE}.
     *
     * @param conn
     *            special connection, not null
     * @param sql
     *            sql, not null
     * @param useDefault
     *            whether used default result type and concurrency
     * @param resultSetType
     *            result set type in bounds
     * @param resultSetConcurrency
     *            result set concurrency in bounds
     * @return prepared statement with special connection and parameters, not
     *         null
     * @throws IllegalArgumentException
     *             if result set type or concurrency is illegal
     * @throws NullPointerException
     *             if connection or sql is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    private PreparedStatement preparedStatement(Connection conn, String sql,
            final boolean useDefault, final int resultSetType,
            final int resultSetConcurrency) {
        conn = Objects.requireNonNull(conn);
        sql = Objects.requireNonNull(sql);
        PreparedStatement ps;
        try {
            if (useDefault) {
                ps = conn.prepareStatement(sql);
            } else {
                if ((resultSetType != ResultSet.TYPE_FORWARD_ONLY
                        && resultSetType != ResultSet.TYPE_SCROLL_INSENSITIVE && resultSetType != ResultSet.TYPE_SCROLL_SENSITIVE)
                        || (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY && resultSetConcurrency != ResultSet.CONCUR_UPDATABLE)) {
                    throw new IllegalArgumentException(
                            "Result set type or concurrency is illegal!");
                }
                ps = conn.prepareStatement(sql, resultSetType,
                        resultSetConcurrency);
            }
        } catch (final SQLException e0) {
            throw new SimpleSQLException(e0);
        }
        if (params == null) {
            return ps;
        }
        params.forEach((k, v) -> {
            try {
                final Object len = inputStreamParamsLengths == null ? null
                        : inputStreamParamsLengths.get(k);
                if (len != null) {
                    final long l = (Long) len;
                    ps.setBinaryStream(k, (InputStream) v, l);
                } else {
                    ps.setObject(k, v);
                }
            } catch (final Exception e) {
                throw new SimpleSQLException(e);
            }
        });
        return ps;
    }

    /**
     * Create callable statement with special connection and parameters.
     *
     * @param conn
     *            special connection, not null
     * @param sql
     *            sql, not null
     * @return callable statement with special connection and parameters, not
     *         null
     * @throws NullPointerException
     *             if connection or sql is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public CallableStatement callableStatement(final Connection conn,
            final String sql) {
        return callableStatement(conn, sql, true, 0, 0);
    }

    /**
     * Create callable statement with special connection, parameters, result set
     * type and result set concurrency.
     * <p>
     * The result set type set include: {@linkplain ResultSet#TYPE_FORWARD_ONLY}, {@linkplain ResultSet#TYPE_SCROLL_INSENSITIVE} and
     * {@linkplain ResultSet#TYPE_SCROLL_SENSITIVE}.
     * <p>
     * The result set concurrency include:
     * {@linkplain ResultSet#CONCUR_READ_ONLY} and
     * {@linkplain ResultSet#CONCUR_UPDATABLE}.
     *
     * @param conn
     *            special connection, not null
     * @param sql
     *            sql, not null
     * @param resultSetType
     *            result set type in bounds
     * @param resultSetConcurrency
     *            result set concurrency in bounds
     * @return callable statement with special connection and parameters, not
     *         null
     * @throws IllegalArgumentException
     *             if result set type or concurrency is illegal
     * @throws NullPointerException
     *             if connection or sql is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public CallableStatement callableStatement(final Connection conn,
            final String sql, final int resultSetType,
            final int resultSetConcurrency) {
        return callableStatement(conn, sql, false, resultSetType,
                resultSetConcurrency);
    }

    /**
     * Create callable statement with special connection, parameters, result set
     * type and result set concurrency, or used default result type and
     * concurrency.
     * <p>
     * The result set type set include: {@linkplain ResultSet#TYPE_FORWARD_ONLY}, {@linkplain ResultSet#TYPE_SCROLL_INSENSITIVE} and
     * {@linkplain ResultSet#TYPE_SCROLL_SENSITIVE}.
     * <p>
     * The result set concurrency include:
     * {@linkplain ResultSet#CONCUR_READ_ONLY} and
     * {@linkplain ResultSet#CONCUR_UPDATABLE}.
     *
     * @param conn
     *            special connection, not null
     * @param sql
     *            sql, not null
     * @param useDefault
     *            whether used default result type and concurrency
     * @param resultSetType
     *            result set type in bounds
     * @param resultSetConcurrency
     *            result set concurrency in bounds
     * @return callable statement with special connection and parameters, not
     *         null
     * @throws IllegalArgumentException
     *             if result set type or concurrency is illegal
     * @throws NullPointerException
     *             if connection or sql is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    private CallableStatement callableStatement(Connection conn, String sql,
            final boolean useDefault, final int resultSetType,
            final int resultSetConcurrency) {
        conn = Objects.requireNonNull(conn);
        sql = Objects.requireNonNull(sql);
        CallableStatement cs;
        try {
            if (useDefault) {
                cs = conn.prepareCall(sql);
            } else {
                if ((resultSetType != ResultSet.TYPE_FORWARD_ONLY
                        && resultSetType != ResultSet.TYPE_SCROLL_INSENSITIVE && resultSetType != ResultSet.TYPE_SCROLL_SENSITIVE)
                        || (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY && resultSetConcurrency != ResultSet.CONCUR_UPDATABLE)) {
                    throw new IllegalArgumentException(
                            "Result set type or concurrency is illegal!");
                }
                cs = conn.prepareCall(sql, resultSetType, resultSetConcurrency);
            }
        } catch (final SQLException e0) {
            throw new SimpleSQLException(e0);
        }
        if (params == null) {
            return cs;
        }
        params.forEach((k, v) -> {
            try {
                final Object len = inputStreamParamsLengths == null ? null
                        : inputStreamParamsLengths.get(k);
                if (len != null) {
                    final long l = (Long) len;
                    cs.setBinaryStream(k, (InputStream) v, l);
                } else {
                    cs.setObject(k, v);
                }
            } catch (final Exception e) {
                throw new SimpleSQLException(e);
            }
        });
        if (registerParams == null) {
            return cs;
        }
        registerParams.forEach((k, v) -> {
            try {
                cs.registerOutParameter(k, v);
            } catch (final Exception e) {
                throw new SimpleSQLException(e);
            }
        });
        return cs;
    }
}
