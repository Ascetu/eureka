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
package com.netflix.appinfo;

import com.google.inject.ImplementedBy;

import java.util.Map;

/**
 * Configuration information required by the instance to register with Eureka
 * server. Once registered, users can look up information from
 * {@link com.netflix.discovery.EurekaClient} based on virtual hostname (also called VIPAddress),
 * the most common way of doing it or by other means to get the information
 * necessary to talk to other instances registered with <em>Eureka</em>.
 *
 * <P>
 * As requirements of registration, an id and an appname must be supplied. The id should be
 * unique within the scope of the appname.
 * </P>
 *
 * <p>
 * Note that all configurations are not effective at runtime unless and
 * otherwise specified.
 * </p>
 * // TODO 芋艿：疑问，这段 Note 的用途
 *
 * Eureka 应用实例配置接口
 *
 * @author Karthik Ranganathan
 */
@ImplementedBy(CloudInstanceConfig.class)
public interface EurekaInstanceConfig {

    /**
     * Get the unique Id (within the scope of the appName) of this instance to be registered with eureka.
     *
     * 对象编号。
     * 需要保证在相同应用名下唯一。
     *
     * @return the (appname scoped) unique id for this instance
     */
    String getInstanceId();

    /**
     * Get the name of the application to be registered with eureka.
     *
     * 应用名
     *
     * @return string denoting the name.
     */
    String getAppname();

    /**
     * Get the name of the application group to be registered with eureka.
     *
     * 应用分组
     *
     * @return string denoting the name.
     */
    String getAppGroupName(); // TODO 芋艿：看看用途

    /**
     * Indicates whether the instance should be enabled for taking traffic as
     * soon as it is registered with eureka. Sometimes the application might
     * need to do some pre-processing before it is ready to take traffic.
     *
     * :( public API typos are the worst. I think this was meant to be "OnInit".
     *
     * 应用初始化后是否开启
     *
     * @return true to immediately start taking traffic, false otherwise.
     */
    boolean isInstanceEnabledOnit();

    /**
     * Get the <code>non-secure</code> port on which the instance should receive
     * traffic.
     *
     * 应用 http 端口
     *
     * @return the non-secure port on which the instance should receive traffic.
     */
    int getNonSecurePort();

    /**
     * Get the <code>Secure port</code> on which the instance should receive
     * traffic.
     *
     * 应用 https 端口
     *
     * @return the secure port on which the instance should receive traffic.
     */
    int getSecurePort();

    /**
     * Indicates whether the <code>non-secure</code> port should be enabled for
     * traffic or not.
     *
     * 应用 http 端口是否开启
     *
     * @return true if the <code>non-secure</code> port is enabled, false
     *         otherwise.
     */
    boolean isNonSecurePortEnabled();

    /**
     * Indicates whether the <code>secure</code> port should be enabled for
     * traffic or not.
     *
     * 应用 https 端口是否开启
     *
     * @return true if the <code>secure</code> port is enabled, false otherwise.
     */
    boolean getSecurePortEnabled();

    /**
     * Indicates how often (in seconds) the eureka client needs to send
     * heartbeats to eureka server to indicate that it is still alive. If the
     * heartbeats are not received for the period specified in
     * {@link #getLeaseExpirationDurationInSeconds()}, eureka server will remove
     * the instance from its view, there by disallowing traffic to this
     * instance.
     *
     * <p>
     * Note that the instance could still not take traffic if it implements
     * {@link HealthCheckCallback} and then decides to make itself unavailable.
     * </p>
     *
     * 租约续约频率，单位：秒。
     * 应用不断通过按照该频率发送心跳给 Eureka-Server 以达到续约的作用。
     * 当 Eureka-Server 超过最大频率【租约过期时间】未收到续约（心跳），契约失效，进行应用移除。
     * 应用移除后，其他应用无法从 Eureka-Server 获取该应用。
     *
     * @return time in seconds
     */
    int getLeaseRenewalIntervalInSeconds();

    /**
     * Indicates the time in seconds that the eureka server waits since it
     * received the last heartbeat before it can remove this instance from its
     * view and there by disallowing traffic to this instance.
     *
     * <p>
     * Setting this value too long could mean that the traffic could be routed
     * to the instance even though the instance is not alive. Setting this value
     * too small could mean, the instance may be taken out of traffic because of
     * temporary network glitches.This value to be set to atleast higher than·
     * the value specified in {@link #getLeaseRenewalIntervalInSeconds()}
     * .
     * </p>
     *
     * 租约过期时间，单位：秒
     * todo ？？？仅仅server用到，client不会用到，为什么不做区分
     *
     * @return value indicating time in seconds.
     */
    int getLeaseExpirationDurationInSeconds();

    /**
     * Gets the virtual host name defined for this instance.
     *
     * <p>
     * This is typically the way other instance would find this instance by
     * using the virtual host name.Think of this as similar to the fully
     * qualified domain name, that the users of your services will need to find
     * this instance.
     * </p>
     *
     * 虚拟主机名。
     * 也可以叫做 VIPAddress 。
     *
     * @return the string indicating the virtual host name which the clients use
     *         to call this service.
     */
    String getVirtualHostName();

    /**
     * Gets the secure virtual host name defined for this instance.
     *
     * <p>
     * This is typically the way other instance would find this instance by
     * using the secure virtual host name.Think of this as similar to the fully
     * qualified domain name, that the users of your services will need to find
     * this instance.
     * </p>
     *
     * 虚拟安全主机名
     * 也可以叫做 SecureVIPAddress 。
     *
     * @return the string indicating the secure virtual host name which the
     *         clients use to call this service.
     */
    String getSecureVirtualHostName();

    /**
     * Gets the <code>AWS autoscaling group name</code> associated with this
     * instance. This information is specifically used in an AWS environment to
     * automatically put an instance out of service after the instance is
     * launched and it has been disabled for traffic..
     *
     * 跳过：AWS 使用
     *
     * @return the autoscaling group name associated with this instance.
     */
    String getASGName();

    /**
     * Gets the hostname associated with this instance. This is the exact name
     * that would be used by other instances to make calls.
     *
     * todo 和getVirtualHostName区别？真正的hostname地址吗？
     *
     * @param refresh
     *            true if the information needs to be refetched, false
     *            otherwise.
     * @return hostname of this instance which is identifiable by other
     *         instances for making remote calls.
     */
    String getHostName(boolean refresh);

    /**
     * Gets the metadata name/value pairs associated with this instance. This
     * information is sent to eureka server and can be used by other instances.
     *
     * 元数据( Metadata )集合
     *
     * @return Map containing application-specific metadata.
     */
    Map<String, String> getMetadataMap();

    /**
     * Returns the data center this instance is deployed. This information is
     * used to get some AWS specific instance information if the instance is
     * deployed in AWS.
     *
     * 数据中心信息
     * todo 数据中心是什么意思？
     *
     * @return information that indicates which data center this instance is
     *         deployed in.
     */
    DataCenterInfo getDataCenterInfo();

    /**
     * Get the IPAdress of the instance. This information is for academic
     * purposes only as the communication from other instances primarily happen
     * using the information supplied in {@link #getHostName(boolean)}.
     *
     * IP 地址
     * 获取实例的IPAdress。此信息仅用于学术目的，因为来自其他实例的通信主要使用 {@link getHostName(boolean)} 中提供的信息进行
     *
     * @return the ip address of this instance.
     */
    String getIpAddress();

    /**
     * Gets the relative status page {@link java.net.URL} <em>Path</em> for this
     * instance. The status page URL is then constructed out of the
     * {@link #getHostName(boolean)} and the type of communication - secure or
     * unsecure as specified in {@link #getSecurePort()} and
     * {@link #getNonSecurePort()}.
     *
     * 组合使用：hostname+port+path，其他服务查询当前实例状态（todo 什么时候会调用url查询状态？服务之间没有，server通过续约心跳，这个是干嘛用的）
     *
     * <p>
     * It is normally used for informational purposes for other services to find
     * about the status of this instance. Users can provide a simple
     * <code>HTML</code> indicating what is the current status of the instance.
     * </p>
     *
     * @return - relative <code>URL</code> that specifies the status page.
     */
    String getStatusPageUrlPath();

    /**
     * Gets the absolute status page {@link java.net.URL} for this instance. The users
     * can provide the {@link #getStatusPageUrlPath()} if the status page
     * resides in the same instance talking to eureka, else in the cases where
     * the instance is a proxy for some other server, users can provide the full
     * {@link java.net.URL}. If the full {@link java.net.URL} is provided it takes precedence.
     *
     * 优先级高于getStatusPageUrlPath
     *
     * <p>
     * * It is normally used for informational purposes for other services to
     * find about the status of this instance. Users can provide a simple
     * <code>HTML</code> indicating what is the current status of the instance.
     * . The full {@link java.net.URL} should follow the format
     * http://${eureka.hostname}:7001/ where the value ${eureka.hostname} is
     * replaced at runtime.
     * </p>
     *
     * @return absolute status page URL of this instance.
     */
    String getStatusPageUrl();

    /**
     * Gets the relative home page {@link java.net.URL} <em>Path</em> for this instance.
     * The home page URL is then constructed out of the
     * {@link #getHostName(boolean)} and the type of communication - secure or
     * unsecure as specified in {@link #getSecurePort()} and
     * {@link #getNonSecurePort()}.
     *
     * 主页（相对地址）
     *
     * <p>
     * It is normally used for informational purposes for other services to use
     * it as a landing page.
     * </p>
     *
     * @return relative <code>URL</code> that specifies the home page.
     */
    String getHomePageUrlPath();

    /**
     * Gets the absolute home page {@link java.net.URL} for this instance. The users can
     * provide the {@link #getHomePageUrlPath()} if the home page resides in the
     * same instance talking to eureka, else in the cases where the instance is
     * a proxy for some other server, users can provide the full {@link java.net.URL}. If
     * the full {@link java.net.URL} is provided it takes precedence.
     *
     * 优先级高于getHomePageUrlPath；todo 主页是做什么用的？server页面上点击查看用的？statusUrl和这个差不多用途吗？
     *
     * <p>
     * It is normally used for informational purposes for other services to use
     * it as a landing page. The full {@link java.net.URL} should follow the format
     * http://${eureka.hostname}:7001/ where the value ${eureka.hostname} is
     * replaced at runtime.
     * </p>
     *
     * @return absolute home page URL of this instance.
     */
    String getHomePageUrl();

    /**
     * Gets the relative health check {@link java.net.URL} <em>Path</em> for this
     * instance. The health check page URL is then constructed out of the
     * {@link #getHostName(boolean)} and the type of communication - secure or
     * unsecure as specified in {@link #getSecurePort()} and
     * {@link #getNonSecurePort()}.
     *
     * 健康检查path todo 用途？健康检查失败，节点状态变为DOWN，即节点还存在，但是不能接受流量；谁去检查的？
     *
     * <p>
     * It is normally used for making educated decisions based on the health of
     * the instance - for example, it can be used to determine whether to
     * proceed deployments to an entire farm or stop the deployments without
     * causing further damage.
     * </p>
     *
     * @return - relative <code>URL</code> that specifies the health check page.
     */
    String getHealthCheckUrlPath();

    /**
     * Gets the absolute health check page {@link java.net.URL} for this instance. The
     * users can provide the {@link #getHealthCheckUrlPath()} if the health
     * check page resides in the same instance talking to eureka, else in the
     * cases where the instance is a proxy for some other server, users can
     * provide the full {@link java.net.URL}. If the full {@link java.net.URL} is provided it
     * takes precedence.
     *
     * 优先级高于getHealthCheckUrlPath
     *
     * <p>
     * It is normally used for making educated decisions based on the health of
     * the instance - for example, it can be used to determine whether to
     * proceed deployments to an entire farm or stop the deployments without
     * causing further damage.  The full {@link java.net.URL} should follow the format
     * http://${eureka.hostname}:7001/ where the value ${eureka.hostname} is
     * replaced at runtime.
     * </p>
     *
     * @return absolute health check page URL of this instance.
     */
    String getHealthCheckUrl();

    /**
     * Gets the absolute secure health check page {@link java.net.URL} for this instance.
     * The users can provide the {@link #getSecureHealthCheckUrl()} if the
     * health check page resides in the same instance talking to eureka, else in
     * the cases where the instance is a proxy for some other server, users can
     * provide the full {@link java.net.URL}. If the full {@link java.net.URL} is provided it
     * takes precedence.
     *
     * ssl健康检查url
     *
     * <p>
     * It is normally used for making educated decisions based on the health of
     * the instance - for example, it can be used to determine whether to
     * proceed deployments to an entire farm or stop the deployments without
     * causing further damage. The full {@link java.net.URL} should follow the format
     * http://${eureka.hostname}:7001/ where the value ${eureka.hostname} is
     * replaced at runtime.
     * </p>
     *
     * @return absolute health check page URL of this instance.
     */
    String getSecureHealthCheckUrl();

    /**
     * An instance's network addresses should be fully expressed in it's {@link DataCenterInfo}.
     * For example for instances in AWS, this will include the publicHostname, publicIp,
     * privateHostname and privateIp, when available. The {@link com.netflix.appinfo.InstanceInfo}
     * will further express a "default address", which is a field that can be configured by the
     * registering instance to advertise it's default address. This configuration allowed
     * for the expression of an ordered list of fields that can be used to resolve the default
     * address. The exact field values will depend on the implementation details of the corresponding
     * implementing DataCenterInfo types.
     *
     * // TODO 芋艿，亚马逊，暂时跳过
     * 默认[]
     * todo 默认地址？数据中心（DataCenterInfo）相关
     *
     * @return an ordered list of fields that should be used to preferentially
     *         resolve this instance's default address, empty String[] for default.
     */
    String[] getDefaultAddressResolutionOrder();

    /**
     * Get the namespace used to find properties.
     *
     * 配置命名空间
     * 用于查找属性； 例如：eureka.client.*****这样？是的，默认默认使用 eureka
     * 基于配置文件的实现才会实现此方法？PropertiesInstanceConfig
     *
     * @return the namespace used to find properties.
     */
    String getNamespace();

}
