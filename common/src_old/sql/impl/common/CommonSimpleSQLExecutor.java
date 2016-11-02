package com.cogician.quicker.sql.impl.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import com.cogician.quicker.sql.SimpleSQLException;
import com.cogician.quicker.sql.SimpleSQLExecutor;

/**
 * Common implementation of {@linkplain SimpleSQLExecutor}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-02 09:21:29
 */
public class CommonSimpleSQLExecutor extends CommonPreparedSetting implements
        SimpleSQLExecutor {
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
    CommonSimpleSQLExecutor(final Connection conn) {
        this.conn = Objects.requireNonNull(conn);
    }

    /**
     * Constructs with special prepared statement.
     *
     * @param conn
     *            prepared statement, not null
     * @throws NullPointerException
     *             if special driver is null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    CommonSimpleSQLExecutor(final PreparedStatement ps) {
        this.ps = Objects.requireNonNull(ps);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public StringBuilder sql() {
        return sql;
    }

    @Override
    public SimpleSQLExecutor sql(final String sql) {
        this.sql = null;
        this.sql = new StringBuilder(sql);
        return this;
    }

    @Override
    public SimpleSQLExecutor append(final String sql) {
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
    public long execute() {
        checkValid();
        invalid();
        try {
            ps = ps == null ? setting.preparedStatement(conn, sql.toString())
                    : ps;
            return ps.executeLargeUpdate();
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
    public long call() {
        checkValid();
        invalid();
        checkValid();
        invalid();
        try {
            cs = setting.callableStatement(conn, sql.toString());
            return cs.executeLargeUpdate();
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
    public SimpleSQLExecutor addBatch() {
        checkValid();
        invalid();
        try {
            ps = ps == null ? setting.preparedStatement(conn, sql.toString())
                    : ps;
            ps.addBatch();
            return new CommonSimpleSQLExecutor(ps);
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
    public long[] batchExecuteUpdate() {
        checkValid();
        invalid();
        try {
            ps = ps == null ? setting.preparedStatement(conn, sql.toString())
                    : ps;
            return ps.executeLargeBatch();
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
    public void close() {
        invalid();
        try {
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
