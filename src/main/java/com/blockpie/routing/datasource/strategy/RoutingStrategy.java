package com.blockpie.routing.datasource.strategy;

import com.blockpie.routing.datasource.RoutingDataSourceGroup;

public interface RoutingStrategy {
    public int determine(RoutingDataSourceGroup routingDataSourceGroup);
}
