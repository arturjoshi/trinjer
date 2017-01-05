package com.arturjoshi.authentication;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private String token;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        token = httpServletRequest.getHeader(AUTH_HEADER_NAME);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public String getToken() {
        return token;
    }

    public static String getAuthHeaderName() {
        return AUTH_HEADER_NAME;
    }
}