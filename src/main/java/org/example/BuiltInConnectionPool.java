package org.example;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class BuiltInConnectionPool {

    private final DataSource dataSource;

    public BuiltInConnectionPool(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init(String url) throws SQLException {
        if (dataSource instanceof BasicDataSource basicDataSource) {
            basicDataSource.setUrl(url);
        }
        else if(dataSource instanceof HikariDataSource hikariDataSource) {
            hikariDataSource.setJdbcUrl(url);
        }
        else if(dataSource instanceof ComboPooledDataSource cpds) {
            cpds.setJdbcUrl(url);
        }
        else{
            throw new IllegalStateException("No datasource of type " + dataSource.getClass() + "  exists!!!");
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
