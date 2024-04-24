package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {

    int POOL_SIZE = 10;


    void releaseConnection(Connection connection);

    void shutdown() throws SQLException;

    Connection getConnection() throws SQLException, InterruptedException;

    Connection createConnection(String url, String userName, String password) throws SQLException;

}
