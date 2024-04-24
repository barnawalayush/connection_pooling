package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool{

    final int MAX_TIMEOUT = 2;
    private final List<Connection> connectionList;
    private final List<Connection> usedConnection = new ArrayList<>();

    private final String url;
    private final String password;
    private final String userName;

    private final Object lock = new Object();

    public BasicConnectionPool(String url, String password, String userName, List<Connection> connectionList) {
        this.url = url;
        this.password = password;
        this.userName = userName;
        this.connectionList = connectionList;
    }

    public static ConnectionPool getConnectionPool(String url, String userName, String password) throws SQLException {
        List<Connection> connectionList = new ArrayList<>();
        BasicConnectionPool basicConnectionPool = new BasicConnectionPool(url, password, userName, connectionList);
        for (int i = 0; i < POOL_SIZE; i++) {
            connectionList.add(basicConnectionPool.createConnection(url, userName, password));
        }
        return basicConnectionPool;
    }

    @Override
    public synchronized Connection getConnection() throws SQLException, InterruptedException {
        while (connectionList.isEmpty()) {
            Thread.sleep(1000);
        }
        Connection connection = connectionList.remove(connectionList.size() - 1);
        usedConnection.add(connection);
        if(!connection.isValid(MAX_TIMEOUT)){
            connection = createConnection(url, userName, password);
        }
        return connection;
    }

    @Override
    public  Connection createConnection(String url, String userName, String password) throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }

    @Override
    public void releaseConnection(Connection connection) {
        synchronized (lock) {
            if (connection == null) return;
            usedConnection.remove(connection);
            connectionList.add(connection);
        }
    }

    @Override
    public void shutdown() throws SQLException {
        usedConnection.forEach(this::releaseConnection);
        for(Connection connection : connectionList) {
            connection.close();
        }
        connectionList.clear();
    }


}
