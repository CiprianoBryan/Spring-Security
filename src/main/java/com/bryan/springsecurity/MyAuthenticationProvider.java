package com.bryan.springsecurity;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    // THE IMPLEMENTATION LOGIC
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        if ("tom".equals(userName) && "cruise".equals(password)) {
            return new UsernamePasswordAuthenticationToken(userName, password, List.of());
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    // TELLS THE AUTHENTICATION-MANAGER THAT THIS GUY CAN SUPPORT A USERNAME PASSWORD AUTHENTICATION TYPE
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
