package com.arturjoshi.config;

import com.arturjoshi.authentication.StatelessAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private StatelessAuthenticationFilter statelessAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/register/",
                        "/api/authenticate/",
                        "/api/accounts/**",
                        "/api/projects/**",
                        "/api/milestones/**",
                        "/api/projectAccountPermissions/**",
                        "/api/projectAccountProfiles/**")
                .permitAll()
                .antMatchers("/api/{accountId}/**").access(
                        "#accountId.toString().equals(principal.id.toString())")
                .anyRequest().authenticated()
                .and().addFilterBefore(statelessAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(true)
                .userDetailsService(userDetailsService)
                .passwordEncoder(new ShaPasswordEncoder(256));
    }
}
