package com.blockpie.routing.datasource.strategy;

import com.blockpie.routing.datasource.RoutingDataSourceGroup;

import java.util.concurrent.ThreadLocalRandom;

public class RandomRoutingStrategy implements RoutingStrategy {
    @Override
    public int determine(RoutingDataSourceGroup routingDataSourceGroup) {
        int dataSourceCount = routingDataSourceGroup.getDataSources().size();
        if (dataSourceCount == 1) {
            return 0;
        }
        return ThreadLocalRandom.current().nextInt(1, dataSourceCount);
    }
}
