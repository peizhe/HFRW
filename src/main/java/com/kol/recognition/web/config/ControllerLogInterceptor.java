package com.kol.recognition.web.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControllerLogInterceptor extends HandlerInterceptorAdapter {

    private static final String STOPWATCH_ATTR = "logging_startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Logger logger = LoggerFactory.getLogger(((HandlerMethod) handler).getBean().getClass());
        logger.info("[START:    " + buildRequestString(request) + "]");
        request.setAttribute(STOPWATCH_ATTR, Stopwatch.createStarted());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        final Logger logger = LoggerFactory.getLogger(((HandlerMethod)handler).getBean().getClass());
        if (null != ex) {
            logger.error("Error " + ex.toString() + " while executing " + request.getRequestURI(), ex);
            response.sendRedirect("error.html");
            return;
        }
        final Stopwatch sw = (Stopwatch) request.getAttribute(STOPWATCH_ATTR);
        request.removeAttribute(STOPWATCH_ATTR);
        logger.info("[COMPLETE: " + sw.stop() + " " + buildRequestString(request) + "]");
    }

    private String buildRequestString(HttpServletRequest request) {
        return request.getMethod() + " " + request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "") + " from " + request.getRemoteAddr();
    }
}