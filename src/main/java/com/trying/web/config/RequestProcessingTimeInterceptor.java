package com.trying.web.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestProcessingTimeInterceptor extends HandlerInterceptorAdapter {

    private static final DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final long startTime = System.currentTimeMillis();
        System.out.println("[" + format.format(new Date()) + "]  INFO  [" + ((HandlerMethod) handler).getBean().getClass().getSimpleName() + "]   -   Start [" + request.getRequestURI() + "]");
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        final long startTime = (Long) request.getAttribute("startTime");
        if(null == ex) {
            System.out.println(
                    "[" + format.format(new Date()) + "]  INFO  [" + ((HandlerMethod) handler).getBean().getClass().getSimpleName() + "]   -   Complete [" +
                            request.getRequestURI() + "]. Execution Time = " + (System.currentTimeMillis() - startTime)
            );
        } else {
            System.out.println(
                    "[" + format.format(new Date()) + "]  ERROR  [" + ((HandlerMethod) handler).getBean().getClass().getSimpleName() + "]   -   Exception [" +
                            request.getRequestURI() + "][" + ex.getMessage() + "]. Execution Time = " + (System.currentTimeMillis() - startTime)
            );
        }
    }
}