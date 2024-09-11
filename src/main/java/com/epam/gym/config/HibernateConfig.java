package com.epam.gym.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.ddl-auto}")
    private String ddlAuto;
    @Value("${hibernate.show_sql}")
    private String showSql;


    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.epam.gym.entity");

        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", dialect);
        hibernateProperties.put("hibernate.hbm2ddl.auto", ddlAuto);
        hibernateProperties.put("hibernate.show_sql", showSql);

        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }
}