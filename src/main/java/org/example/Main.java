package org.example;


import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.SQLException;
import java.time.LocalTime;

public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/demo";
    private static final String USER_NAME = "postgres";
    private static final String USER_PASSWORD = "password";

    private final static Object lock = new Object();
    public static void main(String[] args) throws SQLException, InterruptedException {
//        ConnectionPool connectionPool = BasicConnectionPool.getConnectionPool(DB_URL, USER_NAME, USER_PASSWORD);
//        MyThread[] myThreads = new MyThread[15];
//        for(int i = 0; i < myThreads.length; i++) {
//            myThreads[i] = new MyThread((BasicConnectionPool) connectionPool);
//        }
//        LocalTime start = LocalTime.now();
//        for(MyThread myThread : myThreads) {
//            myThread.start();
//        }
//        for(MyThread myThread: myThreads) {
//            myThread.join();
//        }
//        long seconds = LocalTime.now().toSecondOfDay() - start.toSecondOfDay();
//        System.out.println(seconds);
//        connectionPool.shutdown();


        BuiltInConnectionPool builtInConnectionPool = new BuiltInConnectionPool(new ComboPooledDataSource());
        builtInConnectionPool.init(DB_URL);
        MyThreadModified[] myThreadModifiers = new MyThreadModified[15];
        for(int i = 0; i < myThreadModifiers.length; i++) {
            myThreadModifiers[i] = new MyThreadModified(builtInConnectionPool);
        }
        LocalTime localTime = LocalTime.now();
        for(MyThreadModified myThreadModified: myThreadModifiers) {
            myThreadModified.start();
        }
        for(MyThreadModified myThreadModified : myThreadModifiers) {
            myThreadModified.join();
        }
        System.out.println(LocalTime.now().toSecondOfDay() - localTime.toSecondOfDay());
    }
}