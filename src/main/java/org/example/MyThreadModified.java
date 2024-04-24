package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MyThreadModified extends Thread{

    private final BuiltInConnectionPool builtInConnectionPool;

    MyThreadModified(BuiltInConnectionPool builtInConnectionPool) throws SQLException {
        this.builtInConnectionPool = builtInConnectionPool;
    }

    @Override
    public void run() {
        try(Connection connection = builtInConnectionPool.getDataSource().getConnection()){
            try(Statement statement = connection.createStatement()) {
                final String sql = "SELECT pg_sleep(2)";
                statement.executeQuery(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
