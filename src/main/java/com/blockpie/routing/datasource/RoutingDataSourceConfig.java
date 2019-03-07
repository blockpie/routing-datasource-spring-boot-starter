package com.blockpie.routing.datasource;

import java.util.List;
import java.util.Properties;

public class RoutingDataSourceConfig {
    private String description;
    private String strategy;
    private List<Properties> list;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public List<Properties> getList() {
        return list;
    }

    public void setList(List<Properties> list) {
        this.list = list;
    }
}
