package com.trying.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@PropertySource({"classpath:properties/image.properties"})
@ComponentScan({"com.trying.web.config", "com.trying.web.controllers", "com.trying.fe", "com.trying.web.components"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/pages/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/resources/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/resources/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/");
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("/resources/bootstrap/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestProcessingTimeInterceptor());
    }
}