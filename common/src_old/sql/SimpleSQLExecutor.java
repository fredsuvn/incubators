package com.cogician.quicker.sql;

/**
 * Represents a sql or procedure executor, this is a one-off execute. The method
 * {@linkplain #sql()} and setXXX are used to set this sql or procedure, when
 * close or use "final method" like {@linkplain #execute()} or
 * {@linkplain #call()} or others, instance will be invalid.
 * <p>
 * Should be close after using.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-01 15:58:11
 */
public interface SimpleSQLExecutor extends SimpleSQL, SimpleSQLPreparedSetting {
    /**
     * Sets the sql.
     *
     * @param sql
     *            sql
     * @return this instance after setting
     */
    public SimpleSQLExecutor sql(String sql);

    /**
     * Sppends the sql.
     *
     * @param sql
     *            sql
     * @return this instance after appending
     */
    public SimpleSQLExecutor append(String sql);

    /**
     * Final method, execute sql, return influent row number.
     *
     * @return influent row number
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public long execute();

    /**
     * Final method, execute procedure, return influent row number.
     *
     * @return influent row number
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public long call();

    /**
     * Adds this sql to batch of current connection and return an valid instance
     * of this interface, if the returned instance call
     * {@linkplain #batchExecuteUpdate()}, this instance and returned instance
     * will be batched execute.
     * <p>
     * See {@linkplain Statement#addBatch(String)}.
     *
     * @return next instance, not null
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public SimpleSQLExecutor addBatch();

    /**
     * Final method, batched execute sqls or procedures of this and previous
     * instance (see {@linkplain #addBatch()}), return influent row number for
     * each sql.
     *
     * @return influent row number for each sql, not null
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public long[] batchExecuteUpdate();

    /**
     * Close this sql and result set.
     *
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    @Override
    public void close();
}
