package com.bank.csvapp.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.bank.csvapp.domain"}) //<--Tell DAO accessing functions to look for entities in mvc.models package
@EnableJpaRepositories(basePackages = {"com.bank.csvapp.repositories"}) //<--Tell DAO accessing functions to look for repositories in mvc.repositories package
@EnableTransactionManagement
public class RepositoryConfiguration {
}
