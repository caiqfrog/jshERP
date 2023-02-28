package com.jsh.erp.filter;

import com.jsh.erp.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@WebFilter(
        filterName = "LogCostFilter", urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "ignoredUrl", value = ".ico#/doc.html#/user/login#/user/register"),
                @WebInitParam(name = "filterPath",
                        value = "/user/login#/user/registerUser#/user/randomImage#" +
                                "/platformConfig/getPlatform#/v2/api-docs#/webjars#" +
                                "/systemConfig/static#/ping"),
                @WebInitParam(name = "logoutUrl", value = "/user/logout#/function/findMenuByPNumber")
})
public class LogCostFilter implements Filter {

    private static final String FILTER_PATH = "filterPath";
    private static final String IGNORED_PATH = "ignoredUrl";
    private static final String LOGOUT_PATH = "logoutUrl";

    private static final List<String> IGNORED_LIST = new ArrayList<>();
    private List<String> allowUrls;
    private List<String> logoutUrls;
    @Resource
    private RedisService redisService;
    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Override
    public void init(FilterConfig filterConfig) {
        String filterPath = filterConfig.getInitParameter(FILTER_PATH);
        if (!StringUtils.isEmpty(filterPath)) {
            allowUrls = Arrays.stream(filterPath.split("#"))
                    .map(it -> "".equals(contextPath) || "/".equals(contextPath) ? it : contextPath + it)
                    .collect(Collectors.toList());
        }

        String ignoredPath = filterConfig.getInitParameter(IGNORED_PATH);
        if (!StringUtils.isEmpty(ignoredPath)) {
            String[] ignoredUrls = ignoredPath.contains("#") ? ignoredPath.split("#") : new String[]{ignoredPath};
            IGNORED_LIST.addAll(Arrays.asList(ignoredUrls));
        }

        String logoutUrl = filterConfig.getInitParameter(LOGOUT_PATH);
        if (!StringUtils.isEmpty(ignoredPath)) {
            logoutUrls = Arrays.stream(logoutUrl.split("#"))
                    .map(it -> "".equals(contextPath) || "/".equals(contextPath) ? it : contextPath + it)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String requestUrl = servletRequest.getRequestURI();
        // 具体，比如：处理若用户未登录，则跳转到登录页
        Object userId = redisService.getObjectFromSessionByKey(servletRequest,"userId");
        log.info("user {} request {}.", userId, requestUrl);
        if(Objects.isNull(requestUrl) || Objects.nonNull(userId)) {
            // 如果已登录，不阻止
            chain.doFilter(request, response);
            return;
        }
        // filter ignored urls
        for (String key : IGNORED_LIST) {
            if (requestUrl.contains(key)) {
                chain.doFilter(request, response);
                return;
            }
        }
        // filter allow urls
        for (String url : allowUrls) {
            if (requestUrl.startsWith(url)) {
                chain.doFilter(request, response);
                return;
            }
        }
        // filter logout
        for (String url : logoutUrls) {
            if (requestUrl.startsWith(url)) {
                servletResponse.setStatus(HttpStatus.OK.value());
                servletResponse.getWriter().write("login out.");
                return;
            }
        }
        // forbidden
        servletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        servletResponse.getWriter().write("Forbidden");
    }

    @Override
    public void destroy() {

    }
}