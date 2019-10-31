package com.bank.jbudget.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.bank.jbudget.domain"})
@EnableJpaRepositories(basePackages = {"com.bank.jbudget.repositories"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}
