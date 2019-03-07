package com.blockpie.routing.datasource;

import com.blockpie.routing.datasource.builder.DefaultDataSourceBuilder;
import com.blockpie.routing.datasource.builder.DruidDataSourceBuilder;
import com.blockpie.routing.datasource.builder.RoutingDataSourceBuilder;
import com.blockpie.routing.datasource.strategy.BalanceRoutingStrategy;
import com.blockpie.routing.datasource.strategy.RandomRoutingStrategy;
import com.blockpie.routing.datasource.strategy.RoutingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.*;

public class RoutingDataSourceLoader {
    private static final Logger logger = LoggerFactory.getLogger(RoutingDataSourceLoader.class);

    private static final RoutingDataSourceBuilder defaultRoutingDataSourceBuilder = new DefaultDataSourceBuilder();
    private static final Map<String, RoutingDataSourceBuilder> specialDataSourceTypeBuilders = new HashMap<>();
    private static final Map<String, RoutingStrategy> routingStrategies = new HashMap<>();
    private static final String defaultRoutingStrategy = "balance";

    static {
        specialDataSourceTypeBuilders.put("com.alibaba.druid.pool.DruidDataSource", new DruidDataSourceBuilder());

        routingStrategies.put("balance", new BalanceRoutingStrategy());
        routingStrategies.put("random", new RandomRoutingStrategy());
    }

    public void load(Map<String, RoutingDataSourceConfig> configMap) throws Exception {
        for (Map.Entry<String, RoutingDataSourceConfig> groupConfig : configMap.entrySet()) {
            String sourceName = groupConfig.getKey();
            RoutingDataSourceConfig config = groupConfig.getValue();
            List<Properties> sourceConfigList = Optional.ofNullable(config.getList()).orElseThrow(() -> this.getSourceListEmptyException(sourceName));
            int sourceConfigCount = sourceConfigList.size();
            if (sourceConfigCount == 0) {
                throw this.getSourceListEmptyException(sourceName);
            }

            RoutingDataSourceGroup dataSourceGroup = Optional.ofNullable(RoutingDataSourceContextHolder.getDataSourceGroup(sourceName))
                                                   .orElseGet(()->{
                                                       String strategy = Optional.ofNullable(config.getStrategy()).orElse(defaultRoutingStrategy);
                                                       RoutingStrategy routingStrategy = routingStrategies.get(strategy);
                                                       if (routingStrategy == null) {
                                                           routingStrategy = routingStrategies.get(defaultRoutingStrategy);
                                                           logger.error("Can not found routing strategy [{}], changed to default strategy [{}].", strategy, defaultRoutingStrategy);
                                                       }
                                                       return RoutingDataSourceContextHolder.addDataSourceGroup(sourceName, config.getDescription(), routingStrategy);
                                                   });

            for (int i=0; i<sourceConfigCount; i++) {
                Properties properties = sourceConfigList.get(i);
                String sourceTypeName = properties.getProperty("type");
                RoutingDataSourceBuilder sourceBuilder = specialDataSourceTypeBuilders.get(sourceTypeName);
                DataSource dataSource = null;
                if (sourceBuilder != null) {
                    dataSource = sourceBuilder.build(properties);
                } else {
                    dataSource = defaultRoutingDataSourceBuilder.build(properties);
                }

                RoutingDataSourceContextHolder.addDataSource(sourceName, i, dataSource, dataSourceGroup);
                logger.debug("RoutingDataSource [{} - {}] loaded.", sourceName, i==0 ? "MASTER" : "SLAVE " + String.valueOf(i));
            }
        }
    }

    private Exception getSourceListEmptyException(String sourceName) {
        return new Exception("RoutingDataSource [" + sourceName + "] source list config can not be empty.");
    }
}
