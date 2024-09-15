package com.epam.gym.config;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.epam")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({DataSourceConfig.class, HibernateConfig.class, TransactionConfig.class, JacksonConfig.class})
public class ApplicationConfig {
}