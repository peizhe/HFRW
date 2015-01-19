package com.trying.web.config;

import org.springframework.util.Log4jConfigurer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.FileNotFoundException;

public class Initializer implements WebApplicationInitializer {
    private static final String DISPATCHER_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        try {
            Log4jConfigurer.initLogging("classpath:properties/log4j.properties");
        } catch (FileNotFoundException ignored) {}

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(WebConfig.class);
        servletContext.addListener(new ContextLoaderListener(applicationContext));

        applicationContext.setServletContext(servletContext);

        ServletRegistration.Dynamic servlet = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(applicationContext));
        servlet.addMapping("/");

        servlet.setLoadOnStartup(1);
    }
}