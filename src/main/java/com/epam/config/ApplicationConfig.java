package com.epam.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.epam")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class ApplicationConfig {
}