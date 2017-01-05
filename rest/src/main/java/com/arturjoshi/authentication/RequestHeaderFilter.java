package com.arturjoshi.authentication;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestHeaderFilter extends GenericFilterBean {

    private HttpHeaders httpHeaders = new HttpHeaders();
    private final Collection<String> HEADERS = Collections.singletonList("X-AUTH-TOKEN");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        for (String currentHeaderName : HEADERS) {
            String header = httpServletRequest.getHeader(currentHeaderName);
            if(header != null) {
                httpHeaders.put(currentHeaderName, Collections.singletonList(header));
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }
}