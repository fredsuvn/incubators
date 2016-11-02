package com.cogician.quicker.sql.impl.common;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.cogician.quicker.Bitsss;
import com.cogician.quicker.InputStreamAdapter;
import com.cogician.quicker.MemAllocator;
import com.cogician.quicker.sql.SimpleRow;
import com.cogician.quicker.sql.SimpleSQLException;

/**
 * Implementation of {@linkplain SimpleRow} to get output parameters of
 * procedure.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-02 08:34:13
 */
public class OutputParamsSimpleSQLRow implements SimpleRow {
    /**
     * Callable statement, never null.
     */
    private final CallableStatement cs;

    /**
     * Indexes of output parameters, never null.
     */
    private Map<Integer, Integer> outputParametersIndexs = null;

    /**
     * Constructs with special callable statement.
     *
     * @param cs
     *            special callable statement, not null
     * @param outputParametersIndexs
     *            index of output parameters, nullable
     * @throws NullPointerException
     *             if callable statement is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    OutputParamsSimpleSQLRow(final CallableStatement cs,
            final Map<Integer, Integer> outputParametersIndexs) {
        this.cs = cs;
        this.outputParametersIndexs = outputParametersIndexs;
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public int getRowNumber() {
        return 1;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Object> getObject(final String parameterName) {
        try {
            return Optional.ofNullable(cs.getObject(Objects
                    .requireNonNull(parameterName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Integer> getInt(final String parameterName) {
        try {
            return Optional.ofNullable(cs.getInt(Objects
                    .requireNonNull(parameterName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Long> getLong(final String parameterName) {
        try {
            return Optional.ofNullable(cs.getLong(Objects
                    .requireNonNull(parameterName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<BigDecimal> getBigDecimal(final String parameterName) {
        try {
            return Optional.ofNullable(cs.getBigDecimal(Objects
                    .requireNonNull(parameterName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<String> getString(final String parameterName) {
        try {
            return Optional.ofNullable(cs.getString(Objects
                    .requireNonNull(parameterName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Instant> getInstant(final String parameterName) {
        try {
            return Optional.ofNullable(cs.getDate(
                    Objects.requireNonNull(parameterName)).toInstant());
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<LocalDateTime> getLocalDateTime(final String parameterName) {
        try {
            return Optional.ofNullable(cs.getTimestamp(
                    Objects.requireNonNull(parameterName)).toLocalDateTime());
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<InputStreamAdapter> getBinaryStream(
            final String parameterName) {
        try {
            final byte[] bytes = cs.getBytes(Objects
                    .requireNonNull(parameterName));
            return Optional.ofNullable(bytes == null ? null
                    : new InputStreamAdapter(new ByteArrayInputStream(bytes)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<byte[]> getBinaryBytes(final String parameterName) {
        try {
            return Optional.ofNullable(cs.getBytes(Objects
                    .requireNonNull(parameterName)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if output parameter name is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Bitsss> getBinaryData(final String parameterName) {
        try {
            final byte[] bytes = cs.getBytes(Objects
                    .requireNonNull(parameterName));
            return Optional.ofNullable(bytes == null ? null : MemAllocator
                    .wrap(bytes));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    @Override
    public Map<String, Object> toMap() {
        if (outputParametersIndexs == null) {
            return new HashMap<String, Object>();
        }
        final Map<String, Object> map = new HashMap<>();
        outputParametersIndexs.forEach((k, v) -> {
            try {
                map.put(k.toString(), cs.getObject(k));
            } catch (final SQLException e) {
                throw new SimpleSQLException(e);
            }
        });
        return map;
    }

}
