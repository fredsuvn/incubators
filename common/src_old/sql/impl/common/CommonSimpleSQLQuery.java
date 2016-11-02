package com.cogician.quicker.sql.impl.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.cogician.quicker.sql.SimpleRow;
import com.cogician.quicker.sql.SimpleSQLException;
import com.cogician.quicker.sql.SimpleSQLQuery;

/**
 * Common implementation of {@linkplain SimpleSQLQuery}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 19:24:40
 */
class CommonSimpleSQLQuery extends CommonPreparedSetting implements
        SimpleSQLQuery {

    /**
     * Connection.
     */
    private Connection conn = null;

    /**
     * Prepared statement.
     */
    private PreparedStatement ps = null;

    /**
     * Callable statement.
     */
    private CallableStatement cs = null;

    /**
     * Result set.
     */
    private ResultSet rs = null;

    /**
     * Parameters setting.
     */
    private CommonPreparedSetting setting = new CommonPreparedSetting();

    /**
     * SQL content, never null.
     */
    private StringBuilder sql = new StringBuilder();

    /**
     * Constructs with special connection.
     *
     * @param conn
     *            special connection, not null
     * @throws NullPointerException
     *             if special driver is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    CommonSimpleSQLQuery(final Connection conn) {
        this.conn = Objects.requireNonNull(conn);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public StringBuilder sql() {
        checkValid();
        return sql;
    }

    @Override
    public SimpleSQLQuery sql(final String sql) {
        this.sql = null;
        this.sql = new StringBuilder(sql);
        return this;
    }

    @Override
    public SimpleSQLQuery append(final String sql) {
        this.sql.append(sql);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public long count() {
        checkValid();
        invalid();
        long l = -1;
        try {
            ps = setting.preparedStatement(conn,
                    "select count(*) as totalCount from (" + sql.toString()
                            + ")");
            rs = ps.executeQuery();
            if (rs.next()) {
                l = rs.getLong("totalCount");
            }
            if (rs.wasNull() || rs.next()) {
                close();
                throw new SimpleSQLException("Cannot query count!");
            }
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
        return l;
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public Stream<SimpleRow> queryAsStream() {
        checkValid();
        invalid();
        try {
            long l = 0;
            ps = setting.preparedStatement(conn,
                    "select count(*) as totalCount from (" + sql.toString()
                            + ")");
            rs = ps.executeQuery();
            if (rs.next()) {
                l = rs.getLong("totalCount");
            }
            if (rs.wasNull() || rs.next()) {
                close();
                throw new SimpleSQLException("Cannot query count!");
            }
            rs.close();
            ps.close();

            ps = setting.preparedStatement(conn, sql.toString(),
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            return Stream.generate(() -> {
                try {
                    while (rs.next()) {
                        final SimpleRow row = new CommonSimpleSQLRow(rs);
                        return row;
                    }
                    return null;
                } catch (final Exception e) {
                    throw new SimpleSQLException(e);
                }
            }).limit(l);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public ResultSet query() {
        checkValid();
        invalid();
        try {
            ps = setting.preparedStatement(conn, sql.toString());
            rs = ps.executeQuery();
            return rs;
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if result set type or concurrency is illegal
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public ResultSet query(final int resultSetType,
            final int resultSetConcurrency) {
        checkValid();
        invalid();
        try {
            ps = setting.preparedStatement(conn, sql.toString(), resultSetType,
                    resultSetConcurrency);
            rs = ps.executeQuery();
            return rs;
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public ResultSet call() {
        checkValid();
        invalid();
        try {
            cs = setting.callableStatement(conn, sql.toString());
            rs = cs.executeQuery();
            return rs;
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if result set type or concurrency is illegal
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public ResultSet call(final int resultSetType,
            final int resultSetConcurrency) {
        checkValid();
        invalid();
        try {
            cs = setting.callableStatement(conn, sql.toString(), resultSetType,
                    resultSetConcurrency);
            rs = cs.executeQuery();
            return rs;
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public SimpleRow callAsOutParametersRow() {
        checkValid();
        invalid();
        cs = setting.callableStatement(conn, sql.toString());
        final Optional<Map<Integer, Integer>> opt = getOutputParameters();
        if (opt.isPresent()) {
            return new OutputParamsSimpleSQLRow(cs, opt.get());
        } else {
            return new OutputParamsSimpleSQLRow(cs, null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public void close() {
        invalid();
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
                rs = null;
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
                ps = null;
            }
            if (cs != null && !cs.isClosed()) {
                cs.close();
                cs = null;
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
            setting = null;
            sql = null;
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

}
