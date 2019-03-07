package com.blockpie.routing.datasource;

import com.blockpie.routing.datasource.strategy.RoutingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoutingDataSourceContextHolder {
    private static final Logger logger = LoggerFactory.getLogger(RoutingDataSourceContextHolder.class);

    private static final Map<String, DataSource> dataSources;
    private static final Map<String, RoutingDataSourceGroup> dataSourceGroups;
    private static final ThreadLocal<String> keyHolder = new ThreadLocal<>();

    static {
        dataSources = new ConcurrentHashMap<>();
        dataSourceGroups = new ConcurrentHashMap<>();
    }

    public static RoutingDataSourceGroup addDataSourceGroup(String sourceName, String description, RoutingStrategy strategy) {
        RoutingDataSourceGroup group = new RoutingDataSourceGroup();
        group.setDescription(description);
        group.setStrategy(strategy);
        group.setStrategyCounter(new AtomicInteger(0));
        group.setDataSources(new LinkedList<>());
        dataSourceGroups.put(sourceName, group);
        return group;
    }

    public static RoutingDataSourceGroup getDataSourceGroup(String sourceName) {
        return dataSourceGroups.get(sourceName);
    }

    public static void addDataSource(String sourceName, int sourceIndex, DataSource dataSource, RoutingDataSourceGroup dataSourceGroup) {
        dataSources.put(prepareSourceKey(sourceName, sourceIndex), dataSource);
        dataSourceGroup.getDataSources().add(dataSource);
    }

    public static DataSource getDataSource(String key) {
        return dataSources.get(key);
    }

    public static void setRoutingDataSourceKey(String key) {
        logger.debug("Determine dataSource key to " + key);
        keyHolder.set(key);
    }

    public static String getRoutingDataSourceKey() {
        return keyHolder.get();
    }

    public static void clear() {
        keyHolder.remove();
    }

    public static void changeToMaster(String sourceName) {
        String key = prepareSourceKey(sourceName, 0);
        setRoutingDataSourceKey(key);
    }

    public static void changeToSlave(String sourceName) {
        RoutingDataSourceGroup dataSourceGroup = dataSourceGroups.get(sourceName);
        int sourceIndex = dataSourceGroup.determine();
        String key = prepareSourceKey(sourceName, sourceIndex);
        setRoutingDataSourceKey(key);
    }

    public static String prepareSourceKey(String sourceName, int sourceIndex) {
        return String.format("%s_%s", sourceName, sourceIndex);
    }
}
