package com.netflix.eureka;

import com.google.common.base.Strings;
import com.netflix.appinfo.AbstractEurekaIdentity;
import com.netflix.servo.monitor.DynamicCounter;
import com.netflix.servo.monitor.MonitorConfig;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * An auth filter for client requests. For now, it only logs supported client identification data from header info
 *
 * Eureka-Server 请求认证过滤器
 *
 */
@Singleton
public class ServerRequestAuthFilter implements Filter {
    public static final String UNKNOWN = "unknown";

    private static final String NAME_PREFIX = "DiscoveryServerRequestAuth_Name_";

    private EurekaServerConfig serverConfig;

    @Inject
    public ServerRequestAuthFilter(EurekaServerContext server) {
        this.serverConfig = server.getServerConfig();
    }

    // for non-DI use
    public ServerRequestAuthFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (serverConfig == null) {
            EurekaServerContext serverContext = (EurekaServerContext) filterConfig.getServletContext()
                    .getAttribute(EurekaServerContext.class.getName());
            serverConfig = serverContext.getServerConfig();
        }
    }

    // 仅仅监控一下header
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logAuth(request);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to do here
    }

    protected void logAuth(ServletRequest request) {
        if (serverConfig.shouldLogIdentityHeaders()) {
            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;

                String clientName = getHeader(httpRequest, AbstractEurekaIdentity.AUTH_NAME_HEADER_KEY);
                String clientVersion = getHeader(httpRequest, AbstractEurekaIdentity.AUTH_VERSION_HEADER_KEY);

                DynamicCounter.increment(MonitorConfig.builder(NAME_PREFIX + clientName + "-" + clientVersion).build());
            }
        }
    }

    protected String getHeader(HttpServletRequest request, String headerKey) {
        String value = request.getHeader(headerKey);
        return Strings.isNullOrEmpty(value) ? UNKNOWN : value;
    }
}
