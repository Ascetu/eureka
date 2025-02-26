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

import com.google.inject.ProvidedBy;
import com.netflix.appinfo.providers.MyDataCenterInstanceConfigProvider;

import javax.inject.Singleton;

/**
 * An {@link InstanceInfo} configuration for the non-AWS datacenter.
 *
 * 非 AWS 数据中心的 Eureka 应用实例配置实现类
 * ProvidedBy todo 工厂？
 *
 * @author Karthik Ranganathan
 */
@Singleton
@ProvidedBy(MyDataCenterInstanceConfigProvider.class)
public class MyDataCenterInstanceConfig extends PropertiesInstanceConfig implements EurekaInstanceConfig {

    public MyDataCenterInstanceConfig() {
    }

    public MyDataCenterInstanceConfig(String namespace) {
        super(namespace);
    }

    public MyDataCenterInstanceConfig(String namespace, DataCenterInfo dataCenterInfo) {
        super(namespace, dataCenterInfo);
    }

}
