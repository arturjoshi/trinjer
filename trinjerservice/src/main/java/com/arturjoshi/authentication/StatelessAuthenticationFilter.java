package com.arturjoshi.authentication;

import com.arturjoshi.authentication.token.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StatelessAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Authentication authentication = tokenAuthenticationService.getAuthentication(httpServletRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest, servletResponse);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}