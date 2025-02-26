/*
 * Copyright 2012 Netflix, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.netflix.discovery;

import com.google.inject.ImplementedBy;
import com.netflix.discovery.shared.transport.EurekaTransportConfig;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Configuration information required by the eureka clients to register an
 * instance with <em>Eureka</em> server.
 *
 * <p>
 * Most of the required information is provided by the default configuration
 * {@link DefaultEurekaClientConfig}. The users just need to provide the eureka
 * server service urls. The Eureka server service urls can be configured by 2
 * mechanisms
 *
 * 1) By registering the information in the DNS. 2) By specifying it in the
 * configuration.
 * </p>
 *
 *
 * Once the client is registered, users can look up information from
 * {@link EurekaClient} based on <em>virtual hostname</em> (also called
 * VIPAddress), the most common way of doing it or by other means to get the
 * information necessary to talk to other instances registered with
 * <em>Eureka</em>.
 *
 * <p>
 * Note that all configurations are not effective at runtime unless and
 * otherwise specified.
 * todo ？？？？什么意思
 * </p>
 *
 * @author Karthik Ranganathan
 *
 */
@ImplementedBy(DefaultEurekaClientConfig.class)
public interface EurekaClientConfig {

    /**
     * Indicates how often(in seconds) to fetch the registry information from
     * the eureka server.
     *
     * 从 Eureka-Server 拉取注册信息频率，单位：秒
     *
     * @return the fetch interval in seconds.
     */
    int getRegistryFetchIntervalSeconds();

    /**
     * Indicates how often(in seconds) to replicate instance changes to be
     * replicated to the eureka server.
     *
     * 向 Eureka-Server 同步实例对象信息变化频率，单位：秒
     * todo 和续约renew有什么区别？同步的内容不同？和healthCheck有关？
     * todo com.netflix.discovery.EurekaClient#registerHealthCheckCallback(com.netflix.appinfo.HealthCheckCallback)
     *
     * @return the instance replication interval in seconds.
     */
    int getInstanceInfoReplicationIntervalSeconds();

    /**
     * Indicates how long initially (in seconds) to replicate instance info
     * to the eureka server
     *
     * 向 Eureka-Server 同步应用信息变化初始化延迟，单位：秒
     */
    int getInitialInstanceInfoReplicationIntervalSeconds();

    /**
     * Indicates how often(in seconds) to poll for changes to eureka server
     * information.
     *
     * <p>
     * Eureka servers could be added or removed and this setting controls how
     * soon the eureka clients should know about it.
     * </p>
     *
     * 轮询获取 Eureka-Server 地址变更频率，单位：秒
     * todo 也是DNS方式需要的
     *
     * @return the interval to poll for eureka service url changes.
     */
    int getEurekaServiceUrlPollIntervalSeconds();

    /**
     * Gets the proxy host to eureka server if any.
     *
     * Eureka-Server 代理主机
     * todo 代理主机是什么？
     *
     * @return the proxy host.
     */
    String getProxyHost();

    /**
     * Gets the proxy port to eureka server if any.
     *
     * Eureka-Server 代理主机端口
     *
     * @return the proxy port.
     */
    String getProxyPort();

    /**
     * Gets the proxy user name if any.
     *
     * Eureka-Server 代理用户名
     *
     * @return the proxy user name.
     */
    String getProxyUserName();

    /**
     * Gets the proxy password if any.
     *
     * Eureka-Server 代理密码
     *
     * @return the proxy password.
     */
    String getProxyPassword();

    /**
     * Indicates whether the content fetched from eureka server has to be
     * compressed whenever it is supported by the server. The registry
     * information from the eureka server is compressed for optimum network
     * traffic.
     *
     * @return true, if the content need to be compressed, false otherwise.
     * @deprecated gzip content encoding will be always enforced in the next minor Eureka release (see com.netflix.eureka.GzipEncodingEnforcingFilter).
     */
    boolean shouldGZipContent();

    /**
     * Indicates how long to wait (in seconds) before a read from eureka server
     * needs to timeout.
     *
     * Eureka-Server 读取超时时间
     *
     * @return time in seconds before the read should timeout.
     */
    int getEurekaServerReadTimeoutSeconds();

    /**
     * Indicates how long to wait (in seconds) before a connection to eureka
     * server needs to timeout.
     *
     * Eureka-Server 连接超时时间
     *
     * <p>
     * Note that the connections in the client are pooled by
     * {@link org.apache.http.client.HttpClient} and this setting affects the actual
     * connection creation and also the wait time to get the connection from the
     * pool.
     * </p>
     *
     * @return time in seconds before the connections should timeout.
     */
    int getEurekaServerConnectTimeoutSeconds();

    /**
     * Gets the name of the implementation which implements
     * {@link BackupRegistry} to fetch the registry information as a fall back
     * option for only the first time when the eureka client starts.
     *
     * <p>
     * This may be needed for applications which needs additional resiliency for
     * registry information without which it cannot operate.
     * </p>
     *
     * 获取备份注册中心实现类
     *
     * 当 Eureka-Client 启动时，无法从 Eureka-Server 读取注册信息（可能挂了），从备份注册中心读取注册信息
     * ps：目前 Eureka-Client 未提供合适的实现。
     *
     * @return the class name which implements {@link BackupRegistry}.
     */
    String getBackupRegistryImpl();

    /**
     * Gets the total number of connections that is allowed from eureka client
     * to all eureka servers.
     *
     * 所有 Eureka-Server 总连接数
     * todo 是指connection，还是指注册的实例个数？ 是connection
     *
     * @return total number of allowed connections from eureka client to all
     *         eureka servers.
     */
    int getEurekaServerTotalConnections();

    /**
     * Gets the total number of connections that is allowed from eureka client
     * to a eureka server host.
     *
     * 单个 Eureka-Server 总连接数
     * todo 同上
     *
     * @return total number of allowed connections from eureka client to a
     *         eureka server.
     */
    int getEurekaServerTotalConnectionsPerHost();

    /**
     * Gets the URL context to be used to construct the <em>service url</em> to
     * contact eureka server when the list of eureka servers come from the
     * DNS.This information is not required if the contract returns the service
     * urls by implementing {@link #getEurekaServerServiceUrls(String)}.
     *
     * <p>
     * The DNS mechanism is used when
     * {@link #shouldUseDnsForFetchingServiceUrls()} is set to <em>true</em> and
     * the eureka client expects the DNS to configured a certain way so that it
     * can fetch changing eureka servers dynamically.
     * </p>
     *
     * <p>
     * <em>The changes are effective at runtime.</em>
     * </p>
     *
     * Eureka-Server 的 URL Context
     * DNS方式才需要
     *
     * @return the string indicating the context {@link java.net.URI} of the eureka
     *         server.
     */
    String getEurekaServerURLContext();

    /**
     * Gets the port to be used to construct the <em>service url</em> to contact
     * eureka server when the list of eureka servers come from the DNS.This
     * information is not required if the contract returns the service urls by
     * implementing {@link #getEurekaServerServiceUrls(String)}.
     *
     * <p>
     * The DNS mechanism is used when
     * {@link #shouldUseDnsForFetchingServiceUrls()} is set to <em>true</em> and
     * the eureka client expects the DNS to configured a certain way so that it
     * can fetch changing eureka servers dynamically.
     * </p>
     *
     * <p>
     * <em>The changes are effective at runtime.</em>
     * </p>
     *
     * Eureka-Server 的 Port
     * DNS方式才需要
     *
     * @return the string indicating the port where the eureka server is
     *         listening.
     */
    String getEurekaServerPort();

    /**
     * Gets the DNS name to be queried to get the list of eureka servers.This
     * information is not required if the contract returns the service urls by
     * implementing {@link #getEurekaServerServiceUrls(String)}.
     *
     * <p>
     * The DNS mechanism is used when
     * {@link #shouldUseDnsForFetchingServiceUrls()} is set to <em>true</em> and
     * the eureka client expects the DNS to configured a certain way so that it
     * can fetch changing eureka servers dynamically.
     * </p>
     *
     * <p>
     * <em>The changes are effective at runtime.</em>
     * </p>
     *
     * Eureka-Server 的 DNS 名
     *
     * @return the string indicating the DNS name to be queried for eureka
     *         servers.
     */
    String getEurekaServerDNSName();

    /**
     * Indicates whether the eureka client should use the DNS mechanism to fetch
     * a list of eureka servers to talk to. When the DNS name is updated to have
     * additional servers, that information is used immediately after the eureka
     * client polls for that information as specified in
     * {@link #getEurekaServiceUrlPollIntervalSeconds()}.
     *
     * <p>
     * Alternatively, the service urls can be returned
     * {@link #getEurekaServerServiceUrls(String)}, but the users should implement
     * their own mechanism to return the updated list in case of changes.
     * </p>
     *
     * <p>
     * <em>The changes are effective at runtime.</em>
     * </p>
     *
     * 是否使用 DNS 获取 Eureka-Server 地址集合
     *
     * @return true if the DNS mechanism should be used for fetching urls, false otherwise.
     */
    boolean shouldUseDnsForFetchingServiceUrls();

    /**
     * Indicates whether or not this instance should register its information
     * with eureka server for discovery by others.
     *
     * <p>
     * In some cases, you do not want your instances to be discovered whereas
     * you just want do discover other instances.
     * </p>
     *
     * 是否向 Eureka-Server 注册自身服务
     *
     * @return true if this instance should register with eureka, false
     *         otherwise
     */
    boolean shouldRegisterWithEureka();

    /**
     * Indicates whether the client should explicitly unregister itself from the remote server
     * on client shutdown.
     *
     * 是否向 Eureka-Server 取消注册自身服务，当进程关闭时
     *
     * @return true if this instance should unregister with eureka on client shutdown, false otherwise
     */
    boolean shouldUnregisterOnShutdown();

    /**
     * Indicates whether or not this instance should try to use the eureka
     * server in the same zone for latency and/or other reason.
     *
     * <p>
     * Ideally eureka clients are configured to talk to servers in the same zone
     * </p>
     *
     * <p>
     * <em>The changes are effective at runtime at the next registry fetch cycle as specified by
     * {@link #getRegistryFetchIntervalSeconds()}</em>
     * </p>
     *
     * 优先使用相同 Zone 的 Eureka-Server
     * todo zone是aws的内容？是的，没有使用aws，就是defaultZone，即只有一个zone，所有server都在这个zone里面
     * 默认是true
     *
     * @return true if the eureka client should prefer the server in the same
     *         zone, false otherwise.
     */
    boolean shouldPreferSameZoneEureka();

    /**
     * Indicates whether server can redirect a client request to a backup server/cluster.
     * If set to false, the server will handle the request directly, If set to true, it may
     * send HTTP redirect to the client, with a new server location.
     *
     * 是否允许被 Eureka-Server 重定向
     *
     * @return true if HTTP redirects are allowed
     */
    boolean allowRedirects();

    /**
     * Indicates whether to log differences between the eureka server and the
     * eureka client in terms of registry information.
     *
     * <p>
     * Eureka client tries to retrieve only delta changes from eureka server to
     * minimize network traffic. After receiving the deltas, eureka client
     * reconciles the information from the server to verify it has not missed
     * out some information. Reconciliation failures could happen when the
     * client has had network issues communicating to server.If the
     * reconciliation fails, eureka client gets the full registry information.
     * </p>
     *
     * <p>
     * While getting the full registry information, the eureka client can log
     * the differences between the client and the server and this setting
     * controls that.
     * </p>
     * <p>
     * <em>The changes are effective at runtime at the next registry fetch cycle as specified by
     * {@link #getRegistryFetchIntervalSeconds()}</em>
     * </p>
     *
     * // TODO 后面看
     *
     * @return true if the eureka client should log delta differences in the
     *         case of reconciliation failure.
     */
    boolean shouldLogDeltaDiff();

    /**
     * Indicates whether the eureka client should disable fetching of delta and
     * should rather resort to getting the full registry information.
     *
     * <p>
     * Note that the delta fetches can reduce the traffic tremendously, because
     * the rate of change with the eureka server is normally much lower than the
     * rate of fetches.
     * </p>
     * <p>
     * <em>The changes are effective at runtime at the next registry fetch cycle as specified by
     * {@link #getRegistryFetchIntervalSeconds()}</em>
     * </p>
     *
     * // TODO 后面看
     *
     * @return true to enable fetching delta information for registry, false to
     *         get the full registry.
     */
    boolean shouldDisableDelta();

    /**
     * Comma separated list of regions for which the eureka registry information will be fetched. It is mandatory to
     * define the availability zones for each of these regions as returned by {@link #getAvailabilityZones(String)}.
     * Failing to do so, will result in failure of discovery client startup.
     *
     * 获取哪些区域( Region )集合的注册信息
     * TODO 芋艿：null为获取全部，得再确认下     ？？？？
     *
     * @return Comma separated list of regions for which the eureka registry information will be fetched.
     * <code>null</code> if no remote region has to be fetched.
     */
    @Nullable
    String fetchRegistryForRemoteRegions();

    /**
     * Gets the region (used in AWS datacenters) where this instance resides.
     *
     * 所在区域( Region )
     *
     * @return AWS region where this instance resides.
     */
    String getRegion();

    /**
     * Gets the list of availability zones (used in AWS data centers) for the
     * region in which this instance resides.
     *
     * <p>
     * <em>The changes are effective at runtime at the next registry fetch cycle as specified by
     * {@link #getRegistryFetchIntervalSeconds()}</em>
     * </p>
     * @param region the region where this instance is deployed.
     *
     * 获得所在区域( Region ) 里可用区集合( Zone )
     *
     * @return the list of available zones accessible by this instance.
     */
    String[] getAvailabilityZones(String region);

    /**
     * Gets the list of fully qualified {@link java.net.URL}s to communicate with eureka
     * server.
     *
     * <p>
     * Typically the eureka server {@link java.net.URL}s carry protocol,host,port,context
     * and version information if any.
     * <code>Example: http://ec2-256-156-243-129.compute-1.amazonaws.com:7001/eureka/v2/</code>
     * <p>
     *
     * <p>
     * <em>The changes are effective at runtime at the next service url refresh cycle as specified by
     * {@link #getEurekaServiceUrlPollIntervalSeconds()}</em>
     * </p>
     * @param myZone the zone in which the instance is deployed.
     *
     * 获取 Eureka-Client 所在区域( Zone )的 Eureka-Server 服务地址
     *
     * @return the list of eureka server service urls for eureka clients to talk
     *         to.
     */
    List<String> getEurekaServerServiceUrls(String myZone);

    /**
     * Indicates whether to get the <em>applications</em> after filtering the
     * applications for instances with only {@link com.netflix.appinfo.InstanceInfo.InstanceStatus#UP} states.
     *
     * <p>
     * <em>The changes are effective at runtime at the next registry fetch cycle as specified by
     * {@link #getRegistryFetchIntervalSeconds()}</em>
     * </p>
     *
     * 是否过滤，只获取状态为开启( Up )的应用实例集合
     * 默认为true
     *
     * @return true to filter, false otherwise.
     */
    boolean shouldFilterOnlyUpInstances();

    /**
     * Indicates how much time (in seconds) that the HTTP connections to eureka
     * server can stay idle before it can be closed.
     *
     * <p>
     * In the AWS environment, it is recommended that the values is 30 seconds
     * or less, since the firewall cleans up the connection information after a
     * few mins leaving the connection hanging in limbo
     * </p>
     *
     * Eureka-Server 连接的空闲关闭时间，单位：秒
     *
     * @return time in seconds the connections to eureka can stay idle before it
     *         can be closed.
     */
    int getEurekaConnectionIdleTimeoutSeconds();

    /**
     * Indicates whether this client should fetch eureka registry information from eureka server.
     *
     * 是否从 Eureka-Server 拉取注册信息
     *
     * @return {@code true} if registry information has to be fetched, {@code false} otherwise.
     */
    boolean shouldFetchRegistry();

    /**
     * Indicates whether the client is only interested in the registry information for a single VIP.
     *
     * 只获得一个 `vipAddress` 对应的应用实例们的注册信息。
     * todo 默认为null，null什么意思？
     *
     * {@link AbstractVIPResource}
     * {@link com.netflix.discovery.shared.transport.EurekaHttpClient#getVip(String, String...)}
     *
     * @return the address of the VIP (name:port).
     * <code>null</code> if single VIP interest is not present.
     */
    @Nullable
    String getRegistryRefreshSingleVipAddress();

    /**
     * The thread pool size for the heartbeatExecutor to initialise with
     *
     * 心跳执行线程池大小
     * todo 默认为5，为什么要多线程？
     *
     * @return the heartbeatExecutor thread pool size
     */
    int getHeartbeatExecutorThreadPoolSize();

    /**
     * Heartbeat executor exponential back off related property.
     * It is a maximum multiplier value for retry delay, in case where a sequence of timeouts
     * occurred.
     *
     * 心跳执行超时后的延迟重试的时间（幂指数）
     * 默认10
     *
     * @return maximum multiplier value for retry delay
     */
    int getHeartbeatExecutorExponentialBackOffBound();

    /**
     * The thread pool size for the cacheRefreshExecutor to initialise with
     *
     * 注册信息缓存刷新线程池大小
     * todo 怎么确定是注册信息 缓存又是什么缓存？
     *
     * @return the cacheRefreshExecutor thread pool size
     */
    int getCacheRefreshExecutorThreadPoolSize();

    /**
     * Cache refresh executor exponential back off related property.
     * It is a maximum multiplier value for retry delay, in case where a sequence of timeouts
     * occurred.
     *
     * 注册信息缓存刷新执行超时后的延迟重试的时间。
     *
     * @return maximum multiplier value for retry delay
     */
    int getCacheRefreshExecutorExponentialBackOffBound();

    /**
     * Get a replacement string for Dollar sign <code>$</code> during serializing/deserializing information in eureka server.
     *
     * Eureka-Server 序列化/反序列化信息时，将 $ 替换成的字符串
     * todo 干嘛用的？
     *
     * @return Replacement string for Dollar sign <code>$</code>.
     */
    String getDollarReplacement();

    /**
     * Get a replacement string for underscore sign <code>_</code> during serializing/deserializing information in eureka server.
     *
     * Eureka-Server 序列化/反序列化信息时，将 _ 替换成的字符串
     *
     * @return Replacement string for underscore sign <code>_</code>.
     */
    String getEscapeCharReplacement();

    /**
     * If set to true, local status updates via
     * {@link com.netflix.appinfo.ApplicationInfoManager#setInstanceStatus(com.netflix.appinfo.InstanceInfo.InstanceStatus)}
     * will trigger on-demand (but rate limited) register/updates to remote eureka servers
     *
     * 是否同步应用实例状态到 Eureka-Server
     * 默认true
     *
     * @return true or false for whether local status updates should be updated to remote servers on-demand
     */
    boolean shouldOnDemandUpdateStatusChange();

    /**
     * This is a transient config and once the latest codecs are stable, can be removed (as there will only be one)
     *
     * 编码器名
     *
     * @return the class name of the encoding codec to use for the client. If none set a default codec will be used
     */
    String getEncoderName();

    /**
     * This is a transient config and once the latest codecs are stable, can be removed (as there will only be one)
     *
     * 解码器名
     *
     * @return the class name of the decoding codec to use for the client. If none set a default codec will be used
     */
    String getDecoderName();

    /**
     * Eureka-Client 可接收数据类型
     * todo 和gzip区分，一个是压缩，一个是紧凑/宽松数据结构
     *
     * @return {@link com.netflix.appinfo.EurekaAccept#name()} for client data accept
     */
    String getClientDataAccept();

    /**
     * To avoid configuration API pollution when trying new/experimental or features or for the migration process,
     * the corresponding configuration can be put into experimental configuration section.
     *
     * 获得实验性属性值
     *
     * @return a property of experimental feature
     */
    String getExperimental(String name);

    /**
     * For compatibility, return the transport layer config class
     *
     *
     *
     * @return an instance of {@link EurekaTransportConfig}
     */
    EurekaTransportConfig getTransportConfig();
}
