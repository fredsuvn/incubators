package com.cogician.quicker.sql;

import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 * Represents result of a query(DQL) or procedure. The result contains a result
 * set if it was a query language(DQL) executed or the procedure returned a
 * result set, and/or a output parameters set if it executed a procedure.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-13 15:47:20
 */
public class SimpleResult {
    /**
     * Result set, data source of this class if not null.
     */
    private final ResultSet result;

    /**
     * Statement for getting output parameters if not null.
     */
    private final CallableStatement statement;

    /**
     * Buffered of output parameters.
     */
    private final SimpleOutputParameters outputParameters = null;

    /**
     * Constructs with special result set and statement. Either be null if no
     * result or no output parameter.
     *
     * @param result
     *            special result set
     * @param statement
     *            special callable statement
     */
    SimpleResult(final ResultSet result, final CallableStatement statement) {
        this.result = result;
        this.statement = statement;
    }

    /**
     * Returns output parameters of procedure, if no parameter, return null.
     *
     * @return output parameters of procedure or null
     */
    public SimpleOutputParameters getOutputParameters() {
        if (outputParameters != null) {
            return outputParameters;
        } else if (statement == null) {
            return null;
        } else {
            return new SimpleOutputParameters(statement);
        }
    }

    /**
     * Returns original result set of this instance, if no result set, return
     * null.
     *
     * @return original result set of this instance or null
     */
    public ResultSet getResultSet() {
        return result;
    }

    public static class SimpleRowStream {

    }

}
