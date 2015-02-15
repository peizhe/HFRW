package com.kol.recognition.config;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(servletNames = {"dispatcher"})
public class GatewayFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MDC.put("user", request.getRemoteAddr());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
