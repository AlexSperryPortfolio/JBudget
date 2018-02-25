package com.bank.csvapp.configuration;


/*import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Value("${spring.security.authentication.method}")
    private String authenticationMethod;*/

    /*@Value("${spring.security.ldap.domain}")
    private String ldapDomain;

    @Value("${spring.security.ldap.url}")
    private String ldapUrl;*/

    /*@Autowired
    private DataSource dataSource;*/

    //In memory authentication (impractical)
    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        if(authenticationMethod.equals("NONE")) {

        } */
        /*else if(authenticationMethod.equals("IN_MEMORY")) {
            auth.inMemoryAuthentication().withUser("user").password("123").roles("USER");
            auth.inMemoryAuthentication().withUser("admin").password("123").roles("ADMIN");
            auth.inMemoryAuthentication().withUser("dba").password("123").roles("DBA");

        } else if (authenticationMethod.equals("LDAP")) {

            auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());

        } else if (authenticationMethod.equals("DATABASE")) {
            JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
            userDetailsService.setDataSource(dataSource);
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
            auth.jdbcAuthentication().dataSource(dataSource);

            if(!userDetailsService.userExists("user")) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("USER"));
                User userDetails = new User("user", encoder.encode("password"), authorities);

                userDetailsService.createUser(userDetails);
            }
        }*/

    /*}

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {*/

        /*if(authenticationMethod.equals("NONE")) {
            httpSecurity
                    .authorizeRequests().antMatchers("/").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/console/**").permitAll();

        } */
        /*else if (authenticationMethod.equals("IN_MEMORY")) {*/
            //region Access Control
            /*httpSecurity*/
                    //permit all with no authentication
//                .authorizeRequests().antMatchers("/").permitAll()

                    //create authentication for ADMIN and anything with the URL=/admin/**
                    /*.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                    .and()
                    .authorizeRequests().antMatchers("/console/**").access("hasRole('ROLE_DBA')");*/

//                .and()
//                .authorizeRequests().antMatchers("/").access("hasRole");

            //region Login Page
            //default login page
//        httpSecurity.formLogin();

        /*} else if (authenticationMethod.equals("LDAP")) {
            httpSecurity
                    .authorizeRequests().antMatchers("/static/**").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/login**").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated();

        } else if (authenticationMethod.equals("DATABASE")) {
            httpSecurity
                    .authorizeRequests().antMatchers("/static/**").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/login**").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/admin/**").hasAuthority("USER")
                    .anyRequest().authenticated();

        }*/

        //region Common Security Configuration
        //use custom login page "/login" mapped to login.jsp by IndexController
//        httpSecurity
//                .formLogin().loginPage("/login").loginProcessingUrl("/login.do") //url->controller    url->action on jsp form
//                .defaultSuccessUrl("/",true).failureUrl("/login?err=1")              //success and failure urls
//                .usernameParameter("username").passwordParameter("password");   //username and password names on login.jsp

        //advancedSettings
        /*httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }*/

  /*  @Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider authenticationProvider =
                new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);

        authenticationProvider.setConvertSubErrorCodesToExceptions(true);
        authenticationProvider.setUseAuthenticationRequestCredentials(true);

        return authenticationProvider;
    }*/

//}
