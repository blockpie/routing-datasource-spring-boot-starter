package com.blockpie.routing.datasource.autoconfigure;

import com.blockpie.routing.datasource.RoutingDataSourceConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "routing-datasource")
public class RoutingDataSourceProperties {
    private Map<String, RoutingDataSourceConfig> dataSourceMap;

    public Map<String, RoutingDataSourceConfig> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, RoutingDataSourceConfig> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }
}
