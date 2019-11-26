package com.bank.jbudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.bank.jbudget")
public class JBudget
{
    public static void main(String[] args) {
        SpringApplication.run(JBudget.class, args);
    }
}
