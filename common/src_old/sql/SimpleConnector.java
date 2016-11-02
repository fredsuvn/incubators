package com.cogician.quicker.sql;

import java.sql.Connection;

/**
 * Connector of simple sql, provides connection with one driver, url and user.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 19:07:59
 */
public class SimpleConnector {
    /**
     * Returns a connection.
     *
     * @return a connection, not null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public Connection getConnection();

    /**
     * Gets a query.
     *
     * @return a query, not null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public SimpleSQLQuery query();

    /**
     * Gets a executor.
     *
     * @return a executor, not null
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public SimpleSQLExecutor execute();

    /**
     * Returns driver name of this connection.
     *
     * @return driver name of this connection, not null
     */
    public String getDriverName();

    /**
     * Returns URL of this connection.
     *
     * @return URL of this connection, not null
     */
    public String getURL();

    /**
     * Returns user name of this connection.
     *
     * @return user name of this connection, not null
     */
    public String getUserName();

    /**
     * Returns password of this user of this connection.
     *
     * @return password of this user of this connection, not null
     */
    public char[] getPassword();
}
