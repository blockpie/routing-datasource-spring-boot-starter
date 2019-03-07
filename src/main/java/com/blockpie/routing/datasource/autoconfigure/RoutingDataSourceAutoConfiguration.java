package com.blockpie.routing.datasource.autoconfigure;

import com.blockpie.routing.datasource.RoutingDataSource;
import com.blockpie.routing.datasource.RoutingDataSourceLoader;
import com.blockpie.routing.datasource.aop.RoutingDataSourceAspect;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(RoutingDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class RoutingDataSourceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(RoutingDataSourceProperties properties) throws Exception {
        RoutingDataSourceLoader dataSourceLoader = new RoutingDataSourceLoader();
        dataSourceLoader.load(properties.getDataSourceMap());
        return new RoutingDataSource();
    }

    @Bean
    public RoutingDataSourceAspect routingDataSourceAspect() {
        return new RoutingDataSourceAspect();
    }
}
