package com.lanf.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//配置多数据源
@Data
@Configuration
public class DataSourceConfig {

    //创建数据源
    @Bean(name = "DevOps")
    //从配置文件中获取数据源信息
    @ConfigurationProperties(prefix = "spring.datasource.devops")
    public DataSource dataSourceDevOps() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "HNSX")
    @ConfigurationProperties(prefix = "spring.datasource.hnsx")
    public DataSource dataSourceHNSX() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "WHGX")
    @ConfigurationProperties(prefix = "spring.datasource.whgx")
    public DataSource dataSourceWHGX() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "WHSY")
    @ConfigurationProperties(prefix = "spring.datasource.whsy")
    public DataSource dataSourceWHSY() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "WHGX_MES")
    @ConfigurationProperties(prefix = "spring.datasource.whgxmes")
    public DataSource dataSourceWHGX_MES() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "GXYT_TEST")
    @ConfigurationProperties(prefix = "spring.datasource.gxyttest")
    public DataSource dataSourceGXYT_TEST() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "MES_Prod_SX")
    @ConfigurationProperties(prefix = "spring.datasource.mesprodsx")
    public DataSource dataSourceMES_Prod_SX() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "MES_SX")
    @ConfigurationProperties(prefix = "spring.datasource.messx")
    public DataSource dataSourceMES_SX() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary  // @Primary 注解指定一个默认数据源。
    @Lazy     //懒加载,延迟该 Bean初始化, 避免与H2数据源冲突
    @DependsOn({"DevOps", "HNSX", "WHGX", "WHSY", "WHGX_MES", "GXYT_TEST", "MES_Prod_SX", "MES_SX"}) //指定加载顺序
    public DynamicDataSource dataSource(@Qualifier("HNSX") DataSource dataSourceHNSX,
                                        @Qualifier("WHGX") DataSource dataSourceWHGX,
                                        @Qualifier("WHSY") DataSource dataSourceWHSY,
                                        @Qualifier("DevOps") DataSource dataSourceDevOps,
                                        @Qualifier("WHGX_MES") DataSource dataSourceWHGX_MES,
                                        @Qualifier("GXYT_TEST") DataSource dataSourceGXYT_TEST,
                                        @Qualifier("MES_Prod_SX") DataSource dataSourceMES_Prod_SX,
                                        @Qualifier("MES_SX") DataSource dataSourceMES_SX) {
        //多个数据源注册到 targetDataSources 中
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("DevOps", dataSourceDevOps);
        targetDataSources.put("HNSX", dataSourceHNSX);
        targetDataSources.put("WHGX", dataSourceWHGX);
        targetDataSources.put("WHSY", dataSourceWHGX);
        targetDataSources.put("WHGX_MES", dataSourceWHGX_MES);
        targetDataSources.put("GXYT_TEST", dataSourceGXYT_TEST);
        targetDataSources.put("MES_Prod_SX", dataSourceMES_Prod_SX);
        targetDataSources.put("MES_SX", dataSourceMES_SX);

        DynamicDataSource dataSource = new DynamicDataSource();
        //设置所有目标数据源
        dataSource.setTargetDataSources(targetDataSources);
        //设置默认的数据源，在没有明确指定数据源时使用。
        dataSource.setDefaultTargetDataSource(dataSourceDevOps);
        return dataSource;
    }
}

