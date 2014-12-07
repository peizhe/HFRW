package com.trying.web.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RequestProcessingTimeInterceptor extends HandlerInterceptorAdapter {

    private static final DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("[" + now() + "] [" + controller(handler) + "]   -   " + "Start [" + action(request) + "]");
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(null == ex) {
            System.out.println("[" + now() + "] [" + controller(handler) + "]   -   Complete [" + action(request) + "]. Execution Time = " + time(request));
        } else {
            ex.printStackTrace();
        }
    }

    private static String time(final HttpServletRequest request) {
        return (System.currentTimeMillis() - (Long) request.getAttribute("startTime")) + " ms";
    }

    private static String now(){
        return format.format(new Date());
    }

    private static String controller(final Object handler){
        return ((HandlerMethod) handler).getBean().getClass().getSimpleName();
    }

    private static String action(final HttpServletRequest request){
        final String parameters = request.getParameterMap().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()[0]).collect(Collectors.joining("&"));
        return request.getRequestURI() + (parameters.isEmpty() ? "" : "?" + parameters);
    }
}