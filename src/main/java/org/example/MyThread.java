package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MyThread extends Thread{

    private final BasicConnectionPool basicConnectionPool;

    MyThread(BasicConnectionPool basicConnectionPool) throws SQLException {
        this.basicConnectionPool = basicConnectionPool;
    }

    @Override
    public void run() {
        Connection connection = null;
        try {
            connection = basicConnectionPool.getConnection();
            try(Statement statement = connection.createStatement()) {
                final String sql = "SELECT pg_sleep(2)";
                statement.executeQuery(sql);
            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            basicConnectionPool.releaseConnection(connection);
        }
    }

}
