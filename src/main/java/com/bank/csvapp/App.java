package com.bank.csvapp;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App 
{
	
	/*@Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String datasourceDriverClassName;

    @Bean
    public DataSource dataSource() {
        DataSource ds = new DataSource();
        ds.setDriverClassName(datasourceDriverClassName);
        ds.setUrl(datasourceUrl);
        ds.setUsername(datasourceUsername);
        ds.setPassword(datasourcePassword);

        return ds;

    }*/

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
