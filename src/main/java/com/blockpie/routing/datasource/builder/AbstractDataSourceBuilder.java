package com.blockpie.routing.datasource.builder;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import javax.sql.DataSource;
import java.util.Properties;

public abstract class AbstractDataSourceBuilder implements RoutingDataSourceBuilder {
    private static ConfigurationPropertyNameAliases configurationPropertyNameAliases;

    static {
        configurationPropertyNameAliases = new ConfigurationPropertyNameAliases();
        configurationPropertyNameAliases.addAliases("url", "jdbc-url");
        configurationPropertyNameAliases.addAliases("username", "user");
    }

    protected void bindInstanceProperties(DataSource dataSource, Properties properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(source.withAliases(configurationPropertyNameAliases));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(dataSource));
    }
}
