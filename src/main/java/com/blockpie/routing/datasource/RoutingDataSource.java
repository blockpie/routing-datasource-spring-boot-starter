package com.blockpie.routing.datasource;

public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected String determineKey() {
        return RoutingDataSourceContextHolder.getRoutingDataSourceKey();
    }
}
