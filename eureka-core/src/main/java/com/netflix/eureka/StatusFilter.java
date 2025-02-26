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

package com.netflix.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to check whether the eureka server is ready to take requests based on
 * its {@link InstanceStatus}.
 */
@Singleton
public class StatusFilter implements Filter {

    private static final int SC_TEMPORARY_REDIRECT = 307;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    //todo 几个filter顺序是在那里？？在resource包里的web.xml
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        InstanceInfo myInfo = ApplicationInfoManager.getInstance().getInfo();
        InstanceStatus status = myInfo.getStatus();
        if (status != InstanceStatus.UP && response instanceof HttpServletResponse) {
            HttpServletResponse httpRespone = (HttpServletResponse) response;
            httpRespone.sendError(SC_TEMPORARY_REDIRECT,
                    "Current node is currently not ready to serve requests -- current status: "
                            + status + " - try another DS node: ");
        }
        chain.doFilter(request, response);
    }
    // TODO 芋艿：测试下307的反馈

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig arg0) throws ServletException {
    }

}
