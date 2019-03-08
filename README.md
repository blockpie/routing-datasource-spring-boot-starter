## routing-datasource-spring-boot-starter

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.blockpie/routing-datasource-spring-boot-starter/badge.svg)](https://search.maven.org/artifact/com.blockpie/routing-datasource-spring-boot-starter/)
[![GitHub release](https://img.shields.io/github/release/blockpie/routing-datasource-spring-boot-starter.svg)](https://github.com/blockpie/routing-datasource-spring-boot-starter/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

### Introduction 简介

- Routing datasource for Spring Boot 结合 SpringBoot 使用的路由数据源
- Master-slave splitting 主从分离
- Switching slave datasource random or balanced 从库随机或均衡切换

### How to use 使用方法

**1. Import maven dependency (引入 maven 依赖)**

```xml
<dependency>
  <groupId>com.blockpie</groupId>
  <artifactId>routing-datasource-spring-boot-starter</artifactId>
  <version>${latest.release.version}</version>
</dependency>
```
> Note: Please change the ${latest.release.version} to the actual version

> 注意: 请修改 ${latest.release.version} 为实际版本

**2. Configure rule configuration (配置数据源)**
```yaml
routing-datasource:
  data-source-map:
    test1:
      description: test database 1
      strategy: random
      list:
        - driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/test1_master
          username: root
          password: pass
          type: com.alibaba.druid.pool.DruidDataSource
        - driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/test1_slave1
          username: root
          password: pass
          type: com.zaxxer.hikari.HikariDataSource
        - driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/test1_slave2
          username: root
          password: pass
    test2:
      description: test database 2
      strategy: balance
      list:
        - driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/test2
          username: root
          password: pass
```
> list[0] is the master, others are the slaves

> 数据源的第一项为主数据源, 其余的为从数据源

> strategy: random / balance (default)

> strategy 支持 random / balance 两个选项, 默认为 balance
