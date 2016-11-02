package com.cogician.quicker.sql.impl.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import com.cogician.quicker.sql.SimpleConnector;
import com.cogician.quicker.sql.SimpleSQLException;
import com.cogician.quicker.sql.SimpleSQLExecutor;
import com.cogician.quicker.sql.SimpleSQLQuery;

/**
 * Common implementation of {@linkplain SimpleConnector}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 19:17:06
 */
public class CommonSimpleSQLConnector implements SimpleConnector {
    /**
     * Driver name.
     */
    private final String driverName;

    /**
     * Connection URL.
     */
    private final String connectionURL;

    /**
     * User name.
     */
    private final String userName;

    /**
     * Password.
     */
    private final String password;

    /**
     * Construction with special drier name, connection URL, user name,
     * password.
     *
     * @param driverName
     *            drier name, not null
     * @param connectionURL
     *            connection URL, not null
     * @param userName
     *            user name, not null
     * @param password
     *            password, not null
     * @throws NullPointerException
     *             if exist null parameters
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    public CommonSimpleSQLConnector(final String driverName,
            final String connectionURL, final String userName,
            final String password) {
        this.driverName = Objects.requireNonNull(driverName);
        this.connectionURL = Objects.requireNonNull(connectionURL);
        this.userName = Objects.requireNonNull(userName);
        this.password = Objects.requireNonNull(password);
        try {
            Class.forName(driverName);
        } catch (final ClassNotFoundException e) {
            throw new SimpleSQLException(e);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(connectionURL, userName,
                    password);
        } catch (final SQLException e) {
            throw new SimpleSQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public SimpleSQLQuery query() {
        return new CommonSimpleSQLQuery(getConnection());
    }

    /**
     * {@inheritDoc}
     *
     * @throws SimpleSQLException
     *             if sql error occurs
     */
    @Override
    public SimpleSQLExecutor execute() {
        return new CommonSimpleSQLExecutor(getConnection());
    }

    @Override
    public String getDriverName() {
        return driverName;
    }

    @Override
    public String getURL() {
        // TODO Auto-generated method stub
        return connectionURL;
    }

    @Override
    public String getUserName() {
        // TODO Auto-generated method stub
        return userName;
    }

    @Override
    public char[] getPassword() {
        return password.toCharArray();
    }

}
