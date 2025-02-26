package com.netflix.discovery.shared.transport;

import com.netflix.config.DynamicPropertyFactory;

import static com.netflix.discovery.shared.transport.PropertyBasedTransportConfigConstants.*;

/**
 * @author David Liu
 *
 * 基于配置文件的网络传输配置实现类
 * todo 还是没搞懂，这些配置都是做什么用的
 */
public class DefaultEurekaTransportConfig implements EurekaTransportConfig {

    private static final String SUB_NAMESPACE = TRANSPORT_CONFIG_SUB_NAMESPACE + ".";

    /**
     * 命名空间
     */
    private final String namespace;
    /**
     * 配置文件对象
     */
    private final DynamicPropertyFactory configInstance;

    public DefaultEurekaTransportConfig(String parentNamespace, DynamicPropertyFactory configInstance) {
        // 命名空间
        this.namespace = parentNamespace == null
                ? SUB_NAMESPACE
                : (parentNamespace.endsWith(".")
                    ? parentNamespace + SUB_NAMESPACE
                    : parentNamespace + "." + SUB_NAMESPACE);
        // 配置文件对象
        this.configInstance = configInstance;
    }

    @Override
    public int getSessionedClientReconnectIntervalSeconds() {
        return configInstance.getIntProperty(namespace + SESSION_RECONNECT_INTERVAL_KEY, Values.SESSION_RECONNECT_INTERVAL).get();
    }

    @Override
    public double getRetryableClientQuarantineRefreshPercentage() {
        return configInstance.getDoubleProperty(namespace + QUARANTINE_REFRESH_PERCENTAGE_KEY, Values.QUARANTINE_REFRESH_PERCENTAGE).get();
    }

    @Override
    public int getApplicationsResolverDataStalenessThresholdSeconds() {
        return configInstance.getIntProperty(namespace + DATA_STALENESS_THRESHOLD_KEY, Values.DATA_STALENESS_TRHESHOLD).get();
    }

    @Override
    public boolean applicationsResolverUseIp() {
        return configInstance.getBooleanProperty(namespace + APPLICATION_RESOLVER_USE_IP_KEY, false).get();
    }

    @Override
    public int getAsyncResolverRefreshIntervalMs() {
        return configInstance.getIntProperty(namespace + ASYNC_RESOLVER_REFRESH_INTERVAL_KEY, Values.ASYNC_RESOLVER_REFRESH_INTERVAL).get();
    }

    @Override
    public int getAsyncResolverWarmUpTimeoutMs() {
        return configInstance.getIntProperty(namespace + ASYNC_RESOLVER_WARMUP_TIMEOUT_KEY, Values.ASYNC_RESOLVER_WARMUP_TIMEOUT).get();
    }

    @Override
    public int getAsyncExecutorThreadPoolSize() {
        return configInstance.getIntProperty(namespace + ASYNC_EXECUTOR_THREADPOOL_SIZE_KEY, Values.ASYNC_EXECUTOR_THREADPOOL_SIZE).get();
    }

    @Override
    public String getWriteClusterVip() {
        return configInstance.getStringProperty(namespace + WRITE_CLUSTER_VIP_KEY, null).get();
    }

    @Override
    public String getReadClusterVip() {
        return configInstance.getStringProperty(namespace + READ_CLUSTER_VIP_KEY, null).get();
    }

    @Override
    public String getBootstrapResolverStrategy() {
        return configInstance.getStringProperty(namespace + BOOTSTRAP_RESOLVER_STRATEGY_KEY, null).get();
    }

    @Override
    public boolean useBootstrapResolverForQuery() {
        return configInstance.getBooleanProperty(namespace + USE_BOOTSTRAP_RESOLVER_FOR_QUERY, true).get();
    }
}
