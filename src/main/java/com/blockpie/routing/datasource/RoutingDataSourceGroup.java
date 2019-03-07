package com.blockpie.routing.datasource;

import com.blockpie.routing.datasource.strategy.RoutingStrategy;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoutingDataSourceGroup {
    private String description;
    private RoutingStrategy strategy;
    private AtomicInteger strategyCounter;
    private List<DataSource> dataSources;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStrategy(RoutingStrategy strategy) {
        this.strategy = strategy;
    }

    public AtomicInteger getStrategyCounter() {
        return strategyCounter;
    }

    public void setStrategyCounter(AtomicInteger strategyCounter) {
        this.strategyCounter = strategyCounter;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public int determine() {
        return this.strategy.determine(this);
    }
}
