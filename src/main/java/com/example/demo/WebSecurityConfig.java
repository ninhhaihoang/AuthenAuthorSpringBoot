package com.example.demo;

import com.example.demo.Service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //Page for Admin
        http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");

        //Page for User
        http.authorizeRequests().antMatchers("/user_infor").access("hasAnyRole('ROLE_ADMIN','ROLE_USER')");

        //Access denied exception
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        //Config login
        http.authorizeRequests()
                .antMatchers("/", "/login_form").permitAll() //Pages free access
                .and()
                .formLogin()
                .loginProcessingUrl("/login_check") //Submit URL
                .loginPage("/login_form")
                .defaultSuccessUrl("/list_users")
                .failureUrl("/login_form?error=true")
                .usernameParameter("email")
                .and()
                .logout()
                .logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful"); //Config logout page

        //Remember me
        http.authorizeRequests().and()
                .rememberMe().tokenRepository(this.persistentTokenRepository()).tokenValiditySeconds(1*24*60*60); //24h

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }
}
