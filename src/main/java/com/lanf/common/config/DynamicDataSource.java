package com.lanf.common.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

//自定义的数据源实现类,根据业务逻辑动态选择并切换不同的数据源
public class DynamicDataSource extends AbstractRoutingDataSource {
    //存储当前线程的数据源标识,确保每个线程都有自己的独立副本
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    // 设置数据源
    public static void setDataSource(String dataSourceKey) {
        contextHolder.set(dataSourceKey);
    }

    // 清除数据源
    public static void clearDataSource() {
        contextHolder.remove();
    }

    //会在每次进行数据库操作时调用这个方法，以确定当前应该使用哪个数据源。
    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }
}