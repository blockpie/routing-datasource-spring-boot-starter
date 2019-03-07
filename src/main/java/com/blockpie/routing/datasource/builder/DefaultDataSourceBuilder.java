package com.blockpie.routing.datasource.builder;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.Properties;

public class DefaultDataSourceBuilder extends AbstractDataSourceBuilder {
    private static final String[] DATA_SOURCE_TYPE_NAMES = new String[] {
            "com.zaxxer.hikari.HikariDataSource",
            "org.apache.tomcat.jdbc.pool.DataSource",
            "org.apache.commons.dbcp2.BasicDataSource" };

    private static Class<? extends DataSource> defaultSourceType;

    @Override
    public DataSource build(Properties dataSourceConfig) {
        DataSource dataSource = BeanUtils.instantiateClass(this.getDefaultSourceType());
        this.bindInstanceProperties(dataSource, dataSourceConfig);
        return dataSource;
    }

    private Class<? extends DataSource> getDefaultSourceType() {
        Class<? extends DataSource> type = null;

        if (defaultSourceType == null) {
            for (String name : DATA_SOURCE_TYPE_NAMES) {
                type = this.getTypeByName(name);
                if (type != null) {
                    defaultSourceType = type;
                    break;
                }
            }
        }

        if (defaultSourceType != null) {
            return defaultSourceType;
        }

        throw new IllegalStateException("No supported DataSource type found.");
    }

    @SuppressWarnings("unchecked")
    private Class<? extends DataSource> getTypeByName(String typeName) {
        Class<? extends DataSource> type = null;
        try {
            type = (Class<? extends DataSource>) ClassUtils.forName(typeName, null);
        } catch (Exception ignored) {

        }

        return type;
    }
}
