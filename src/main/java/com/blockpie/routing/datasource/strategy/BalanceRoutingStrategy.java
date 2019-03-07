package com.blockpie.routing.datasource.strategy;

import com.blockpie.routing.datasource.RoutingDataSourceGroup;

import java.util.concurrent.atomic.AtomicInteger;

public class BalanceRoutingStrategy implements RoutingStrategy {
    @Override
    public int determine(RoutingDataSourceGroup routingDataSourceGroup) {
        int dataSourceCount = routingDataSourceGroup.getDataSources().size();
        if (dataSourceCount == 1) {
            return 0;
        }
        AtomicInteger counter = routingDataSourceGroup.getStrategyCounter();
        return counter.getAndIncrement() % (dataSourceCount - 1) + 1;
    }
}
