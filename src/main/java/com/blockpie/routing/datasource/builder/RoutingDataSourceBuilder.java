package com.blockpie.routing.datasource.builder;

import javax.sql.DataSource;
import java.util.Properties;

public interface RoutingDataSourceBuilder {
    public DataSource build(Properties dataSourceConfig);
}
