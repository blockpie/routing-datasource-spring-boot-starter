package com.blockpie.routing.datasource.builder;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.SQLException;
import java.util.Properties;

public class DruidDataSourceBuilder extends AbstractDataSourceBuilder {
    @Override
    public DruidDataSource build(Properties dataSourceConfig) {
        DruidDataSource dataSource = new DruidDataSource();

        this.bindInstanceProperties(dataSource, dataSourceConfig);

        try {
            dataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
