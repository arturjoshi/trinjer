package com.arturjoshi.authentication.token;

import com.arturjoshi.authentication.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Service
public class TokenAuthenticationService {
    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    @Autowired
    private TokenHandler tokenHandler;

    public Authentication getAuthentication(HttpServletRequest request) throws BadCredentialsException {

        String token = request.getHeader(AUTH_HEADER_NAME);
        if(token != null){
            AccountDetails userDetails = tokenHandler.getUserFromToken(token);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                return authentication;
            }
        }
        return null;
    }
}
