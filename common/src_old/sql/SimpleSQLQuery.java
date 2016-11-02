package com.cogician.quicker.sql;

import java.sql.ResultSet;
import java.util.stream.Stream;

/**
 * Represents a sql or procedure query, this is a one-off query. The method
 * {@linkplain #sql()} and setXXX are used to set this sql or procedure, when
 * close or use "final method" like {@linkplain #query()} or
 * {@linkplain #count()} or others, instance will be invalid.
 * <p>
 * Should be close after using.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-25 17:19:12
 */
public interface SimpleSQLQuery extends SimpleSQL, SimpleSQLPreparedSetting {
    /**
     * Sets the sql.
     *
     * @param sql
     *            sql
     * @return this instance after setting
     */
    public SimpleSQLQuery sql(String sql);

    /**
     * Appends the sql.
     *
     * @param sql
     *            sql
     * @return this instance after appending
     */
    public SimpleSQLQuery append(String sql);

    /**
     * Final method, returns count of result of sql in a query.
     *
     * @return count of result of sql
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public long count();

    /**
     * Final method, returns result of a query as a stream, the result cursor
     * only move forward and content is read only.
     * <p>
     * Cannot parallel returned stream.
     *
     * @return result as a stream, cannot paralleled, not null
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public Stream<SimpleRow> queryAsStream();

    /**
     * Final method, returns result of a query, the result cursor only move
     * forward and content is read only.
     *
     * @return result, not null
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public ResultSet query();

    /**
     * Final method, returns result of a query with special special result set
     * type and special result type concurrency.
     * <p>
     * The result set type set include: {@linkplain ResultSet#TYPE_FORWARD_ONLY}, {@linkplain ResultSet#TYPE_SCROLL_INSENSITIVE} and
     * {@linkplain ResultSet#TYPE_SCROLL_SENSITIVE}.
     * <p>
     * The result set concurrency include:
     * {@linkplain ResultSet#CONCUR_READ_ONLY} and
     * {@linkplain ResultSet#CONCUR_UPDATABLE}.
     *
     * @param resultSetType
     *            result set type in bounds
     * @param resultSetConcurrency
     *            result set concurrency in bounds
     * @return result, not null
     * @throws IllegalArgumentException
     *             if result set type or concurrency is illegal
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public ResultSet query(int resultSetType, int resultSetConcurrency);

    /**
     * Final method, returns result of a procedure, the result cursor only move
     * forward and content is read only.
     *
     * @return result, not null
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public ResultSet call();

    /**
     * Final method, returns result of a procedure with special special result
     * set type and special result type concurrency.
     * <p>
     * The result set type set include: {@linkplain ResultSet#TYPE_FORWARD_ONLY}, {@linkplain ResultSet#TYPE_SCROLL_INSENSITIVE} and
     * {@linkplain ResultSet#TYPE_SCROLL_SENSITIVE}.
     * <p>
     * The result set concurrency include:
     * {@linkplain ResultSet#CONCUR_READ_ONLY} and
     * {@linkplain ResultSet#CONCUR_UPDATABLE}.
     *
     * @param resultSetType
     *            result set type in bounds
     * @param resultSetConcurrency
     *            result set concurrency in bounds
     * @return result, not null
     * @throws IllegalArgumentException
     *             if result set type or concurrency is illegal
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public ResultSet call(int resultSetType, int resultSetConcurrency);

    /**
     * Final method, returns output parameters of a procedure as one row.
     *
     * @return output parameters of a procedure as one row, not null
     * @throws SimpleSQLException
     *             if sql error occurs.
     */
    public SimpleRow callAsOutParametersRow();

}
