package com.bank.csvapp.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.bank.csvapp.domain"})
@EnableJpaRepositories(basePackages = {"com.bank.csvapp.repositories"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}
