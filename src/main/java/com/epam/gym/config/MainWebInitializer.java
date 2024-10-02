package com.epam.gym.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MainWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ApplicationConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        var authFilter = servletContext.addFilter("authenticationFilter", new DelegatingFilterProxy("authenticationFilter"));
        authFilter.addMappingForUrlPatterns(null, false,
                "/v1/trainee/*",
                "/v1/trainer/*",
                "/v1/training/*",
                "/v1/training-type/*",
                "/v1/auth/change-password/*");
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}