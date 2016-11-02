package com.cogician.quicker.sql.impl.common;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
 * Common implementation of {@linkplain SimpleRow}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 19:25:14
 */
class CommonSimpleSQLRow implements SimpleRow {
    /**
     * Result set.
     */
    private final ResultSet result;

    /**
     * Constructs with special result set.
     *
     * @param result
     *            special result set, not null
     * @throws NullPointerException
     *             if special result set is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    CommonSimpleSQLRow(final ResultSet result) {
        this.result = Objects.requireNonNull(result);
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public int getRowNumber() {
        try {
            return result.getRow();
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Object> getObject(final String columnLabel) {
        try {
            return Optional.ofNullable(result.getObject(Objects
                    .requireNonNull(columnLabel)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Integer> getInt(final String columnLabel) {
        try {
            final int i = result.getInt(Objects.requireNonNull(columnLabel));
            return Optional.ofNullable(result.wasNull() ? null : i);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Long> getLong(final String columnLabel) {
        try {
            final long l = result.getLong(Objects.requireNonNull(columnLabel));
            return Optional.ofNullable(result.wasNull() ? null : l);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<BigDecimal> getBigDecimal(final String columnLabel) {
        try {
            return Optional.ofNullable(result.getBigDecimal(Objects
                    .requireNonNull(columnLabel)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<String> getString(final String columnLabel) {
        try {
            return Optional.ofNullable(result.getString(Objects
                    .requireNonNull(columnLabel)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Instant> getInstant(final String columnLabel) {
        try {
            return Optional.ofNullable(result.getDate(
                    Objects.requireNonNull(columnLabel)).toInstant());
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<LocalDateTime> getLocalDateTime(final String columnLabel) {
        try {
            return Optional.ofNullable(result.getTimestamp(
                    Objects.requireNonNull(columnLabel)).toLocalDateTime());
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<InputStreamAdapter> getBinaryStream(final String columnLabel) {
        try {
            final InputStream in = result.getBinaryStream(Objects
                    .requireNonNull(columnLabel));
            return Optional.ofNullable(in == null ? null
                    : new InputStreamAdapter(in));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<byte[]> getBinaryBytes(final String columnLabel) {
        try {
            return Optional.ofNullable(result.getBytes(Objects
                    .requireNonNull(columnLabel)));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if special column label is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Optional<Bitsss> getBinaryData(final String columnLabel) {
        try {
            final byte[] bytes = result.getBytes(Objects
                    .requireNonNull(columnLabel));
            return Optional.ofNullable(bytes == null ? null : MemAllocator
                    .wrap(bytes));
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    @Override
    public Map<String, Object> toMap() {
        try {
            final Map<String, Object> map = new HashMap<>();
            ResultSetMetaData meta;
            meta = result.getMetaData();
            final int count = meta.getColumnCount();
            for (int i = 1; i <= count; i++) {
                final String key = meta.getColumnLabel(i);
                map.put(meta.getColumnLabel(i), result.getObject(key));
            }
            return map;
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

}
