package com.cogician.quicker.sql;

import com.cogician.quicker.sql.impl.common.CommonSimpleSQLConnector;

/**
 * Factory to build {@linkplain SimpleConnector}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-02 10:07:36
 */
public class SimpleSQLFactory {
    /**
     * Gets {@linkplain SimpleConnector}with special drier name, connection URL,
     * user name, password.
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
    public static SimpleConnector getConnector(final String driverName,
            final String connectionURL, final String userName,
            final String password) {
        return new CommonSimpleSQLConnector(driverName, connectionURL,
                userName, password);
    }
}
