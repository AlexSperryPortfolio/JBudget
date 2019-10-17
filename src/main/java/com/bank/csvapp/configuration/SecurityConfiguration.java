package com.bank.csvapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests().antMatchers("/").permitAll()
                .and()
                .authorizeRequests().antMatchers("/console/**").permitAll();

        //region Common Security Configuration
        //use custom login page "/login" mapped to login.jsp by IndexController
        httpSecurity
                .formLogin().loginPage("/login").loginProcessingUrl("/login.do") //url->controller    url->action on jsp form
                .defaultSuccessUrl("/",true).failureUrl("/login?err=1")              //success and failure urls
                .usernameParameter("username").passwordParameter("password");   //username and password names on login.jsp

        //advancedSettings
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

}
