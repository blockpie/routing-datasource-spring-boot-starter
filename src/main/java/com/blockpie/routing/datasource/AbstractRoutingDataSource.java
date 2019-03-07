package com.blockpie.routing.datasource;

import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractRoutingDataSource extends AbstractDataSource {
    @Override
    public Connection getConnection() throws SQLException {
        return determineDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineDataSource().getConnection(username, password);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return determineDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return (iface.isInstance(this) || determineDataSource().isWrapperFor(iface));
    }

    private DataSource determineDataSource() {
        DataSource dataSource = null;
        String key = this.determineKey();
        if (key != null && key.length() > 0) {
            dataSource = RoutingDataSourceContextHolder.getDataSource(key);
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine dataSource for key " + key);
        }
        return dataSource;
    }

    protected abstract String determineKey();
}
