package com.arturjoshi.authentication.token;

import com.arturjoshi.authentication.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Component
public class TokenHandler {

    @Value("${server.secretKey}")
    private String secretKey;
    @Value("${server.expirationTime}")
    private String expirationTime;

    @Autowired
    private UserDetailsService userDetailsService;

    public AccountDetails getUserFromToken(String token) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String decoded = new String(Base64.decode(token.getBytes()));
        String[] strings = decoded.split(":");
        String username = strings[0];
        long timestamp = Long.parseLong(strings[1]);
        if (timestamp < System.currentTimeMillis()) {
            System.out.println("TokenHandler: token has expired");
            return null;
        }
        byte[] digest = Base64.decode(strings[2].getBytes());

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        StringBuilder builder = new StringBuilder();
        builder.append(username).append(':').append(timestamp).append(':').append(((AccountDetails) userDetails).getPassword())
                .append(':').append(secretKey);
        byte[] expectedDigest = md5.digest(builder.toString().getBytes());
        if (Arrays.equals(expectedDigest, digest)) {
            return (AccountDetails) userDetails;
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    public String   createTokenForUser(UserDetails user) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        long timestamp = System.currentTimeMillis() + Long.parseLong(expirationTime);
        builder.append(user.getUsername()).append(':').append(timestamp).append(':').append(user.getPassword())
                .append(':').append(secretKey);
        String digest = new String(Base64.encode(md5.digest(builder.toString().getBytes())));
        builder = new StringBuilder();
        builder.append(user.getUsername()).append(':').append(timestamp).append(':').append(digest);
        return new String(Base64.encode(builder.toString().getBytes()));
    }
}
